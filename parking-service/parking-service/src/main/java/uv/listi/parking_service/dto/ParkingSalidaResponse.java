package uv.listi.parking_service.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class ParkingSalidaResponse {

    private Integer idMovimiento;
    private LocalDateTime tiempoEntrada;
    private LocalDateTime tiempoSalida;
    private Integer espacio;
    private BigDecimal tarifa;
    private BigDecimal costoTotal;
    private Integer horasCobradas;
    private String mensaje;

    
    public ParkingSalidaResponse(Integer idMovimiento, LocalDateTime tiempoEntrada, LocalDateTime tiempoSalida,
            Integer espacio, BigDecimal tarifa, BigDecimal costoTotal, Integer horasCobradas, String mensaje) {
        this.idMovimiento = idMovimiento;
        this.tiempoEntrada = tiempoEntrada;
        this.tiempoSalida = tiempoSalida;
        this.espacio = espacio;
        this.tarifa = tarifa;
        this.costoTotal = costoTotal;
        this.horasCobradas = horasCobradas;
        this.mensaje = mensaje;
    }



    

    
}
