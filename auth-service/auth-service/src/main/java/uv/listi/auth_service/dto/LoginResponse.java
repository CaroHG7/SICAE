package uv.listi.auth_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {
    private boolean success;
    private String mensaje;
    private String token;
    private Integer idUsuario;
    private Integer idRol;
    private String rol;
    private String usuario;
    private String nombreCompleto;
    private Integer idTipoUsuario;
    private String tipoUsuario;
}
