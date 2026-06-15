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
    
}
