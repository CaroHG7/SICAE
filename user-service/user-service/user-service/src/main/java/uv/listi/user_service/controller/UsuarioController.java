package uv.listi.user_service.controller;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import uv.listi.user_service.dto.UsuarioPerfilResponse;
import uv.listi.user_service.dto.UsuarioRegistroRequest;
import uv.listi.user_service.dto.UsuarioResponse;
import uv.listi.user_service.service.UsuarioService;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping("/test")
    public String test() {
        return "UserService funcionando correctamente con BD oficial";
    }

    @PostMapping
    public ResponseEntity<UsuarioResponse> registrarUsuario(
            @Valid @RequestBody UsuarioRegistroRequest request) {

        UsuarioResponse response = usuarioService.registrarUsuario(request);

        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        }

        return ResponseEntity.badRequest().body(response);
    }

    @GetMapping("/{idUsuario}")
    public ResponseEntity<?> obtenerPerfil(@PathVariable Integer idUsuario) {

        UsuarioPerfilResponse perfil = usuarioService.obtenerPerfil(idUsuario);

        if (perfil == null) {
            return ResponseEntity.badRequest().body(
                    new UsuarioResponse(false, "No se encontró el usuario solicitado")
            );
        }

        return ResponseEntity.ok(perfil);
    }
}