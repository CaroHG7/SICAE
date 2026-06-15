package uv.listi.parking_service.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Data;


@Data
public class Movimiento {

    private Integer idMovimiento;
    private String claveUsuario;
    private String placa;
    private Integer idEspacio;
    private LocalDateTime entrada;
    private LocalDateTime salida;
    private BigDecimal tarifa;
    private Integer minEstacionado;
    private Integer horasCobradas;
    private BigDecimal costoTotal;
    private LocalDateTime tiempoActualizacion;



    
    public Movimiento() {
    }
    
    public Movimiento(Integer idMovimiento, String claveUsuario, String placa, Integer idEspacio, LocalDateTime entrada,
            LocalDateTime salida, BigDecimal tarifa, Integer minEstacionado, Integer horasCobradas,
            BigDecimal costoTotal, LocalDateTime tiempoActualizacion) {
        this.idMovimiento = idMovimiento;
        this.claveUsuario = claveUsuario;
        this.placa = placa;
        this.idEspacio = idEspacio;
        this.entrada = entrada;
        this.salida = salida;
        this.tarifa = tarifa;
        this.minEstacionado = minEstacionado;
        this.horasCobradas = horasCobradas;
        this.costoTotal = costoTotal;
        this.tiempoActualizacion = tiempoActualizacion;
    }
    public Integer getIdMovimiento() {
        return idMovimiento;
    }
    public void setIdMovimiento(Integer idMovimiento) {
        this.idMovimiento = idMovimiento;
    }
    public String getClaveUsuario() {
        return claveUsuario;
    }
    public void setClaveUsuario(String claveUsuario) {
        this.claveUsuario = claveUsuario;
    }
    public String getPlaca() {
        return placa;
    }
    public void setPlaca(String placa) {
        this.placa = placa;
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
    public LocalDateTime getSalida() {
        return salida;
    }
    public void setSalida(LocalDateTime salida) {
        this.salida = salida;
    }
    public BigDecimal getTarifa() {
        return tarifa;
    }
    public void setTarifa(BigDecimal tarifa) {
        this.tarifa = tarifa;
    }
    public Integer getMinEstacionado() {
        return minEstacionado;
    }
    public void setMinEstacionado(Integer minEstacionado) {
        this.minEstacionado = minEstacionado;
    }
    public Integer getHorasCobradas() {
        return horasCobradas;
    }
    public void setHorasCobradas(Integer horasCobradas) {
        this.horasCobradas = horasCobradas;
    }
    public BigDecimal getCostoTotal() {
        return costoTotal;
    }
    public void setCostoTotal(BigDecimal costoTotal) {
        this.costoTotal = costoTotal;
    }
    public LocalDateTime getTiempoActualizacion() {
        return tiempoActualizacion;
    }
    public void setTiempoActualizacion(LocalDateTime tiempoActualizacion) {
        this.tiempoActualizacion = tiempoActualizacion;
    }


    


    
}
