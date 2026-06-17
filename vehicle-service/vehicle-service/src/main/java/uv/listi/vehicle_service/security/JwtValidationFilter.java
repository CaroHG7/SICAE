package uv.listi.vehicle_service.security;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtValidationFilter extends OncePerRequestFilter {

    private final AuthServiceClient authServiceClient;

    public JwtValidationFilter(AuthServiceClient authServiceClient) {
        this.authServiceClient = authServiceClient;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getRequestURI();

        // Endpoint público para comprobar que VehicleService está activo.
        if (path.equals("/api/vehiculos/test")) {
            filterChain.doFilter(request, response);
            return;
        }

        String authHeader = request.getHeader("Authorization");

        // Caso 1: no se envió la cabecera Authorization.
        if (authHeader == null || authHeader.isBlank()) {
            responderError(
                    request,
                    response,
                    HttpServletResponse.SC_UNAUTHORIZED,
                    "No se proporcionó el token JWT en la cabecera Authorization."
            );
            return;
        }

        // Caso 2: la cabecera no utiliza el formato Bearer TOKEN.
        if (!authHeader.regionMatches(true, 0, "Bearer ", 0, 7)) {
            responderError(
                    request,
                    response,
                    HttpServletResponse.SC_UNAUTHORIZED,
                    "El formato de autorización es incorrecto. Se esperaba: Bearer TOKEN."
            );
            return;
        }

        // Se obtiene únicamente el contenido del token.
        String token = authHeader.substring(7).trim();

        // Caso 3: se escribió Bearer, pero no se proporcionó un token.
        if (token.isEmpty()) {
            responderError(
                    request,
                    response,
                    HttpServletResponse.SC_UNAUTHORIZED,
                    "El token JWT está vacío."
            );
            return;
        }

        // Se reconstruye la cabecera para enviarla a AuthService.
        String bearerToken = "Bearer " + token;

        boolean valido = authServiceClient.validarToken(bearerToken);

        // Caso 4: token falso, alterado o expirado.
        if (!valido) {
            responderError(
                    request,
                    response,
                    HttpServletResponse.SC_UNAUTHORIZED,
                    "El token JWT es inválido o ha expirado."
            );
            return;
        }

        // El token es válido y la petición continúa.
        filterChain.doFilter(request, response);
    }

    private void responderError(
            HttpServletRequest request,
            HttpServletResponse response,
            int codigo,
            String mensaje) throws IOException {

        response.setStatus(codigo);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");

        String error = HttpStatus.valueOf(codigo).getReasonPhrase();
        String path = request.getRequestURI();

        String json = """
                {
                  "success": false,
                  "status": %d,
                  "error": "%s",
                  "mensaje": "%s",
                  "path": "%s"
                }
                """.formatted(
                codigo,
                escaparJson(error),
                escaparJson(mensaje),
                escaparJson(path)
        );

        response.getWriter().write(json);
        response.getWriter().flush();
    }

    private String escaparJson(String texto) {
        if (texto == null) {
            return "";
        }

        return texto
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r");
    }
}