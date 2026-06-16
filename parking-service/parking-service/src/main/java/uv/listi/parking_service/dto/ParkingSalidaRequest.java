package uv.listi.parking_service.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ParkingSalidaRequest {

    @NotBlank(message = "La clave del usuario es necesaria para continuar")
    private String claveUsuario;

    @NotBlank(message = "La placa del vehículo es necesaria para continuar")
    private String placa;
}