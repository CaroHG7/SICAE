package uv.listi.parking_service.dto;

import lombok.Data;

@Data
public class ConsultaEspaciosResponse {
    private Integer idEspacio;
    private String claveEspacio;
    private String tipo;
    private Boolean ocupado;
    private Boolean estatus;

    
}
