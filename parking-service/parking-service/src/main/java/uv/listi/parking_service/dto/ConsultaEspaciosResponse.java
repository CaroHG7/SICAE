package uv.listi.parking_service.dto;

import lombok.Data;

@Data
public class ConsultaEspaciosResponse {
    private Integer idEspacio;
    private String claveEspacio;
    private String tipo;
    private Boolean ocupado;
    private Boolean estatus;

    
    public ConsultaEspaciosResponse(Integer idEspacio, String claveEspacio, String tipo, Boolean ocupado,
            Boolean estatus) {
        this.idEspacio = idEspacio;
        this.claveEspacio = claveEspacio;
        this.tipo = tipo;
        this.ocupado = ocupado;
        this.estatus = estatus;
    }


    

    
}
