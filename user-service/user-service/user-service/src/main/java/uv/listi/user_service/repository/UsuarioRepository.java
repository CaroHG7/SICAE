
package uv.listi.user_service.repository;


import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import uv.listi.user_service.model.Usuario;

@Mapper
public interface UsuarioRepository {

    @Select("SELECT COUNT(*) FROM usuarios WHERE correo = #{correo}")
    int existeCorreo(String correo);

    @Select("SELECT COUNT(*) FROM usuarios WHERE usuario = #{usuario}")
    int existeUsuario(String usuario);

    @Select("SELECT COUNT(*) FROM usuarios WHERE clave_usuario = #{claveUsuario}")
    int existeClaveUsuario(String claveUsuario);

    @Insert("""
        INSERT INTO usuarios (
            id_rol,
            id_tipo_usuario,
            id_programa_educativo,
            nombre,
            apellido_paterno,
            usuario,
            password,
            correo,
            telefono,
            clave_usuario,
            estatus,
            tiempo_creacion
        ) VALUES (
            #{idRol},
            #{idTipoUsuario},
            #{idProgramaEducativo},
            #{nombre},
            #{apellidoPaterno},
            #{usuario},
            #{password},
            #{correo},
            #{telefono},
            #{claveUsuario},
            #{estatus},
            #{tiempoCreacion}
        )
        """)
    int registrar(Usuario usuario);
}
