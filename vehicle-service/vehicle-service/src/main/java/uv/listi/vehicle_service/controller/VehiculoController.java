package uv.listi.vehicle_service.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import uv.listi.vehicle_service.model.VehiculoInfo;
import uv.listi.vehicle_service.service.VehiculoService;

@RestController
@RequestMapping("/api/vehiculos")
public class VehiculoController {

    private final VehiculoService vehiculoService;

    public VehiculoController(VehiculoService vehiculoService) {
        this.vehiculoService = vehiculoService;
    }

    @GetMapping("/test")
    public String test() {
        return "Vehicle Service funcionando";
    }

    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<List<VehiculoInfo>> obtenerVehiculos(
            @PathVariable Integer idUsuario) {

        return ResponseEntity.ok(
                vehiculoService.buscarPorUsuario(idUsuario)
        );
    }
}