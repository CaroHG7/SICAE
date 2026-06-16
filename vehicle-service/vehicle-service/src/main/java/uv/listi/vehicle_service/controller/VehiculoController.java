package uv.listi.vehicle_service.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import uv.listi.vehicle_service.model.Vehiculo;
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

    @PostMapping
    public ResponseEntity<?> registrarVehiculo(
            @RequestBody Vehiculo vehiculo) {

        String resultado = vehiculoService.registrar(vehiculo);

        if (!resultado.equals("Vehículo registrado correctamente")) {

            return ResponseEntity.badRequest().body(
                    Map.of(
                            "success", false,
                            "mensaje", resultado
                    )
            );
        }

        return ResponseEntity.ok(
                Map.of(
                        "success", true,
                        "mensaje", resultado
                )
        );
    }

    @PutMapping("/{idVehiculo}")
    public ResponseEntity<?> editarVehiculo(
            @PathVariable Integer idVehiculo,
            @RequestBody Vehiculo vehiculo) {

        vehiculo.setIdVehiculo(idVehiculo);

        String resultado = vehiculoService.editar(vehiculo);

        if (!resultado.equals("Vehículo actualizado correctamente")) {
            return ResponseEntity.badRequest().body(
                    Map.of(
                            "success", false,
                            "mensaje", resultado
                    )
            );
        }

        return ResponseEntity.ok(
                Map.of(
                        "success", true,
                        "mensaje", resultado
                )
        );
    }

    @PatchMapping("/{idVehiculo}/estatus")
    public ResponseEntity<?> cambiarEstatus(
            @PathVariable Integer idVehiculo,
            @RequestBody Vehiculo vehiculo) {

        vehiculo.setIdVehiculo(idVehiculo);

        String resultado = vehiculoService.cambiarEstatus(vehiculo);

        if (!resultado.equals("Estatus del vehículo actualizado correctamente")) {
            return ResponseEntity.badRequest().body(
                    Map.of(
                            "success", false,
                            "mensaje", resultado
                    )
            );
        }

        return ResponseEntity.ok(
                Map.of(
                        "success", true,
                        "mensaje", resultado
                )
        );
    }
}