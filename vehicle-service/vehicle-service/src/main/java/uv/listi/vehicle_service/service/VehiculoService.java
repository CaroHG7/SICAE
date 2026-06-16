package uv.listi.vehicle_service.service;

import java.util.List;

import org.springframework.stereotype.Service;

import uv.listi.vehicle_service.model.Vehiculo;
import uv.listi.vehicle_service.model.VehiculoInfo;
import uv.listi.vehicle_service.repository.VehiculoRepository;

@Service
public class VehiculoService {

    private final VehiculoRepository vehiculoRepository;

    public VehiculoService(VehiculoRepository vehiculoRepository) {
        this.vehiculoRepository = vehiculoRepository;
    }

    public List<VehiculoInfo> buscarPorUsuario(Integer idUsuario) {
        return vehiculoRepository.buscarPorUsuario(idUsuario);
    }

    public String registrar(Vehiculo vehiculo) {

        if (vehiculo.getIdUsuario() == null || vehiculo.getIdUsuario() <= 0) {
            return "El idUsuario es obligatorio";
        }

        if (vehiculo.getIdModelo() == null || vehiculo.getIdModelo() <= 0) {
            return "El idModelo es obligatorio";
        }

        if (vehiculo.getPlaca() == null || vehiculo.getPlaca().isBlank()) {
            return "La placa es obligatoria";
        }

        if (vehiculo.getColor() == null || vehiculo.getColor().isBlank()) {
            return "El color es obligatorio";
        }

        if (vehiculo.getAnio() == null || vehiculo.getAnio() <= 0) {
            return "El año es obligatorio";
        }

        if (vehiculoRepository.existePlaca(vehiculo.getPlaca()) > 0) {
            return "Ya existe un vehículo registrado con esa placa";
        }

        if (vehiculoRepository.contarActivos(vehiculo.getIdUsuario()) >= 4) {
            return "El usuario ya tiene 4 vehículos activos";
        }

        Integer siguienteId = vehiculoRepository.ultimoIdVehiculo() + 1;
        String clave = String.format("VEH-%03d", siguienteId);

        vehiculo.setClaveVehiculo(clave);
        vehiculo.setEstatus(true);

        vehiculoRepository.guardar(vehiculo);

        return "Vehículo registrado correctamente";
    }

    public String editar(Vehiculo vehiculo) {

        if (vehiculo.getIdVehiculo() == null || vehiculo.getIdVehiculo() <= 0) {
            return "El idVehiculo es obligatorio";
        }

        if (vehiculo.getIdUsuario() == null || vehiculo.getIdUsuario() <= 0) {
            return "El idUsuario es obligatorio";
        }

        if (vehiculo.getIdModelo() == null || vehiculo.getIdModelo() <= 0) {
            return "El idModelo es obligatorio";
        }

        if (vehiculo.getPlaca() == null || vehiculo.getPlaca().isBlank()) {
            return "La placa es obligatoria";
        }

        if (vehiculo.getColor() == null || vehiculo.getColor().isBlank()) {
            return "El color es obligatorio";
        }

        if (vehiculo.getAnio() == null || vehiculo.getAnio() <= 0) {
            return "El año es obligatorio";
        }

        if (vehiculoRepository.existeVehiculo(vehiculo.getIdVehiculo()) == 0) {
            return "El vehículo no existe";
        }

        if (vehiculoRepository.existePlacaEnOtroVehiculo(
                vehiculo.getPlaca(),
                vehiculo.getIdVehiculo()) > 0) {
            return "Ya existe otro vehículo registrado con esa placa";
        }

        Integer filasActualizadas = vehiculoRepository.actualizar(vehiculo);

        if (filasActualizadas == 0) {
            return "El vehículo no pertenece al usuario indicado";
        }

        return "Vehículo actualizado correctamente";
    }

    public String cambiarEstatus(Vehiculo vehiculo) {

        if (vehiculo.getIdVehiculo() == null || vehiculo.getIdVehiculo() <= 0) {
            return "El idVehiculo es obligatorio";
        }

        if (vehiculo.getIdUsuario() == null || vehiculo.getIdUsuario() <= 0) {
            return "El idUsuario es obligatorio";
        }

        if (vehiculo.getEstatus() == null) {
            return "El estatus es obligatorio";
        }

        if (vehiculoRepository.existeVehiculo(vehiculo.getIdVehiculo()) == 0) {
            return "El vehículo no existe";
        }

        Integer filasActualizadas = vehiculoRepository.cambiarEstatus(vehiculo);

        if (filasActualizadas == 0) {
            return "El vehículo no pertenece al usuario indicado";
        }

        return "Estatus del vehículo actualizado correctamente";
    }
}