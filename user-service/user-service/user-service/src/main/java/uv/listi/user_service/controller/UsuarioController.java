package uv.listi.user_service.controller;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import uv.listi.user_service.dto.UsuarioPerfilResponse;
import uv.listi.user_service.dto.UsuarioRegistroRequest;
import uv.listi.user_service.dto.UsuarioResponse;
import uv.listi.user_service.service.UsuarioService;
import uv.listi.user_service.dto.UsuarioEditarRequest;
import uv.listi.user_service.dto.UsuarioEstatusRequest;

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
    public ResponseEntity<UsuarioResponse> registrarUsuario(
            @Valid @RequestBody UsuarioRegistroRequest request) {

        UsuarioResponse response = usuarioService.registrarUsuario(request);

        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        }

        return ResponseEntity.badRequest().body(response);
    }

    @GetMapping("/{idUsuario}") //info usuario x id
    public ResponseEntity<?> obtenerPerfil(@PathVariable Integer idUsuario) {

        UsuarioPerfilResponse perfil = usuarioService.obtenerPerfil(idUsuario);

        if (perfil == null) {
            return ResponseEntity.badRequest().body(
                    new UsuarioResponse(false, "No se encontró el usuario solicitado")
            );
        }

        return ResponseEntity.ok(perfil);
    }
    
    @PutMapping("/{idUsuario}") //editar
    public ResponseEntity<UsuarioResponse> editarUsuario(
            @PathVariable Integer idUsuario,
            @Valid @RequestBody UsuarioEditarRequest request) {

        UsuarioResponse response = usuarioService.editarUsuario(idUsuario, request);

        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        }

        return ResponseEntity.badRequest().body(response);
    }

    @PatchMapping("/{idUsuario}/estatus") //cambiar estatus
    public ResponseEntity<UsuarioResponse> cambiarEstatus(
            @PathVariable Integer idUsuario,
            @Valid @RequestBody UsuarioEstatusRequest request) {

        UsuarioResponse response = usuarioService.cambiarEstatus(idUsuario, request);

        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        }

        return ResponseEntity.badRequest().body(response);
    }
}