package uv.listi.parking_service.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class ParkingSalidaResponse {

    private Integer idMovimiento;
    private LocalDateTime tiempoEntrada;
    private LocalDateTime tiempoSalida;
    private String espacio;
    private BigDecimal tarifa;
    private BigDecimal costoTotal;
    private Integer horasCobradas;

    private String mensaje;

    
}
