package uv.listi.parking_service.dto;

import lombok.Data;

@Data
public class UsuarioResponse {
    
    private Integer idUsuario;
    private String claveUsuario;
    private boolean status;

    

    public UsuarioResponse(Integer idUsuario, String claveUsuario, boolean status) {
        this.idUsuario = idUsuario;
        this.claveUsuario = claveUsuario;
        this.status = status;
    }
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
