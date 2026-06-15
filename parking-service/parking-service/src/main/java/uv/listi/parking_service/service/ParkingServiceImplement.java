package uv.listi.parking_service.service;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
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

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ParkingServiceImplement implements ParkingService{

    private final RestTemplate restTemplate;
    private final JwtUtil jwtUtil;
    private final MovimientoRepository movimientoRepository;
    private final EspacioRepository espacioRepository;

    private final String userServiceUrl = "http://localhost:8081/api/users/";
    private final String vehicleServiceUrl = "http://localhost:8082/api/vehicles/";

    public ParkingServiceImplement(RestTemplate restTemplate, JwtUtil jwtUtil, MovimientoRepository movimientoRepository, EspacioRepository espacioRepository) {
        this.restTemplate = restTemplate;
        this.jwtUtil = jwtUtil;
        this.movimientoRepository = movimientoRepository;
        this.espacioRepository = espacioRepository;
    }

    private void verificarToken(String token) {


        String tokenObtenido = jwtUtil.obtenerToken(token);

        if (tokenObtenido != null || !jwtUtil.validarToken(token)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "No puede acceder. El token es inválido");
        }
    }


    @Override
    public ParkingEntradaResponse registrarEntrada(ParkingEntradaRequest request, String token) {
        verificarToken(token);
        if(request.getClaveUsuario() !=null || request.getPlaca() != null || request.getIdEspacio()==null ||request.getTarifa()==null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Faltan alguno de los datos requeridos: clave de usuario, placa, id del espacio o la tarifa");
            
        }

        try{
            UsuarioResponse usuarioResponse = restTemplate.getForObject(userServiceUrl + request.getClaveUsuario(), UsuarioResponse.class);
            if(usuarioResponse == null || Boolean.FALSE.equals(usuarioResponse.getStatus())){
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "El usuario no existe o su estatus no es activo");
            }

            VehiculoResponse vehiculoResponse = restTemplate.getForObject(vehicleServiceUrl + request.getPlaca(), VehiculoResponse.class);
            if(vehiculoResponse == null){
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "El vehiculo con la placa proporcionada no existe");
            }

            if(!vehiculoResponse.getIdUsuario().equals(request.getClaveUsuario())){
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El vehiculo no pertenece al usuario que intenta ingresar");

            }

            ResponseEntity<List> responseVehiculos=restTemplate.getForEntity(vehicleServiceUrl + "user/" + request.getClaveUsuario(), List.class);
            List<LinkedHashMap<String, Object>> vehiculosUsuario = responseVehiculos.getBody();

            if(vehiculosUsuario!=null && !vehiculosUsuario.isEmpty()){
                String vehiculosUsuarioStr = vehiculosUsuario.stream()
                    .map(vehiculo -> vehiculo.get("idVehiculo").toString())
                    .collect(Collectors.joining(","));

                int vehiculosDentro = movimientoRepository.movimientosUsuario(vehiculosUsuarioStr);
                if(vehiculosDentro >= 2){
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El usuario ya tiene 2 vehiculos dentro del estacionamiento");
                }

            }
            
            Espacio espacio = espacioRepository.buscarPorID(request.getIdEspacio());
            if(espacio == null|| Boolean.TRUE.equals(espacio.getOcupado())){
                throw new ResponseStatusException(HttpStatus.CONFLICT, "El espacio de estacionamiento no existe o ya esta ocupado");
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
            
        } catch (HttpClientErrorException.NotFound e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No se encontraron los datos solicitaods");
        }catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al registrar la entrada: " + e.getMessage());

        }
       
    }

    @Override
    public ParkingSalidaResponse registrarSalida(ParkingSalidaRequest request, String token) {
        verificarToken(token);

        if(request.getClaveUsuario() == null || request.getPlaca() == null || request.getTiempoSalida() == null || request.getTiempoActualizacion() == null|| request.getCostoTotal() == null || request.getHorasCobradas() == null || request.getMinutosEstacionados() == null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Faltan alguno de los datos requeridos: clave de usuario, placa, tiempo de salida, de actualizacion, costo total, las horas cobradas o los minutos estacionado");
        }

        UsuarioResponse usuarioResponse = restTemplate.getForObject(userServiceUrl + request.getClaveUsuario(), UsuarioResponse.class);
        if(usuarioResponse == null || Boolean.FALSE.equals(usuarioResponse.getStatus())){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "El usuario no existe o su estatus no es activo");
        }

        VehiculoResponse vehiculoResponse = restTemplate.getForObject(vehicleServiceUrl + request.getPlaca(), VehiculoResponse.class);
        if(vehiculoResponse == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "El vehiculo con la placa proporcionada no existe");
        }

        if(!vehiculoResponse.getIdUsuario().equals(request.getClaveUsuario())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El vehiculo no pertenece al usuario que intenta ingresar");

        }

        Movimiento movimiento = movimientoRepository.movimientosActivos(vehiculoResponse.getIdVehiculo());
        if(movimiento == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No se encontró ningun movimiento activo para el vehículo proporcionado");
        }


        movimiento.setIdMovimiento(movimiento.getIdMovimiento());
        movimiento.setEntrada(movimiento.getEntrada());
        movimiento.setSalida(LocalDateTime.now());
        movimiento.setIdEspacio(movimiento.getIdEspacio());
        movimiento.setTarifa(movimiento.getTarifa());
        movimiento.setCostoTotal(movimiento.getCostoTotal());
        movimiento.setHorasCobradas(movimiento.getHorasCobradas());

        movimientoRepository.actualizarMovimiento(movimiento);


        Espacio espacio = espacioRepository.buscarPorID(movimiento.getIdEspacio());
        if (espacio != null) {
            espacio.setOcupado(false);
            espacioRepository.actualizarDisponibilidad(movimiento.getIdEspacio(), espacio.getOcupado() );
        }
        
        return new ParkingSalidaResponse(movimiento.getIdMovimiento(), movimiento.getEntrada(), movimiento.getSalida(), movimiento.getIdEspacio(), movimiento.getTarifa(), movimiento.getCostoTotal(), movimiento.getHorasCobradas(), "");


    }


    @Override
    public List<Espacio> consultarEspacios(String token) {
        verificarToken(token);
        return espacioRepository.buscarDisponibles();
    }
    
    
}
