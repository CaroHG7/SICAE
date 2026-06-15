package uv.listi.parking_service.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Data;


public class ParkingEntradaResponse {


    private Integer idMovimiento;
    private Integer idEspacio;
    private LocalDateTime entrada;
    private BigDecimal tarifa;
    
    private String mensaje;

    public ParkingEntradaResponse(Integer idMovimiento, Integer idEspacio, LocalDateTime entrada, BigDecimal tarifa,
            String mensaje) {
        this.idMovimiento = idMovimiento;
        this.idEspacio = idEspacio;
        this.entrada = entrada;
        this.tarifa = tarifa;
        this.mensaje = mensaje;
    }

    public Integer getIdMovimiento() {
        return idMovimiento;
    }

    public void setIdMovimiento(Integer idMovimiento) {
        this.idMovimiento = idMovimiento;
    }

    public Integer getIdEspacio() {
        return idEspacio;
    }

    public void setIdEspacio(Integer idEspacio) {
        this.idEspacio = idEspacio;
    }

    public LocalDateTime getEntrada() {
        return entrada;
    }

    public void setEntrada(LocalDateTime entrada) {
        this.entrada = entrada;
    }

    public BigDecimal getTarifa() {
        return tarifa;
    }

    public void setTarifa(BigDecimal tarifa) {
        this.tarifa = tarifa;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    } 

    
    
    
    

    
}
