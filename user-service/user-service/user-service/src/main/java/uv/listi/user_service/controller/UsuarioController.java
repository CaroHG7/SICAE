package uv.listi.user_service.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import uv.listi.user_service.dto.UsuarioEditarRequest;
import uv.listi.user_service.dto.UsuarioEstatusRequest;
import uv.listi.user_service.dto.UsuarioPerfilResponse;
import uv.listi.user_service.dto.UsuarioRegistroRequest;
import uv.listi.user_service.dto.UsuarioResponse;
import uv.listi.user_service.service.UsuarioService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping("/test") //prueba
    public String test() {
        return "UserService funcionando correctamente con BD oficial";
    }
    
    @PostMapping //crear usuario
    public ResponseEntity<?> registrarUsuario(
            @Valid @RequestBody UsuarioRegistroRequest requestDto,
            HttpServletRequest request) { 

       
        Integer idRolToken = (Integer) request.getAttribute("idRolAuth");

        // solo admin puede registrar
        if (idRolToken == null || idRolToken != 1) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(new UsuarioResponse(false, "No tienes permisos de administrador para realizar esta acción"));
        }

        UsuarioResponse response = usuarioService.registrarUsuario(requestDto);

        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        }

        return ResponseEntity.badRequest().body(response);
    }
    
    @GetMapping("/{idUsuario}") //info usuario x id
    public ResponseEntity<?> obtenerPerfil(
            @PathVariable Integer idUsuario,
            HttpServletRequest request) { //leemos token

        Integer idRolToken = (Integer) request.getAttribute("idRolAuth");
        String usernameAutenticado = (String) request.getAttribute("usuarioAuth");

        UsuarioPerfilResponse perfil = usuarioService.obtenerPerfil(idUsuario);

        if (perfil == null) {
            return ResponseEntity.badRequest().body(
                    new UsuarioResponse(false, "No se encontró el usuario solicitado")
            );
        }

        boolean esAdministrador = (idRolToken != null && idRolToken == 1);
        boolean esMismoUsuario = perfil.getUsername() != null && perfil.getUsername().equals(usernameAutenticado);

        if (!esAdministrador && !esMismoUsuario) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
                    new UsuarioResponse(false, "Acceso denegado: No tienes permiso para visualizar este perfil.")
            );
        }

        return ResponseEntity.ok(perfil);
    }

    @GetMapping("/clave/{claveUsuario}") // info usuario x claveUsuario
    public ResponseEntity<?> obtenerPerfilPorClave(
            @PathVariable String claveUsuario,
            HttpServletRequest request) { 

        Integer idRolToken = (Integer) request.getAttribute("idRolAuth");
        String usernameAutenticado = (String) request.getAttribute("usuarioAuth");

        UsuarioPerfilResponse perfil = usuarioService.obtenerPerfilPorClave(claveUsuario);

        if (perfil == null) {
            return ResponseEntity.badRequest().body(
                    new UsuarioResponse(false, "No se encontró el usuario solicitado")
            );
        }

        boolean esAdministrador = (idRolToken != null && idRolToken == 1);
        boolean esMismoUsuario = perfil.getUsername() != null && perfil.getUsername().equals(usernameAutenticado);

        if (!esAdministrador && !esMismoUsuario) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
                    new UsuarioResponse(false, "Acceso denegado: No tienes permiso para visualizar este perfil.")
            );
        }

        return ResponseEntity.ok(perfil);
    }
    
    @PutMapping("/{idUsuario}") //editar
    public ResponseEntity<?> editarUsuario(
            @PathVariable Integer idUsuario,
            @Valid @RequestBody UsuarioEditarRequest requestDto,
            HttpServletRequest request) { 

        Integer idRolToken = (Integer) request.getAttribute("idRolAuth");
        String usernameAutenticado = (String) request.getAttribute("usuarioAuth");

        UsuarioPerfilResponse usuarioAEditar = usuarioService.obtenerPerfil(idUsuario);

        if (usuarioAEditar == null) {
            return ResponseEntity.badRequest().body(new UsuarioResponse(false, "El usuario no existe"));
        }

        //si no es admin ni es el propio usuario intentando editar sus propios datos
        boolean esAdministrador = (idRolToken != null && idRolToken == 1);
        boolean esMismoUsuario = usuarioAEditar.getUsername().equals(usernameAutenticado);

        if (!esAdministrador && !esMismoUsuario) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new UsuarioResponse(false, "Acceso denegado: No tienes permiso para editar este perfil."));
        }

        UsuarioResponse response = usuarioService.editarUsuario(idUsuario, requestDto);

        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        }

        return ResponseEntity.badRequest().body(response);
    }
    
    @PatchMapping("/{idUsuario}/estatus") //cambiar estatus
    public ResponseEntity<?> cambiarEstatus(
            @PathVariable Integer idUsuario,
            @Valid @RequestBody UsuarioEstatusRequest requestDto,
            HttpServletRequest request) {

        //solo admin
        Integer idRolToken = (Integer) request.getAttribute("idRolAuth");
        if (idRolToken == null || idRolToken != 1) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new UsuarioResponse(false, "Acceso denegado: Solo los administradores pueden cambiar el estatus."));
        }

        try {
            UsuarioResponse response = usuarioService.cambiarEstatus(idUsuario, requestDto);

            if (response.isSuccess()) {
                return ResponseEntity.ok(response);
            }
            return ResponseEntity.badRequest().body(response);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new UsuarioResponse(false, e.getMessage()));
        }
    }
}