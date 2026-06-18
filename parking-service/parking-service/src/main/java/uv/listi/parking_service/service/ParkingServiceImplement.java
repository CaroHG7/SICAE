package uv.listi.parking_service.service;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import uv.listi.parking_service.config.JwtUtil;
import uv.listi.parking_service.dto.ParkingEntradaRequest;
import uv.listi.parking_service.dto.ParkingEntradaResponse;
import uv.listi.parking_service.dto.ParkingSalidaRequest;
import uv.listi.parking_service.dto.ParkingSalidaResponse;
import uv.listi.parking_service.dto.UsuarioResponse;
import uv.listi.parking_service.dto.VehiculoResponse;
import uv.listi.parking_service.model.Espacio;
import uv.listi.parking_service.model.Movimiento;
import uv.listi.parking_service.repository.EspacioRepository;
import uv.listi.parking_service.repository.MovimientoRepository;

@Service
public class ParkingServiceImplement implements ParkingService{

    private final RestTemplate restTemplate;
    private final JwtUtil jwtUtil;
    private final MovimientoRepository movimientoRepository;
    private final EspacioRepository espacioRepository;

    @Value("${user.service.url}")
    private String userServiceUrl;

    @Value("${vehicle.service.url}")
    private String vehicleServiceUrl;
    
    public ParkingServiceImplement(RestTemplate restTemplate, JwtUtil jwtUtil, MovimientoRepository movimientoRepository, EspacioRepository espacioRepository) {
        this.restTemplate = restTemplate;
        this.jwtUtil = jwtUtil;
        this.movimientoRepository = movimientoRepository;
        this.espacioRepository = espacioRepository;
    }

