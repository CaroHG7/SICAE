package uv.listi.auth_service.dto;

public class TokenValidationResponse {

    private boolean valid;
    private String mensaje;
    private String usuario;

    public TokenValidationResponse() {
    }

    public TokenValidationResponse(boolean valid, String mensaje, String usuario) {
        this.valid = valid;
        this.mensaje = mensaje;
        this.usuario = usuario;
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
}
