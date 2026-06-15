package uv.listi.parking_service.dto;

import lombok.Data;

@Data
public class UsuarioResponse {
    
    private Integer idUsuario;
    private String claveUsuario;
    private boolean status;


    public Integer getIdUsuario() {
        return idUsuario;
    }
    public String getClaveUsuario() {
        return claveUsuario;
    }
    public boolean getStatus() {
        return status;
    }

}