    private void verificarToken(String token) {


        String tokenObtenido = jwtUtil.obtenerToken(token);

        if (tokenObtenido == null || !jwtUtil.validarToken(token)) {
            
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "No puede acceder. El token es inválido");

        }
    }

    private HttpEntity<Void> crearPeticionConToken(String token) {
    	HttpHeaders headers = new HttpHeaders();
    	headers.set(HttpHeaders.AUTHORIZATION, token);
    	return new HttpEntity<>(headers);
    }


    @Override
    @Transactional
    public ParkingEntradaResponse registrarEntrada(ParkingEntradaRequest request, String token) {
        verificarToken(token);
        if(request.getClaveUsuario() == null || request.getPlaca() == null || request.getIdEspacio() == null || request.getTarifa() == null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Faltan alguno de los datos requeridos: clave de usuario, placa, id del espacio o la tarifa");
            
        }

        try{
            ResponseEntity<UsuarioResponse> responseUsuario =
        	    restTemplate.exchange(
                	    userServiceUrl + "usuarios/clave/" + request.getClaveUsuario(),
                	    HttpMethod.GET,
                	    crearPeticionConToken(token),
                	    UsuarioResponse.class
         	     );

	        UsuarioResponse usuarioResponse = responseUsuario.getBody();

            /**if (usuarioResponse == null || !usuarioResponse.estaActivo()) {
                throw new ResponseStatusException(
                        HttpStatus.FORBIDDEN,
                        "El usuario no existe o su estatus no es activo"
                );
            }**/

            if (usuarioResponse == null) {
                throw new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "El usuario no existe"
                );
            }

            if (!usuarioResponse.estaActivo()) {
                throw new ResponseStatusException(
                        HttpStatus.FORBIDDEN,
                        "El estatus del usuario no es activo"
                );
            }

            ResponseEntity<VehiculoResponse[]> responseVehiculos =
                    restTemplate.exchange(
                            vehicleServiceUrl + "usuario/" + usuarioResponse.getIdUsuario(),
			    HttpMethod.GET,
			    crearPeticionConToken(token),
                            VehiculoResponse[].class
                    );

            VehiculoResponse[] vehiculos =
                    responseVehiculos.getBody();

            if (vehiculos == null) {
                vehiculos = new VehiculoResponse[0];
            }

            VehiculoResponse vehiculoResponse = null;

            for (VehiculoResponse vehiculo : vehiculos) {
                if (vehiculo.getPlaca() != null
                        && vehiculo.getPlaca().equalsIgnoreCase(request.getPlaca())) {
                    vehiculoResponse = vehiculo;
                    break;
                }
            }

            if (vehiculoResponse == null) {
                throw new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "El vehículo con la placa proporcionada no pertenece al usuario"
                );
            }

            if (!vehiculoResponse.estaActivo()) {
                throw new ResponseStatusException(
                        HttpStatus.FORBIDDEN,
                        "El vehículo se encuentra inactivo"
                );
            }

            String vehiculosUsuarioStr = java.util.Arrays.stream(vehiculos)
                    .map(VehiculoResponse::getIdVehiculo)
                    .filter(java.util.Objects::nonNull)
                    .map(String::valueOf)
                    .collect(Collectors.joining(","));

            if (!vehiculosUsuarioStr.isBlank()) {
                int vehiculosDentro =
                        movimientoRepository.movimientosUsuario(vehiculosUsuarioStr);

                if (vehiculosDentro >= 2) {
                    throw new ResponseStatusException(
                            HttpStatus.CONFLICT,
                            "El usuario ya tiene 2 vehículos dentro del estacionamiento"
                    );
                }
            }

            
            Espacio espacio = espacioRepository.buscarPorID(request.getIdEspacio());
            /**if(espacio == null|| Boolean.TRUE.equals(espacio.getOcupado())){
                throw new ResponseStatusException(HttpStatus.CONFLICT, "El espacio de estacionamiento no existe o ya esta ocupado");
            }**/

            if (espacio == null) {
                throw new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "El espacio de estacionamiento no exite"
                );
            }

            if (Boolean.TRUE.equals(espacio.getOcupado())) {
                throw new ResponseStatusException(
                        HttpStatus.CONFLICT,
                        "El espacio de estacionamiento ya está ocupado"
                );
            }

            espacio.setOcupado(true);
            espacioRepository.actualizarDisponibilidad(espacio.getIdEspacio(), espacio.getOcupado());

            Movimiento movimiento = new Movimiento();
            movimiento.setIdVehiculo(vehiculoResponse.getIdVehiculo());
            movimiento.setClaveUsuario(request.getClaveUsuario());
            movimiento.setPlaca(request.getPlaca());
            movimiento.setIdEspacio(request.getIdEspacio());
            movimiento.setEntrada(LocalDateTime.now());
            movimiento.setTiempoCreacion(LocalDateTime.now());
            movimiento.setTarifa(request.getTarifa());

            movimientoRepository.crearMovimiento(movimiento);
            return new ParkingEntradaResponse(movimiento.getIdMovimiento(),movimiento.getIdEspacio(), movimiento.getEntrada(), movimiento.getTarifa(), "Entrada registrada exitosamente");
            
        } catch (ResponseStatusException e) {
            throw e;
        } catch (HttpClientErrorException.NotFound e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "No se encontraron los datos solicitados"
            );
        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error al registrar la entrada: " + e.getMessage()
            );
        }
    }

    @Override
    @Transactional
    public ParkingSalidaResponse registrarSalida(
            ParkingSalidaRequest request,
            String token) {

        verificarToken(token);

        if (request.getClaveUsuario() == null
                || request.getClaveUsuario().isBlank()
                || request.getPlaca() == null
                || request.getPlaca().isBlank()) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "La clave del usuario y la placa son obligatorias"
            );
        }

        try {
            ResponseEntity<UsuarioResponse> responseUsuario =
        	    restTemplate.exchange(
                	    userServiceUrl + "usuarios/clave/" + request.getClaveUsuario(),
                	    HttpMethod.GET,
                	    crearPeticionConToken(token),
                            UsuarioResponse.class
        	    );

	     UsuarioResponse usuarioResponse = responseUsuario.getBody();


            if (usuarioResponse == null
                    || !usuarioResponse.estaActivo()) {

                throw new ResponseStatusException(
                        HttpStatus.FORBIDDEN,
                        "El usuario no existe o su estatus no es activo"
                );
            }

            ResponseEntity<VehiculoResponse[]> responseVehiculos =
                    restTemplate.exchange(
                            vehicleServiceUrl
                                    + "usuario/"
                                    + usuarioResponse.getIdUsuario(),
			    HttpMethod.GET,
			    crearPeticionConToken(token),
                            VehiculoResponse[].class
                    );

            VehiculoResponse[] vehiculos = responseVehiculos.getBody();

            if (vehiculos == null) {
                vehiculos = new VehiculoResponse[0];
            }

            VehiculoResponse vehiculoResponse = null;

            for (VehiculoResponse vehiculo : vehiculos) {
                if (vehiculo.getPlaca() != null
                        && vehiculo.getPlaca().equalsIgnoreCase(
                                request.getPlaca())) {

                    vehiculoResponse = vehiculo;
                    break;
                }
            }

            if (vehiculoResponse == null) {
                throw new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "El vehículo no pertenece al usuario indicado"
                );
            }

            Movimiento movimiento =
                    movimientoRepository.movimientosActivos(
                            vehiculoResponse.getIdVehiculo());

            if (movimiento == null) {
                throw new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "No existe una entrada activa para ese vehículo"
                );
            }

            LocalDateTime tiempoSalida = LocalDateTime.now();

            long minutosCalculados = Duration.between(
                    movimiento.getEntrada(),
                    tiempoSalida
            ).toMinutes();

            int minutosEstacionados =
                    (int) Math.max(0, minutosCalculados);

            int horasCobradas = Math.max(
                    1,
                    (int) Math.ceil(minutosEstacionados / 60.0)
            );

            BigDecimal costoTotal = movimiento.getTarifa()
                    .multiply(BigDecimal.valueOf(horasCobradas));

            movimiento.setSalida(tiempoSalida);
            movimiento.setTiempoActualizacion(tiempoSalida);
            movimiento.setMinEstacionado(minutosEstacionados);
            movimiento.setHorasCobradas(horasCobradas);
            movimiento.setCostoTotal(costoTotal);

            movimientoRepository.actualizarMovimiento(movimiento);

            Espacio espacio =
                    espacioRepository.buscarPorID(
                            movimiento.getIdEspacio());

            if (espacio != null) {
                espacio.setOcupado(false);

                espacioRepository.actualizarDisponibilidad(
                        movimiento.getIdEspacio(),
                        false
                );
            }

            return new ParkingSalidaResponse(
                    movimiento.getIdMovimiento(),
                    movimiento.getEntrada(),
                    movimiento.getSalida(),
                    movimiento.getIdEspacio(),
                    movimiento.getTarifa(),
                    movimiento.getCostoTotal(),
                    movimiento.getHorasCobradas(),
                    "Salida registrada exitosamente"
            );

        } catch (ResponseStatusException e) {
            throw e;

        } catch (HttpClientErrorException.NotFound e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "No se encontraron los datos solicitados"
            );

        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error al registrar la salida: " + e.getMessage()
            );
        }
    }
    
    @Override
    public List<Espacio> consultarEspacios(String token) {
        verificarToken(token);
        return espacioRepository.buscarDisponibles();
    }
}
