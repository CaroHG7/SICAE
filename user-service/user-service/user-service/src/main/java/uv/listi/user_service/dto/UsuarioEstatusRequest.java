package uv.listi.user_service.dto;

import jakarta.validation.constraints.NotNull;

public class UsuarioEstatusRequest {

    @NotNull(message = "El estatus es obligatorio")
    private Boolean estatus; //boleano true-false

    @NotNull(message = "El rol es obligatorio") 
    private Integer idRol;
    
    public Boolean getEstatus() {
        return estatus;
    }

    public void setEstatus(Boolean estatus) {
        this.estatus = estatus;
    }
    
    public Integer getIdRol() {
        return idRol;
    }

    public void setIdRol(Integer idRol) {
        this.idRol = idRol;
    }
}