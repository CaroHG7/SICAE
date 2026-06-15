package uv.listi.parking_service.model;

import lombok.Data;

@Data
public class Espacio {

    private Integer idEspacio;
    private String claveEspacio;
    private String tipo;
    private Boolean ocupado;
    private Boolean estatus;
    

    public Espacio() {
    }

    public Espacio(Integer idEspacio, String claveEspacio, String tipo, Boolean ocupado, Boolean estatus) {
        this.idEspacio = idEspacio;
        this.claveEspacio = claveEspacio;
        this.tipo = tipo;
        this.ocupado = ocupado;
        this.estatus = estatus;
    }
    public Integer getIdEspacio() {
        return idEspacio;
    }
    public void setIdEspacio(Integer idEspacio) {
        this.idEspacio = idEspacio;
    }
    public String getClaveEspacio() {
        return claveEspacio;
    }
    public void setClaveEspacio(String claveEspacio) {
        this.claveEspacio = claveEspacio;
    }
    public String getTipo() {
        return tipo;
    }
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
    public Boolean isOcupado() {
        return ocupado;
    }
    public void setOcupado(Boolean ocupado) {
        this.ocupado = ocupado;
    }
    public Boolean isEstatus() {
        return estatus;
    }
    public void setEstatus(Boolean estatus) {
        this.estatus = estatus;
    }


    


    



}
