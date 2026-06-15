package uv.listi.parking_service.service;

import java.util.List;

import uv.listi.parking_service.dto.ParkingEntradaRequest;
import uv.listi.parking_service.dto.ParkingEntradaResponse;
import uv.listi.parking_service.dto.ParkingSalidaRequest;
import uv.listi.parking_service.dto.ParkingSalidaResponse;
import uv.listi.parking_service.model.Espacio;


public interface ParkingService {

    ParkingEntradaResponse registrarEntrada(ParkingEntradaRequest request, String token);
    ParkingSalidaResponse registrarSalida(ParkingSalidaRequest request, String token);
    public List<Espacio> consultarEspacios(String token);
    
}
