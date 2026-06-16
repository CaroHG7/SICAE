package uv.listi.parking_service.client;

import org.springframework.stereotype.Component;

@Component
public class VehicleClient {

    public String obtenerVehiculosUsuario() {
        return "1,2,3";
    }
}