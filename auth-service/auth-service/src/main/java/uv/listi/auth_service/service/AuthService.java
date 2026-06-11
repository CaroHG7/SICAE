package uv.listi.auth_service.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import uv.listi.auth_service.dto.LoginRequest;
import uv.listi.auth_service.dto.LoginResponse;
import uv.listi.auth_service.model.UsuarioAuth;
import uv.listi.auth_service.repository.AuthRepository;
import uv.listi.auth_service.security.JwtUtil;

@Service
public class AuthService {

    private final AuthRepository authRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthService(AuthRepository authRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.authRepository = authRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public LoginResponse login(LoginRequest request) {
        if (request.getUsuario() == null || request.getUsuario().trim().isEmpty()) {
            return new LoginResponse(false, "El campo usuario es obligatorio", null, null, null, null, null, null, null, null);
        }
        if (request.getPassword() == null || request.getPassword().trim().isEmpty()) {
            return new LoginResponse(false, "El campo password es obligatorio", null, null, null, null, null, null, null, null);
        }

        UsuarioAuth usuarioOpt = authRepository.buscarPorUsuario(request.getUsuario());

        if (usuarioOpt == null) {
            return new LoginResponse(false, "Usuario no encontrado", null, null, null, null, null, null, null, null);
        }

        if (!"1".equals(usuarioOpt.getEstatus())) {
            return new LoginResponse(false, "Usuario inactivo", null, null, null, null, null, null, null, null);
        }

        if (!passwordEncoder.matches(request.getPassword(), usuarioOpt.getPassword())) {
            return new LoginResponse(false, "Contraseña incorrecta", null, null, null, null, null, null, null, null);
        }

        String nombreCompleto = usuarioOpt.getNombre() + " " + usuarioOpt.getApellidoPaterno();
        if (usuarioOpt.getApellidoMaterno() != null && !usuarioOpt.getApellidoMaterno().trim().isEmpty()) {
            nombreCompleto += " " + usuarioOpt.getApellidoMaterno();
        }

        String token = jwtUtil.generarToken(usuarioOpt);

        return new LoginResponse(
                true,
                "Login correcto",
                token,
                usuarioOpt.getIdUsuario(),
                usuarioOpt.getIdRol(),
                usuarioOpt.getRol(),
                usuarioOpt.getUsername(),
                nombreCompleto,
                usuarioOpt.getIdTipoUsuario(),
                usuarioOpt.getTipoUsuario()
        );
    }
}
