package uv.listi.parking_service.repository;

import java.util.List;
import java.util.Optional;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import uv.listi.parking_service.model.Espacio;

@Mapper
public interface EspacioRepository {

    @Select("""
            select * from espacioestacionamiento where idEspacio = #{idEspacio} 
            """)
            Espacio buscarPorID(Integer idEspacio);

    @Select("""
            SELECT * FROM espacioestacionamiento WHERE idEspacio = #{idEspacio}
            """)
            Optional<Espacio> buscarPorId(Integer idEspacio);

    @Select("""
            select * from espacioestacionamiento where estatus = 1
            """) 
            List<Espacio> buscarDisponibles();


    @Select("""
            select * from espacioestacionamiento
            """)
            List<Espacio> buscarTodos();

    @Update("""
            update espacioestacionamiento set ocupado = #{ocupado} where idEspacio = #{idEspacio}
            """)
            int actualizarDisponibilidad(@Param("idEspacio") Integer idEspacio, @Param("ocupado") Boolean ocupado);

    
}
