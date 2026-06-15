package uv.listi.parking_service.config;



import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import uv.listi.parking_service.dto.TokenValidationResponse;

@Component
public class JwtUtil {

    
    private final RestTemplate restTemplate;
    private final String urlValidar="http://localhost:8080/auth/validate";

    public JwtUtil(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String obtenerToken(String header) {
        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7);
        }
        return null;
    }

    public boolean validarToken(String token){
        try{
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", token);
            HttpEntity<Void> entity = new HttpEntity<>(headers);
            ResponseEntity<TokenValidationResponse> response = restTemplate.exchange(urlValidar, HttpMethod.POST, entity, TokenValidationResponse.class);

            return response.getBody() != null && response.getBody().isValid();
        }catch (Exception e){
            return false;
        }
    }

    
}
