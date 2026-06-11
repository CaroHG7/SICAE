package uv.listi.auth_service.model;

import lombok.Data;

@Data
public class UsuarioAuth {
    private Integer idUsuario;
    private String nombre;
    private String apellidoPaterno;
    private String apellidoMaterno;
    private String username;
    private String password;
    private String estatus;
    private Integer idRol;
    private String rol;
    private Integer idTipoUsuario;
    private String tipoUsuario;
}
