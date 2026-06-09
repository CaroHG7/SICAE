package uv.listi.user_service.dto;

import jakarta.validation.constraints.NotNull;

public class UsuarioEstatusRequest {

    @NotNull(message = "El estatus es obligatorio")
    private Boolean estatus; //boleano true-false

    public Boolean getEstatus() {
        return estatus;
    }

    public void setEstatus(Boolean estatus) {
        this.estatus = estatus;
    }
}