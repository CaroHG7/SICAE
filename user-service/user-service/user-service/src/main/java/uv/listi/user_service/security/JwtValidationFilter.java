
package uv.listi.user_service.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import uv.listi.user_service.dto.TokenValidationResponse;
import java.io.IOException;

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

        if (path.equals("/usuarios/test")) {
            filterChain.doFilter(request, response);
            return;
        }

        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {

            response.sendError(HttpServletResponse.SC_UNAUTHORIZED,
                    "Token requerido"
            );
            return;
        }

        TokenValidationResponse tokenInfo = authServiceClient.validarToken(authHeader);
        
        if (tokenInfo == null || !tokenInfo.isValid()) {
            response.sendError(
                    HttpServletResponse.SC_UNAUTHORIZED,
                    "Token inválido"
            );
            return;
        }

        // guardamos la información del usuario y su rol en los atributos 
        request.setAttribute("usuarioAuth", tokenInfo.getUsuario());
        request.setAttribute("idRolAuth", tokenInfo.getIdRol());
        
        filterChain.doFilter(request, response);
    }
    
}