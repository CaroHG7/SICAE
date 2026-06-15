package uv.listi.parking_service.service;

import java.util.List;

import org.springframework.stereotype.Service;

import uv.listi.parking_service.model.Espacio;
import uv.listi.parking_service.repository.EspacioRepository;
import uv.listi.parking_service.repository.MovimientoRepository;

@Service
public class ParkingService {
    private final EspacioRepository espacioRepository;
    private final MovimientoRepository movimientoRepository;


    public ParkingService(EspacioRepository espacioRepository, MovimientoRepository movimientoRepository) {
        this.espacioRepository = espacioRepository;
        this.movimientoRepository = movimientoRepository;
    }

    public List<Espacio> consultarEspacios(){
        return espacioRepository.buscarTodos();
    }
    
}
