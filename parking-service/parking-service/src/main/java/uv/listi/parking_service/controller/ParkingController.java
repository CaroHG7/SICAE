package uv.listi.parking_service.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

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
        public List<Espacio> consultarEspacios(){
            return ParkingService.consultarEspacios();
        }


    
    
}
