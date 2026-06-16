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
        INSERT INTO movimiento (
            idVehiculo,
            tiempoEntrada,
            tiempoCreacion,
            tarifaHora,
            idEspacio
        )
        VALUES (
            #{idVehiculo},
            #{entrada},
            #{tiempoCreacion},
            #{tarifa},
            #{idEspacio}
        )
        """)
    @Options(
        useGeneratedKeys = true,
        keyProperty = "idMovimiento",
        keyColumn = "idMovimiento"
    )
    int crearMovimiento(Movimiento movimiento);

    @Update("""
        UPDATE movimiento
        SET
            tiempoSalida = #{salida},
            tiempoActualizacion = #{tiempoActualizacion},
            minutosEstacionado = #{minEstacionado},
            horasCobradas = #{horasCobradas},
            costoTotal = #{costoTotal}
        WHERE idMovimiento = #{idMovimiento}
        """)
    int actualizarMovimiento(Movimiento movimiento);

    @Select("""
        SELECT COUNT(*)
        FROM movimiento
        WHERE tiempoSalida IS NULL
          AND idVehiculo IN (${vehiculosUsuario})
        """)
    int movimientosUsuario(
        @Param("vehiculosUsuario") String vehiculosUsuario
    );

    @Select("""
        SELECT
            idMovimiento,
            idVehiculo,
            tiempoEntrada AS entrada,
            tiempoSalida AS salida,
            minutosEstacionado AS minEstacionado,
            horasCobradas,
            costoTotal,
            tarifaHora AS tarifa,
            tiempoCreacion,
            tiempoActualizacion,
            idEspacio
        FROM movimiento
        WHERE idMovimiento = #{idMovimiento}
        """)
    Optional<Movimiento> obtenerMovimientoPorId(
        @Param("idMovimiento") Integer idMovimiento
    );

    @Select("""
        SELECT
            idMovimiento,
            idVehiculo,
            tiempoEntrada AS entrada,
            tiempoSalida AS salida,
            minutosEstacionado AS minEstacionado,
            horasCobradas,
            costoTotal,
            tarifaHora AS tarifa,
            tiempoCreacion,
            tiempoActualizacion,
            idEspacio
        FROM movimiento
        WHERE idVehiculo = #{idVehiculo}
          AND tiempoSalida IS NULL
        ORDER BY tiempoEntrada DESC
        LIMIT 1
        """)
    Movimiento movimientosActivos(
        @Param("idVehiculo") Integer idVehiculo
    );
}