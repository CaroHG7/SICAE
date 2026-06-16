package uv.listi.parking_service.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UsuarioResponse {

    private Integer idUsuario;
    private String claveUsuario;
    private String estatus;

    public boolean estaActivo() {
        return "1".equals(estatus)
                || "true".equalsIgnoreCase(estatus);
    }
}