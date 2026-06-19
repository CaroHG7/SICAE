package uv.listi.auth_service.dto;

public class TokenValidationResponse {

    private boolean valid;
    private String mensaje;
    private String usuario;
    private Integer idRol;

    public TokenValidationResponse() {
    }
    
    public TokenValidationResponse(boolean valid, String mensaje, String usuario, Integer idRol) {
        this.valid = valid;
        this.mensaje = mensaje;
        this.usuario = usuario;
        this.idRol = idRol;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }
    
    public Integer getIdRol() { 
        return idRol; 
    }
    
    public void setIdRol(Integer idRol) { 
        this.idRol = idRol; 
    }
}
