package cl.colegio.records_service.service.impl;

import cl.colegio.records_service.client.UserServiceClient;
import cl.colegio.records_service.dto.AsistenciaRequestDTO;
import cl.colegio.records_service.dto.AsistenciaResponseDTO;
import cl.colegio.records_service.entity.Asistencia;
import cl.colegio.records_service.enums.EstadoAsistencia;
import cl.colegio.records_service.exception.ResourceNotFoundException;
import cl.colegio.records_service.exception.BusinessRuleException;
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
    private final UserServiceClient userServiceClient;

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
        if (!EstadoAsistencia.esValido(dto.estadoAsistencia())) {
            throw new BusinessRuleException(EstadoAsistencia.getMensajeError());
        }
        if (!userServiceClient.estudianteExists(dto.idEstudiante())) {
            throw new ResourceNotFoundException("El estudiante con ID " + dto.idEstudiante() + " no existe en el sistema.");
        }
        if (!userServiceClient.docenteExists(dto.idDocente())) {
            throw new ResourceNotFoundException("El docente con ID " + dto.idDocente() + " no existe en el sistema.");
        }
        Asistencia asistencia = asistenciaFactory.crearDesdeRequest(dto);
        Asistencia guardada = asistenciaRepository.save(asistencia);
        return asistenciaMapper.toResponseDTO(guardada);
    }

    @Override
    public AsistenciaResponseDTO actualizar(Long id, AsistenciaRequestDTO dto) {
        if (!EstadoAsistencia.esValido(dto.estadoAsistencia())) {
            throw new BusinessRuleException(EstadoAsistencia.getMensajeError());
        }
        if (!userServiceClient.estudianteExists(dto.idEstudiante())) {
            throw new ResourceNotFoundException("El estudiante con ID " + dto.idEstudiante() + " no existe en el sistema.");
        }
        if (!userServiceClient.docenteExists(dto.idDocente())) {
            throw new ResourceNotFoundException("El docente con ID " + dto.idDocente() + " no existe en el sistema.");
        }
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
