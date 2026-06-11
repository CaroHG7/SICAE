package uv.listi.auth_service.dto;

import lombok.Data;

@Data
public class LoginRequest {
    private String usuario;
    private String password;
}
