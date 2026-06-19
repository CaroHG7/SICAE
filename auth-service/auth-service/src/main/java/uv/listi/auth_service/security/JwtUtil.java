package uv.listi.auth_service.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import uv.listi.auth_service.model.UsuarioAuth;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expiration;

    public String generarToken(UsuarioAuth usuario) {
        try {
            //  header del JWT
            String header = "{\"alg\":\"HS256\",\"typ\":\"JWT\"}";
            String headerBase64 = Base64.getUrlEncoder().withoutPadding().encodeToString(header.getBytes(StandardCharsets.UTF_8));

            // colocamos la información del usuario en el payload
            long nowMillis = System.currentTimeMillis();
            long expMillis = nowMillis + expiration;

            String payload = String.format(
                "{\"sub\":\"%s\",\"idUsuario\":%d,\"idRol\":%d,\"rol\":\"%s\",\"idTipoUsuario\":%d,\"tipoUsuario\":\"%s\",\"iat\":%d,\"exp\":%d}",
                usuario.getUsername(),
                usuario.getIdUsuario(),
                usuario.getIdRol(),
                usuario.getRol(),
                usuario.getIdTipoUsuario(),
                usuario.getTipoUsuario(),
                nowMillis / 1000,
                expMillis / 1000
            );
            String payloadBase64 = Base64.getUrlEncoder().withoutPadding().encodeToString(payload.getBytes(StandardCharsets.UTF_8));

            // generamos la firma con nuestra clave secreta para evitar modificaciones
            String signatureData = headerBase64 + "." + payloadBase64;
            Mac mac = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKeySpec = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
            mac.init(secretKeySpec);
            byte[] signatureBytes = mac.doFinal(signatureData.getBytes(StandardCharsets.UTF_8));
            String signatureBase64 = Base64.getUrlEncoder().withoutPadding().encodeToString(signatureBytes);

            // unimos las tres partes con puntos, tal como vimos en clase
            return signatureData + "." + signatureBase64;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean validarToken(String token) {
        if (token == null || token.trim().isEmpty()) {
            return false;
        }

        // dividimos el token para asegurar que tenga header, payload y firma
        String[] parts = token.split("\\.");
        if (parts.length != 3) {
            return false;
        }

        try {
            String headerAndPayload = parts[0] + "." + parts[1];
            String firmaCalculada = calcularFirma(headerAndPayload);

            // comprobamos que la firma coincida para confirmar que no fue alterado
            if (!firmaCalculada.equals(parts[2])) {
                return false;
            }

            String payloadJson = decodificarPayload(parts[1]);
            String expString = obtenerValorNumericoClaim(payloadJson, "exp");

            // verificamos la fecha de expiración del token
            if (expString != null) {
                long exp = Long.parseLong(expString);
                long now = System.currentTimeMillis() / 1000;
                if (now >= exp) {
                    return false;
                }
            } else {
                return false;
            }

            return true; 
        } catch (Exception e) {
            return false;
        }
    }

    public String obtenerUsuario(String token) {
        if (!validarToken(token)) {
            return null;
        }

        try {
            String[] parts = token.split("\\.");
            String payloadJson = decodificarPayload(parts[1]);
            return obtenerValorStringClaim(payloadJson, "sub");
        } catch (Exception e) {
            return null;
        }
    }
    
    public Integer obtenerIdRol(String token) {
        if (!validarToken(token)) {
            return null;
        }

        try {
            String[] parts = token.split("\\.");
            String payloadJson = decodificarPayload(parts[1]);
            String idRolString = obtenerValorNumericoClaim(payloadJson, "idRol");
            
            if (idRolString != null) {
                return Integer.parseInt(idRolString);
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    private String calcularFirma(String data) throws Exception {
        Mac mac = Mac.getInstance("HmacSHA256");
        SecretKeySpec secretKeySpec = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        mac.init(secretKeySpec);
        byte[] signatureBytes = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
        return Base64.getUrlEncoder().withoutPadding().encodeToString(signatureBytes);
    }

    private String decodificarPayload(String payloadBase64) {
        byte[] decodedBytes = Base64.getUrlDecoder().decode(payloadBase64);
        return new String(decodedBytes, StandardCharsets.UTF_8);
    }

    private String obtenerValorStringClaim(String json, String key) {
        String searchKey = "\"" + key + "\":\"";
        int startIndex = json.indexOf(searchKey);
        if (startIndex == -1) {
            return null;
        }
        startIndex += searchKey.length();
        int endIndex = json.indexOf("\"", startIndex);
        if (endIndex == -1) {
            return null;
        }
        return json.substring(startIndex, endIndex);
    }

    private String obtenerValorNumericoClaim(String json, String key) {
        String searchKey = "\"" + key + "\":";
        int startIndex = json.indexOf(searchKey);
        if (startIndex == -1) {
            return null;
        }
        startIndex += searchKey.length();
        int endIndex = json.indexOf(",", startIndex);
        if (endIndex == -1) {
            endIndex = json.indexOf("}", startIndex);
        }
        if (endIndex == -1) {
            return null;
        }
        return json.substring(startIndex, endIndex).trim();
    }
}
