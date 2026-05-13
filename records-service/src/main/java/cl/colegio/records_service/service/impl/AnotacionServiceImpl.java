package cl.colegio.records_service.service.impl;

import cl.colegio.records_service.client.UserServiceClient;
import cl.colegio.records_service.dto.AnotacionRequestDTO;
import cl.colegio.records_service.dto.AnotacionResponseDTO;
import cl.colegio.records_service.entity.Anotacion;
import cl.colegio.records_service.enums.TipoAnotacion;
import cl.colegio.records_service.exception.ResourceNotFoundException;
import cl.colegio.records_service.exception.BusinessRuleException;
import cl.colegio.records_service.factory.AnotacionFactory;
import cl.colegio.records_service.mapper.AnotacionMapper;
import cl.colegio.records_service.repository.AnotacionRepository;
import cl.colegio.records_service.service.AnotacionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor

public class AnotacionServiceImpl implements AnotacionService {

    private final AnotacionRepository anotacionRepository;
    private final AnotacionFactory anotacionFactory;
    private final AnotacionMapper anotacionMapper;
    private final UserServiceClient userServiceClient;

    @Override
    public List<AnotacionResponseDTO> obtenerTodas() {
        return anotacionMapper.toResponseList(anotacionRepository.findAll());
    }

    @Override
    public AnotacionResponseDTO obtenerPorId(Long id) {
        Anotacion anotacion = anotacionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Anotación no encontrada con ID: " + id));
        return anotacionMapper.toResponseDTO(anotacion);
    }

    @Override
    public AnotacionResponseDTO crear(AnotacionRequestDTO dto) {
        if (!TipoAnotacion.esValido(dto.tipoAnotacion())) {
            throw new BusinessRuleException(TipoAnotacion.getMensajeError());
        }
        if (!userServiceClient.estudianteExists(dto.idEstudiante())) {
            throw new ResourceNotFoundException("El estudiante con ID " + dto.idEstudiante() + " no existe en el sistema.");
        }
        if (!userServiceClient.docenteExists(dto.idDocente())) {
            throw new ResourceNotFoundException("El docente con ID " + dto.idDocente() + " no existe en el sistema.");
        }
        Anotacion anotacion = anotacionFactory.crearDesdeRequest(dto);
        Anotacion guardada = anotacionRepository.save(anotacion);
        return anotacionMapper.toResponseDTO(guardada);
    }

    @Override
    public AnotacionResponseDTO actualizar(Long id, AnotacionRequestDTO dto) {
        if (!TipoAnotacion.esValido(dto.tipoAnotacion())) {
            throw new BusinessRuleException(TipoAnotacion.getMensajeError());
        }
        if (!userServiceClient.estudianteExists(dto.idEstudiante())) {
            throw new ResourceNotFoundException("El estudiante con ID " + dto.idEstudiante() + " no existe en el sistema.");
        }
        if (!userServiceClient.docenteExists(dto.idDocente())) {
            throw new ResourceNotFoundException("El docente con ID " + dto.idDocente() + " no existe en el sistema.");
        }
        Anotacion anotacion = anotacionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Anotación no encontrada con ID: " + id));
        anotacionFactory.actualizarDesdeRequest(anotacion, dto);
        Anotacion actualizada = anotacionRepository.save(anotacion);
        return anotacionMapper.toResponseDTO(actualizada);
    }

    @Override
    public void eliminar(Long id) {
        if (!anotacionRepository.existsById(id)) {
            throw new ResourceNotFoundException("Anotación no encontrada con ID: " + id);
        }
        anotacionRepository.deleteById(id);
    }

    @Override
    public List<AnotacionResponseDTO> buscarPorTipo(String tipo) {
        return anotacionMapper.toResponseList(anotacionRepository.findByTipoAnotacion(tipo));
    }

    @Override
    public List<AnotacionResponseDTO> buscarPorEstudiante(Long idEstudiante) {
        return anotacionMapper.toResponseList(anotacionRepository.findByIdEstudiante(idEstudiante));
    }
}