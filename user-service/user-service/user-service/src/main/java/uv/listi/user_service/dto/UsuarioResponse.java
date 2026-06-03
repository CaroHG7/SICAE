
package uv.listi.user_service.dto;

public class UsuarioResponse {
    private boolean success;
    private String mensaje;

    public UsuarioResponse() {
    }

    public UsuarioResponse(boolean success, String mensaje) {
        this.success = success;
        this.mensaje = mensaje;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
}
