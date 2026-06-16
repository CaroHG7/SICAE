
package uv.listi.user_service.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import uv.listi.user_service.dto.TokenValidationResponse;

@Component
public class AuthServiceClient {

    @Value("${auth.service.url}")
    private String authServiceUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    public boolean validarToken(String token) {

        try {

            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", token);

            HttpEntity<Void> entity =
                    new HttpEntity<>(headers);

            ResponseEntity<TokenValidationResponse> response =
                    restTemplate.exchange(
                            authServiceUrl + "/auth/validar",
                            HttpMethod.GET,
                            entity,
                            TokenValidationResponse.class
                    );

            return response.getBody() != null
                    && response.getBody().isValid();

        } catch (Exception e) {
            return false;
        }
    }
    
}