package uv.listi.auth_service.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uv.listi.auth_service.dto.LoginRequest;
import uv.listi.auth_service.dto.LoginResponse;
import uv.listi.auth_service.dto.TokenValidationResponse;
import uv.listi.auth_service.security.JwtUtil;
import uv.listi.auth_service.service.AuthService;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final JwtUtil jwtUtil;

    public AuthController(AuthService authService, JwtUtil jwtUtil) {
        this.authService = authService;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping("/test")
    public String test() {
        return "AuthService funcionando correctamente";
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) {
        return authService.login(request);
    }

    @GetMapping("/validar")
    public TokenValidationResponse validar(@RequestHeader(value = "Authorization", required = false) String authHeader) {
        if (authHeader == null || authHeader.trim().isEmpty()) {
            return new TokenValidationResponse(false, "Token no proporcionado", null);
        }

        if (!authHeader.startsWith("Bearer ")) {
            return new TokenValidationResponse(false, "Formato de token inválido", null);
        }

        String token = authHeader.substring(7);

        try {
            boolean esValido = jwtUtil.validarToken(token);
            if (!esValido) {
                return new TokenValidationResponse(false, "Token inválido o expirado", null);
            }

            String usuario = jwtUtil.obtenerUsuario(token);
            return new TokenValidationResponse(true, "Token válido", usuario);
        } catch (Exception e) {
            return new TokenValidationResponse(false, "Token inválido o expirado", null);
        }
    }
}
