package uv.listi.parking_service.repository;

import java.util.Optional;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import uv.listi.parking_service.model.Movimiento;

@Mapper
public interface MovimientoRepository {
    

    @Insert("""
            insert into movimiento(idVehiculo, tiempoEntrada, tiempoCreacion, tarifaHora, idEspacio)
            values(#{idVehiculo}, #{tiempoEntrada}, #{tiempoCreacion}, #{tarifa}, #{idEspacio})
            """)
            @Options(useGeneratedKeys = true, keyProperty = "idMovimiento", keyColumn = "idMovimiento")
            int crearMovimiento(Movimiento movimiento);


    @Update("""
            update movimiento set tiempoSalida = #{tiempoSalida}, tiempoActualizacion = #{tiempoActualizacion}, minutosEstacionado = #{minutosEstacionado}, horasCobradas = #{horasCobradas}, costoTotal = #{costoTotal}
            where idMovimiento = #{idMovimiento}
            """)
            int actualizarMovimiento(Movimiento movimiento);


    @Select("""
            select count(*) from movimiento where costoTotal is null and idVehiculo in (${vehiculosUsuario})
            """)
            int movimientosUsuario(@Param("vehiculosUsuario") String vehiculosUsuario);
    

    @Select("""
            select idMovimiento, tiempoEntrada, tiempoSalida, idEspacio, tarifaHora, costoTotal, horasCobradas from movimiento
            where idMovimiento = #{idMovimiento}
            """)
            Optional<Movimiento> obtenerMovimientoPorId(@Param("idMovimiento") Integer idMovimiento);


    @Select("""
            select * from movimiento
            where idVehiculo = #{idVehiculo} and costoTotal is null
        """)
        Movimiento movimientosActivos(@Param("idVehiculo") Integer idVehiculo);


    
    
}
