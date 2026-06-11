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
            // 1. Header
            String header = "{\"alg\":\"HS256\",\"typ\":\"JWT\"}";
            String headerBase64 = Base64.getUrlEncoder().withoutPadding().encodeToString(header.getBytes(StandardCharsets.UTF_8));

            // 2. Payload
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

            // 3. Signature
            String signatureData = headerBase64 + "." + payloadBase64;
            Mac mac = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKeySpec = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
            mac.init(secretKeySpec);
            byte[] signatureBytes = mac.doFinal(signatureData.getBytes(StandardCharsets.UTF_8));
            String signatureBase64 = Base64.getUrlEncoder().withoutPadding().encodeToString(signatureBytes);

            // 4. JWT Final
            return signatureData + "." + signatureBase64;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
