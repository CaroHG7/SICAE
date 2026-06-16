package uv.listi.parking_service.dto;

import lombok.Data;

@Data
public class VehiculoResponse {
    
    private Integer idVehiculo;
    private String placa;
    private Integer idUsuario;
    

    public VehiculoResponse(Integer idVehiculo, String placa, Integer idUsuario) {
        this.idVehiculo = idVehiculo;
        this.placa = placa;
        this.idUsuario = idUsuario;
    }
    public Integer getIdVehiculo() {
        return idVehiculo;
    }
    public String getPlaca() {
        return placa;
    }
    public Integer getIdUsuario() {
        return idUsuario;
    }
    
    
}