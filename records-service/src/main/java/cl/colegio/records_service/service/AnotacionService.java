package cl.colegio.records_service.service;

import cl.colegio.records_service.dto.AnotacionRequestDTO;
import cl.colegio.records_service.dto.AnotacionResponseDTO;

import java.util.List;

public interface AnotacionService {

    List<AnotacionResponseDTO> obtenerTodas();

    AnotacionResponseDTO obtenerPorId(Long id);

    AnotacionResponseDTO crear(AnotacionRequestDTO dto);

    AnotacionResponseDTO actualizar(Long id, AnotacionRequestDTO dto);

    void eliminar(Long id);

    List<AnotacionResponseDTO> buscarPorTipo(String tipo);

    List<AnotacionResponseDTO> buscarPorEstudiante(Long idEstudiante);
}