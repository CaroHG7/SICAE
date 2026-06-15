package uv.listi.parking_service.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Data;


@Data
public class ParkingEntradaResponse {


    private Integer idMovimiento;
    private Integer idEspacio;
    private LocalDateTime entrada;
    private BigDecimal tarifa;
    
    private String mensaje;    

    
}
