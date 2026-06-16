package uv.listi.parking_service.service;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import uv.listi.parking_service.dto.TokenValidationResponse;

@Service
public class ValidarToken {

    private final RestTemplate restTemplate;
    private final String AUTH_URL = "http://localhost:8080/auth/validar";

    public ValidarToken(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public boolean validar(String token) {

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", token);

            HttpEntity<Void> entity = new HttpEntity<>(headers);

            ResponseEntity<TokenValidationResponse> response =
                    restTemplate.exchange(
                            AUTH_URL,
                            HttpMethod.GET,
                            entity,
                            TokenValidationResponse.class
                    );

            return response.getBody() != null && response.getBody().isValid();

        } catch (Exception e) {
            System.err.println("Error al validar el token: " + e.getMessage());
            return false;
        }
    }
}