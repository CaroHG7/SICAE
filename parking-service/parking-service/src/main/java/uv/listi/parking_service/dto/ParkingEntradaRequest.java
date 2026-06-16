package uv.listi.parking_service.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ParkingEntradaRequest {
    @NotBlank(message = "La clave del usuaria es necesaria para continuar.")
    private String claveUsuario;

    @NotBlank(message = ("La placa es necesaria para continuar."))
    private String placa;

    @NotNull(message = "EL tiempo de entrada es necesario para continuar.")
    private LocalDateTime tiempoEntrada;

    @NotNull(message="el tiempo de creacion es necesariopara continuar.")
    private LocalDateTime tiempoCreacion;

    @NotNull(message="La tarifa es necesaria para continuar")
    private BigDecimal tarifa;

    @NotNull(message = "El id del espacio de estacionamiento es necesaria para continuar.")
    private Integer idEspacio;

    public ParkingEntradaRequest(
            @NotBlank(message = "La clave del usuaria es necesaria para continuar.") String claveUsuario,
            @NotBlank(message = "La placa es necesaria para continuar.") String placa,
            @NotNull(message = "EL tiempo de entrada es necesario para continuar.") LocalDateTime tiempoEntrada,
            @NotNull(message = "el tiempo de creacion es necesariopara continuar.") LocalDateTime tiempoCreacion,
            @NotNull(message = "La tarifa es necesaria para continuar") BigDecimal tarifa,
            @NotNull(message = "El id del espacio de estacionamiento es necesaria para continuar.") Integer idEspacio) {
        this.claveUsuario = claveUsuario;
        this.placa = placa;
        this.tiempoEntrada = tiempoEntrada;
        this.tiempoCreacion = tiempoCreacion;
        this.tarifa = tarifa;
        this.idEspacio = idEspacio;
    }

    
    
}
