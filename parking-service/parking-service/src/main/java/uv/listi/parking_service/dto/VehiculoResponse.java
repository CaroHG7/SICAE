package uv.listi.parking_service.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class VehiculoResponse {

    private Integer idVehiculo;
    private String placa;
    private Integer idUsuario;
    private Boolean estatus;

    public boolean estaActivo() {
        return Boolean.TRUE.equals(estatus);
    }
}