package uv.listi.auth_service.repository;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import uv.listi.auth_service.model.UsuarioAuth;

@Mapper
public interface AuthRepository {

    @Select("""
        SELECT "idUsuario", nombre, "apellidoPaterno", "apellidoMaterno", username, password, estatus, "idRol", rol, "idTipoUsuario", "tipoUsuario"
        FROM "usuarioFullInfo"
        WHERE username = #{usuario}
        """)
    UsuarioAuth buscarPorUsuario(String usuario);
}
