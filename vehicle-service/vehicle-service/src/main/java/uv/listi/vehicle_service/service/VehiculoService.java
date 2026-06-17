package uv.listi.vehicle_service.service;

import java.time.Year;
import java.util.List;
import java.util.Locale;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import uv.listi.vehicle_service.model.Vehiculo;
import uv.listi.vehicle_service.model.VehiculoInfo;
import uv.listi.vehicle_service.repository.VehiculoRepository;

@Service
public class VehiculoService {

    private static final int LONGITUD_MINIMA_PLACA = 5;
    private static final int LONGITUD_MAXIMA_PLACA = 7;
    private static final int LONGITUD_MAXIMA_COLOR = 20;
    private static final int LONGITUD_MAXIMA_DESCRIPCION = 255;
    private static final int ANIO_MINIMO = 1900;

    private final VehiculoRepository vehiculoRepository;

    public VehiculoService(VehiculoRepository vehiculoRepository) {
        this.vehiculoRepository = vehiculoRepository;
    }

    public List<VehiculoInfo> buscarPorUsuario(Integer idUsuario) {

        if (idUsuario == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "El idUsuario es obligatorio."
            );
        }

        if (idUsuario <= 0) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "El idUsuario debe ser un número mayor que cero."
            );
        }

        return vehiculoRepository.buscarPorUsuario(idUsuario);
    }

    public String registrar(Vehiculo vehiculo) {

        normalizarDatos(vehiculo);

        String validacion = validarDatosGenerales(vehiculo);

        if (validacion != null) {
            return validacion;
        }

        if (vehiculoRepository.existePlaca(vehiculo.getPlaca()) > 0) {
            return "Ya existe un vehículo registrado con la placa proporcionada.";
        }

        if (vehiculoRepository.contarActivos(vehiculo.getIdUsuario()) >= 4) {
            return "El usuario ya tiene cuatro vehículos activos. "
                    + "Debe desactivar uno antes de registrar otro.";
        }

        Integer siguienteId = vehiculoRepository.ultimoIdVehiculo() + 1;
        String clave = String.format("VEH-%03d", siguienteId);

        vehiculo.setClaveVehiculo(clave);
        vehiculo.setEstatus(true);

        Integer filasInsertadas = vehiculoRepository.guardar(vehiculo);

        if (filasInsertadas == null || filasInsertadas == 0) {
            return "No fue posible registrar el vehículo en la base de datos.";
        }

        return "Vehículo registrado correctamente";
    }

    public String editar(Vehiculo vehiculo) {

        if (vehiculo.getIdVehiculo() == null) {
            return "El idVehiculo es obligatorio.";
        }

        if (vehiculo.getIdVehiculo() <= 0) {
            return "El idVehiculo debe ser un número mayor que cero.";
        }

        normalizarDatos(vehiculo);

        String validacion = validarDatosGenerales(vehiculo);

        if (validacion != null) {
            return validacion;
        }

        if (vehiculoRepository.existeVehiculo(
                vehiculo.getIdVehiculo()) == 0) {

            return "El vehículo no existe.";
        }

        if (vehiculoRepository.existePlacaEnOtroVehiculo(
                vehiculo.getPlaca(),
                vehiculo.getIdVehiculo()) > 0) {

            return "Ya existe otro vehículo registrado con la placa proporcionada.";
        }

        Integer filasActualizadas =
                vehiculoRepository.actualizar(vehiculo);

        if (filasActualizadas == null || filasActualizadas == 0) {
            return "El vehículo no pertenece al usuario indicado.";
        }

        return "Vehículo actualizado correctamente";
    }

    public String cambiarEstatus(Vehiculo vehiculo) {

        if (vehiculo.getIdVehiculo() == null) {
            return "El idVehiculo es obligatorio.";
        }

        if (vehiculo.getIdVehiculo() <= 0) {
            return "El idVehiculo debe ser un número mayor que cero.";
        }

        if (vehiculo.getIdUsuario() == null) {
            return "El idUsuario es obligatorio.";
        }

        if (vehiculo.getIdUsuario() <= 0) {
            return "El idUsuario debe ser un número mayor que cero.";
        }

        if (vehiculo.getEstatus() == null) {
            return "El estatus es obligatorio y debe contener true o false.";
        }

        if (vehiculoRepository.existeVehiculo(
                vehiculo.getIdVehiculo()) == 0) {

            return "El vehículo no existe.";
        }

        /*
         * Evita activar otro vehículo cuando el usuario ya tiene cuatro activos.
         */
        if (Boolean.TRUE.equals(vehiculo.getEstatus())
                && vehiculoRepository.contarActivos(
                        vehiculo.getIdUsuario()) >= 4) {

            return "El vehículo no puede activarse porque el usuario "
                    + "ya tiene cuatro vehículos activos.";
        }

        Integer filasActualizadas =
                vehiculoRepository.cambiarEstatus(vehiculo);

        if (filasActualizadas == null || filasActualizadas == 0) {
            return "El vehículo no pertenece al usuario indicado.";
        }

        return "Estatus del vehículo actualizado correctamente";
    }

    private String validarDatosGenerales(Vehiculo vehiculo) {

        if (vehiculo == null) {
            return "El cuerpo de la petición es obligatorio.";
        }

        if (vehiculo.getIdUsuario() == null) {
            return "El idUsuario es obligatorio.";
        }

        if (vehiculo.getIdUsuario() <= 0) {
            return "El idUsuario debe ser un número mayor que cero.";
        }

        if (vehiculo.getIdModelo() == null) {
            return "El idModelo es obligatorio.";
        }

        if (vehiculo.getIdModelo() <= 0) {
            return "El idModelo debe ser un número mayor que cero.";
        }

        if (vehiculo.getPlaca() == null
                || vehiculo.getPlaca().isBlank()) {

            return "La placa es obligatoria.";
        }

        if (vehiculo.getPlaca().length() < LONGITUD_MINIMA_PLACA
                || vehiculo.getPlaca().length() > LONGITUD_MAXIMA_PLACA) {

            return "La placa debe contener entre "
                    + LONGITUD_MINIMA_PLACA
                    + " y "
                    + LONGITUD_MAXIMA_PLACA
                    + " caracteres.";
        }

        if (!vehiculo.getPlaca().matches("[A-Z0-9-]+")) {
            return "La placa solo puede contener letras, números y guiones.";
        }

        if (vehiculo.getColor() == null
                || vehiculo.getColor().isBlank()) {

            return "El color es obligatorio.";
        }

        if (vehiculo.getColor().length() > LONGITUD_MAXIMA_COLOR) {
            return "El color no puede superar los "
                    + LONGITUD_MAXIMA_COLOR
                    + " caracteres.";
        }

        if (vehiculo.getAnio() == null) {
            return "El año es obligatorio.";
        }

        int anioMaximo = Year.now().getValue() + 1;

        if (vehiculo.getAnio() < ANIO_MINIMO
                || vehiculo.getAnio() > anioMaximo) {

            return "El año debe encontrarse entre "
                    + ANIO_MINIMO
                    + " y "
                    + anioMaximo
                    + ".";
        }

        if (vehiculo.getDescripcion() == null
                || vehiculo.getDescripcion().isBlank()) {

            return "La descripción es obligatoria.";
        }

        if (vehiculo.getDescripcion().length()
                > LONGITUD_MAXIMA_DESCRIPCION) {

            return "La descripción no puede superar los "
                    + LONGITUD_MAXIMA_DESCRIPCION
                    + " caracteres.";
        }

        return null;
    }

    private void normalizarDatos(Vehiculo vehiculo) {

        if (vehiculo == null) {
            return;
        }

        if (vehiculo.getPlaca() != null) {
            vehiculo.setPlaca(
                    vehiculo.getPlaca()
                            .trim()
                            .toUpperCase(Locale.ROOT)
            );
        }

        if (vehiculo.getColor() != null) {
            vehiculo.setColor(vehiculo.getColor().trim());
        }

        if (vehiculo.getDescripcion() != null) {
            vehiculo.setDescripcion(
                    vehiculo.getDescripcion().trim()
            );
        }
    }
}