package cl.colegio.records_service.service.impl;

import cl.colegio.records_service.dto.AsistenciaRequestDTO;
import cl.colegio.records_service.dto.AsistenciaResponseDTO;
import cl.colegio.records_service.entity.Asistencia;
import cl.colegio.records_service.exception.ResourceNotFoundException;
import cl.colegio.records_service.factory.AsistenciaFactory;
import cl.colegio.records_service.mapper.AsistenciaMapper;
import cl.colegio.records_service.repository.AsistenciaRepository;
import cl.colegio.records_service.service.AsistenciaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor

public class AsistenciaServiceImpl implements AsistenciaService {

    private final AsistenciaRepository asistenciaRepository;
    private final AsistenciaFactory asistenciaFactory;
    private final AsistenciaMapper asistenciaMapper;

    @Override
    public List<AsistenciaResponseDTO> obtenerTodas() {
        return asistenciaMapper.toResponseList(asistenciaRepository.findAll());
    }

    @Override
    public AsistenciaResponseDTO obtenerPorId(Long id) {
        Asistencia asistencia = asistenciaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Asistencia no encontrada con ID: " + id));
        return asistenciaMapper.toResponseDTO(asistencia);
    }

    @Override
    public AsistenciaResponseDTO crear(AsistenciaRequestDTO dto) {
        Asistencia asistencia = asistenciaFactory.crearDesdeRequest(dto);
        Asistencia guardada = asistenciaRepository.save(asistencia);
        return asistenciaMapper.toResponseDTO(guardada);
    }

    @Override
    public AsistenciaResponseDTO actualizar(Long id, AsistenciaRequestDTO dto) {
        Asistencia asistencia = asistenciaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Asistencia no encontrada con ID: " + id));
        asistenciaFactory.actualizarDesdeRequest(asistencia, dto);
        Asistencia actualizada = asistenciaRepository.save(asistencia);
        return asistenciaMapper.toResponseDTO(actualizada);
    }

    @Override
    public void eliminar(Long id) {
        if (!asistenciaRepository.existsById(id)) {
            throw new ResourceNotFoundException("Asistencia no encontrada con ID: " + id);
        }
        asistenciaRepository.deleteById(id);
    }

    @Override
    public List<AsistenciaResponseDTO> buscarPorEstado(String estado) {
        return asistenciaMapper.toResponseList(asistenciaRepository.findByEstadoAsistencia(estado));
    }

    @Override
    public List<AsistenciaResponseDTO> buscarPorFecha(LocalDate inicio, LocalDate fin) {
        return asistenciaMapper.toResponseList(asistenciaRepository.findByFechaBetween(inicio, fin));
    }
}
