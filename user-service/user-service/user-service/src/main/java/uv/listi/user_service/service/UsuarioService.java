package uv.listi.user_service.service;

import java.time.LocalDateTime;
import java.util.Random;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import uv.listi.user_service.dto.UsuarioPerfilResponse;
import uv.listi.user_service.dto.UsuarioRegistroRequest;
import uv.listi.user_service.dto.UsuarioResponse;
import uv.listi.user_service.model.Usuario;
import uv.listi.user_service.repository.UsuarioRepository;
import uv.listi.user_service.dto.UsuarioEditarRequest;
import uv.listi.user_service.dto.UsuarioEstatusRequest;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UsuarioResponse registrarUsuario(UsuarioRegistroRequest request) {

        if (usuarioRepository.existeEmail(request.getEmail()) > 0) {
            return new UsuarioResponse(false, "Ya existe un usuario registrado con ese email");
        }

        if (usuarioRepository.existeUsername(request.getUsername()) > 0) {
            return new UsuarioResponse(false, "Ya existe un usuario registrado con ese username");
        }

        Usuario usuario = new Usuario();

        usuario.setNombre(request.getNombre());
        usuario.setApellidoPaterno(request.getApellidoPaterno());
        usuario.setApellidoMaterno(request.getApellidoMaterno());
        usuario.setEmail(request.getEmail());
        usuario.setTelefono(request.getTelefono());
        usuario.setUsername(request.getUsername());

        String passwordCifrada = passwordEncoder.encode(request.getPassword());
        usuario.setPassword(passwordCifrada);

        usuario.setIdRol(request.getIdRol());
        usuario.setIdTipoUsuario(request.getIdTipoUsuario());
        usuario.setIdProgramaEducativo(request.getIdProgramaEducativo());

        String claveGenerada = generarClaveUsuario();
        usuario.setClaveUsuario(claveGenerada);

        usuario.setTiempoCreacion(LocalDateTime.now());

        int filasAfectadas = usuarioRepository.registrar(usuario);

        if (filasAfectadas > 0) {
            return new UsuarioResponse(true, "Usuario registrado correctamente con clave: " + claveGenerada);
        }

        return new UsuarioResponse(false, "No se pudo registrar el usuario");
    }

    public UsuarioPerfilResponse obtenerPerfil(Integer idUsuario) {

        if (idUsuario == null || idUsuario <= 0) {
            return null;
        }

        return usuarioRepository.obtenerPerfilPorId(idUsuario);
    }

    private String generarClaveUsuario() {
        Random random = new Random();
        String clave;

        do {
            int numero = random.nextInt(900) + 100;
            clave = "USR-" + numero;
        } while (usuarioRepository.existeClaveUsuario(clave) > 0);

        return clave;
    }
    
    public UsuarioResponse editarUsuario(Integer idUsuario, UsuarioEditarRequest request) {

        if (idUsuario == null || idUsuario <= 0) {
            return new UsuarioResponse(false, "El idUsuario no es válido");
        }

        if (usuarioRepository.existeUsuarioPorId(idUsuario) == 0) {
            return new UsuarioResponse(false, "No existe el usuario que deseas editar");
        }

        if (usuarioRepository.existeEmailEnOtroUsuario(request.getEmail(), idUsuario) > 0) {
            return new UsuarioResponse(false, "Ya existe otro usuario registrado con ese email");
        }

        int filasAfectadas = usuarioRepository.editarUsuario(
                idUsuario,
                request.getNombre(),
                request.getApellidoPaterno(),
                request.getApellidoMaterno(),
                request.getEmail(),
                request.getTelefono(),
                request.getIdRol(),
                request.getIdTipoUsuario(),
                request.getIdProgramaEducativo()
        );

        if (filasAfectadas > 0) {
            return new UsuarioResponse(true, "Usuario actualizado correctamente");
        }

        return new UsuarioResponse(false, "No se pudo actualizar el usuario");
    }

    public UsuarioResponse cambiarEstatus(Integer idUsuario, UsuarioEstatusRequest request) {

        if (idUsuario == null || idUsuario <= 0) {
            return new UsuarioResponse(false, "El idUsuario no es válido");
        }

        if (usuarioRepository.existeUsuarioPorId(idUsuario) == 0) {
            return new UsuarioResponse(false, "No existe el usuario solicitado");
        }

        int filasAfectadas = usuarioRepository.cambiarEstatus(idUsuario, request.getEstatus());

        if (filasAfectadas > 0) {
            String mensaje = request.getEstatus()
                    ? "Usuario activado correctamente"
                    : "Usuario desactivado correctamente";

            return new UsuarioResponse(true, mensaje);
        }

        return new UsuarioResponse(false, "No se pudo cambiar el estatus del usuario");
    }
}