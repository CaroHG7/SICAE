package uv.listi.vehicle_service.model;

import lombok.Data;

@Data
public class Vehiculo {

    private Integer idVehiculo;
    private Integer idUsuario;
    private String claveVehiculo;
    private Integer idModelo;
    private String placa;
    private String color;
    private Integer anio;
    private String descripcion;
    private Boolean estatus;
    
}