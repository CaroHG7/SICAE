package uv.listi.vehicle_service.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

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

}