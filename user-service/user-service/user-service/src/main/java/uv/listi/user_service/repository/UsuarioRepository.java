package uv.listi.user_service.repository;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import uv.listi.user_service.model.Usuario;
import uv.listi.user_service.dto.UsuarioPerfilResponse;

@Mapper
public interface UsuarioRepository {

    @Select("""
        SELECT COUNT(*) 
        FROM usuario 
        WHERE email = #{email}
        """)
    int existeEmail(String email);

    @Select("""
        SELECT COUNT(*) 
        FROM usuario 
        WHERE username = #{username}
        """)
    int existeUsername(String username);

    @Select("""
        SELECT COUNT(*) 
        FROM usuario 
        WHERE "claveUsuario" = #{claveUsuario}
        """)
    int existeClaveUsuario(String claveUsuario);

    @Insert("""
        INSERT INTO usuario (
            nombre,
            "apellidoPaterno",
            "apellidoMaterno",
            "claveUsuario",
            email,
            telefono,
            username,
            password,
            estatus,
            "idRol",
            "idTipoUsuario",
            "idProgramaEducativo",
            "tiempoCreacion",
            "tempoActualizacion"
        ) VALUES (
            #{nombre},
            #{apellidoPaterno},
            #{apellidoMaterno},
            #{claveUsuario},
            #{email},
            #{telefono},
            #{username},
            #{password},
            B'1',
            #{idRol},
            #{idTipoUsuario},
            #{idProgramaEducativo},
            #{tiempoCreacion},
            NULL
        )
        """)
    int registrar(Usuario usuario);

    @Select("""
        SELECT 
            "idUsuario" AS "idUsuario",
            rol AS "rol",
            CONCAT(nombre, ' ', "apellidoPaterno", ' ', COALESCE("apellidoMaterno", '')) AS "nombreCompleto",
            "tipoUsuario" AS "tipoUsuario",
            "programaEducativo" AS "programaEducativo",
            username AS "username",
            email AS "email",
            telefono AS "telefono",
            estatus::text AS "estatus",
            "claveUsuario" AS "claveUsuario",
            "tiempoCreacion" AS "tiempoCreacion",
            "tempoActualizacion" AS "tempoActualizacion"
        FROM "usuarioFullInfo"
        WHERE "idUsuario" = #{idUsuario}
        """)
    UsuarioPerfilResponse obtenerPerfilPorId(Integer idUsuario);
}