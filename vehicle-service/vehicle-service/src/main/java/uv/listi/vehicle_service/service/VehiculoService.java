package uv.listi.vehicle_service.service;

import java.util.List;

import org.springframework.stereotype.Service;

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

}