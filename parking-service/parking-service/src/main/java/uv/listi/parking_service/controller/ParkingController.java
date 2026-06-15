package uv.listi.parking_service.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import uv.listi.parking_service.dto.ParkingEntradaRequest;
import uv.listi.parking_service.dto.ParkingEntradaResponse;
import uv.listi.parking_service.dto.ParkingSalidaRequest;
import uv.listi.parking_service.dto.ParkingSalidaResponse;
import uv.listi.parking_service.model.Espacio;
import uv.listi.parking_service.repository.EspacioRepository;
import uv.listi.parking_service.repository.MovimientoRepository;
import uv.listi.parking_service.service.ParkingService;

@RestController
@RequestMapping("/api/parking")
public class ParkingController {

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private EspacioRepository espacioRepository;
    @Autowired
    private MovimientoRepository movimientoRepository;

    private ParkingService ParkingService;

    public ParkingController(ParkingService parkingService) {
        this.ParkingService = parkingService;
        
    }

    @GetMapping("/espacios")
    public List<Espacio> consultarEspacios(@RequestHeader("Authorizacion") String token){
        return ParkingService.consultarEspacios(token);
    }


    @PostMapping("/entradaParking")
    public ParkingEntradaResponse registrarEntrada(
            @RequestBody ParkingEntradaRequest request,
            @RequestHeader("Authorization") String token) {
        
        return ParkingService.registrarEntrada(request, token);
    }

    @PostMapping("/salidaParking")
    public ParkingSalidaResponse registrarSalida(
            @RequestBody ParkingSalidaRequest request,
            @RequestHeader("Authorization") String token) {
        
        return ParkingService.registrarSalida(request, token);
    }


    
    
}
