package uv.listi.vehicle_service.repository;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import uv.listi.vehicle_service.model.Vehiculo;
import uv.listi.vehicle_service.model.VehiculoInfo;

@Mapper
public interface VehiculoRepository {

    @Select("""
    SELECT
    idVehiculo,
    idUsuario,
    claveVehiculo,
    idMarca,
    marca,
    idModelo,
    modelo,
    placa,
    color,
    anio,
    descripcion,
    estatus
    FROM vehiculofullinfo
    WHERE idUsuario = #{idUsuario}
    ORDER BY idVehiculo
    """)
    List<VehiculoInfo> buscarPorUsuario(Integer idUsuario);
    
    @Select("""
    SELECT COUNT(*)
    FROM vehiculo
    WHERE placa = #{placa}
    """)
    Integer existePlaca(String placa);

    @Select("""
    SELECT COUNT(*)
    FROM vehiculo
    WHERE idUsuario = #{idUsuario}
    AND estatus = b'1'
    """)
    Integer contarActivos(Integer idUsuario);

    @Select("""
    SELECT COALESCE(MAX(idVehiculo),0)
    FROM vehiculo
    """)
    Integer ultimoIdVehiculo();

    @Insert("""
    INSERT INTO vehiculo(
    idUsuario,
    claveVehiculo,
    idModelo,
    placa,
    color,
    anio,
    descripcion,
    estatus
    )
    VALUES(
    #{idUsuario},
    #{claveVehiculo},
    #{idModelo},
    #{placa},
    #{color},
    #{anio},
    #{descripcion},
    #{estatus}
    )
    """)
    Integer guardar(Vehiculo vehiculo);

    @Select("""
    SELECT COUNT(*)
    FROM vehiculo
    WHERE idVehiculo = #{idVehiculo}
    """)
    Integer existeVehiculo(Integer idVehiculo);

    @Select("""
    SELECT COUNT(*)
    FROM vehiculo
    WHERE placa = #{placa}
    AND idVehiculo <> #{idVehiculo}
    """)
    Integer existePlacaEnOtroVehiculo(String placa, Integer idVehiculo);

    @Update("""
    UPDATE vehiculo
    SET
    idModelo = #{idModelo},
    placa = #{placa},
    color = #{color},
    anio = #{anio},
    descripcion = #{descripcion}
    WHERE idVehiculo = #{idVehiculo}
    AND idUsuario = #{idUsuario}
    """)
    Integer actualizar(Vehiculo vehiculo);

    @Update("""
    UPDATE vehiculo
    SET estatus = #{estatus}
    WHERE idVehiculo = #{idVehiculo}
    AND idUsuario = #{idUsuario}
    """)
    Integer cambiarEstatus(Vehiculo vehiculo);
}