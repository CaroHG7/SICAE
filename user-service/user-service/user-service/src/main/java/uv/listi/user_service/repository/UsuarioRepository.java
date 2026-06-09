package uv.listi.user_service.repository;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import uv.listi.user_service.model.Usuario;
import uv.listi.user_service.dto.UsuarioPerfilResponse;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

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
    
    @Select("""
        SELECT COUNT(*) 
        FROM usuario 
        WHERE "idUsuario" = #{idUsuario}
        """)
    int existeUsuarioPorId(Integer idUsuario);

    @Select("""
        SELECT COUNT(*) 
        FROM usuario 
        WHERE email = #{email}
        AND "idUsuario" <> #{idUsuario}
        """)
    int existeEmailEnOtroUsuario(@Param("email") String email, @Param("idUsuario") Integer idUsuario);

    @Update("""
        UPDATE usuario
        SET
            nombre = #{nombre},
            "apellidoPaterno" = #{apellidoPaterno},
            "apellidoMaterno" = #{apellidoMaterno},
            email = #{email},
            telefono = #{telefono},
            "idRol" = #{idRol},
            "idTipoUsuario" = #{idTipoUsuario},
            "idProgramaEducativo" = #{idProgramaEducativo},
            "tempoActualizacion" = CURRENT_TIMESTAMP
        WHERE "idUsuario" = #{idUsuario}
        """)
    int editarUsuario(
            @Param("idUsuario") Integer idUsuario,
            @Param("nombre") String nombre,
            @Param("apellidoPaterno") String apellidoPaterno,
            @Param("apellidoMaterno") String apellidoMaterno,
            @Param("email") String email,
            @Param("telefono") String telefono,
            @Param("idRol") Integer idRol,
            @Param("idTipoUsuario") Integer idTipoUsuario,
            @Param("idProgramaEducativo") Integer idProgramaEducativo
    );

    @Update("""
        UPDATE usuario
        SET 
            estatus = CASE 
                WHEN #{estatus} = true THEN B'1'
                ELSE B'0'
            END,
            "tempoActualizacion" = CURRENT_TIMESTAMP
        WHERE "idUsuario" = #{idUsuario}
        """)
    int cambiarEstatus(
            @Param("idUsuario") Integer idUsuario,
            @Param("estatus") Boolean estatus
    );
}