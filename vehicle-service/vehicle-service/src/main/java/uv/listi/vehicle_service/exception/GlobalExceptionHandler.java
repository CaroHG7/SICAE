package uv.listi.vehicle_service.exception;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.server.ResponseStatusException;

import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

    /*
     * Captura errores dentro del JSON:
     * - Letras en campos Integer.
     * - Valores inválidos en campos Boolean.
     * - JSON incompleto o mal formado.
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, Object>> manejarJsonInvalido(
            HttpMessageNotReadableException exception,
            HttpServletRequest request) {

        Throwable causa = exception.getMostSpecificCause();

        String detalle = causa != null && causa.getMessage() != null
                ? causa.getMessage()
                : "";

        String campo = extraerCampo(detalle);
        String mensaje;

        if (esTipoEntero(detalle)) {

            if (campo != null) {
                mensaje = "El campo '" + campo
                        + "' debe contener un número entero.";
            } else {
                mensaje = "Uno de los campos debe contener un número entero.";
            }

        } else if (esTipoBooleano(detalle)) {

            if (campo != null) {
                mensaje = "El campo '" + campo
                        + "' debe contener únicamente true o false.";
            } else {
                mensaje = "Uno de los campos debe contener únicamente true o false.";
            }

        } else {
            mensaje = "El cuerpo JSON está mal formado o contiene un tipo de dato inválido.";
        }

        return construirRespuesta(
                HttpStatus.BAD_REQUEST,
                mensaje,
                request.getRequestURI()
        );
    }

    /*
     * Captura errores en variables de la URL.
     * como /api/vehiculos/abc
     * o cuando idVehiculo debe ser Integer.
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Map<String, Object>> manejarParametroInvalido(
            MethodArgumentTypeMismatchException exception,
            HttpServletRequest request) {

        String nombreParametro = exception.getName();
        Class<?> tipoRequerido = exception.getRequiredType();

        String mensaje;

        if (tipoRequerido == Integer.class || tipoRequerido == int.class) {
            mensaje = "El parámetro '" + nombreParametro
                    + "' debe contener un número entero.";
        } else if (tipoRequerido == Boolean.class
                || tipoRequerido == boolean.class) {
            mensaje = "El parámetro '" + nombreParametro
                    + "' debe contener únicamente true o false.";
        } else {
            mensaje = "El parámetro '" + nombreParametro
                    + "' contiene un valor inválido.";
        }

        return construirRespuesta(
                HttpStatus.BAD_REQUEST,
                mensaje,
                request.getRequestURI()
        );
    }

    /*
     * Muestra los mensajes de las reglas de negocio como:
     * - Placa duplicada.
     * - Máximo cuatro vehículos activos.
     * - Vehículo inexistente.
     * - Datos obligatorios faltantes.
     */
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Map<String, Object>> manejarReglaDeNegocio(
            ResponseStatusException exception,
            HttpServletRequest request) {

        String mensaje = exception.getReason();

        if (mensaje == null || mensaje.isBlank()) {
            mensaje = "La operación solicitada no pudo completarse.";
        }

        return construirRespuesta(
                exception.getStatusCode(),
                mensaje,
                request.getRequestURI()
        );
    }

    private ResponseEntity<Map<String, Object>> construirRespuesta(
            HttpStatusCode estado,
            String mensaje,
            String path) {

        Map<String, Object> respuesta = new LinkedHashMap<>();

        respuesta.put("timestamp", Instant.now().toString());
        respuesta.put("success", false);
        respuesta.put("status", estado.value());
        respuesta.put("error", obtenerNombreEstado(estado));
        respuesta.put("mensaje", mensaje);
        respuesta.put("path", path);

        return ResponseEntity
                .status(estado)
                .body(respuesta);
    }

    private String obtenerNombreEstado(HttpStatusCode estado) {
        try {
            return HttpStatus.valueOf(estado.value()).getReasonPhrase();
        } catch (IllegalArgumentException exception) {
            return "Error";
        }
    }

    private boolean esTipoEntero(String detalle) {
        String texto = detalle.toLowerCase();

        return texto.contains("integer")
                || texto.contains("java.lang.integer")
                || texto.contains("int from")
                || texto.contains("number from");
    }

    private boolean esTipoBooleano(String detalle) {
        String texto = detalle.toLowerCase();

        return texto.contains("boolean")
                || texto.contains("true or false");
    }

    /*
     * Intenta obtener el nombre del campo desde mensajes como:
     * Vehiculo["anio"]
     * Vehiculo["idUsuario"]
     */
    private String extraerCampo(String detalle) {

        Pattern patron = Pattern.compile("\\[\"([^\"]+)\"\\]");
        Matcher matcher = patron.matcher(detalle);

        String ultimoCampo = null;

        while (matcher.find()) {
            ultimoCampo = matcher.group(1);
        }

        return ultimoCampo;
    }
}