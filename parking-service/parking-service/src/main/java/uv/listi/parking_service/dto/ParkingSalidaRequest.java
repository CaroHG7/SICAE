package uv.listi.parking_service.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ParkingSalidaRequest {

    @NotBlank(message = "La clave del usuario es necesaria para continuar")
    private String claveUsuario;
    
    @NotBlank(message="La placa del vehiculo es necesaria para continuar")
    private String placa;

    @NotNull(message="El tiempo de salida es necesario para continuar")
    private LocalDateTime tiempoSalida;

    @NotNull(message="El tiempo de actualizacion es necesario para continuar")
    private LocalDateTime tiempoActualizacion;

    @NotNull(message="El costo total es necesario para continuar")
    private BigDecimal costoTotal;

    @NotNull(message="Las horas cobradas son necesarias para continuar")
    private Integer horasCobradas;

    @NotNull(message="Los minutos estacionados son necesarios para continuar")
    private Integer minutosEstacionados;

    public ParkingSalidaRequest(
            @NotBlank(message = "La clave del usuario es necesaria para continuar") String claveUsuario,
            @NotBlank(message = "La placa del vehiculo es necesaria para continuar") String placa,
            @NotNull(message = "El tiempo de salida es necesario para continuar") LocalDateTime tiempoSalida,
            @NotNull(message = "El tiempo de actualizacion es necesario para continuar") LocalDateTime tiempoActualizacion,
            @NotNull(message = "El costo total es necesario para continuar") BigDecimal costoTotal,
            @NotNull(message = "Las horas cobradas son necesarias para continuar") Integer horasCobradas,
            @NotNull(message = "Los minutos estacionados son necesarios para continuar") Integer minutosEstacionados) {
        this.claveUsuario = claveUsuario;
        this.placa = placa;
        this.tiempoSalida = tiempoSalida;
        this.tiempoActualizacion = tiempoActualizacion;
        this.costoTotal = costoTotal;
        this.horasCobradas = horasCobradas;
        this.minutosEstacionados = minutosEstacionados;
    }

    
    
}
