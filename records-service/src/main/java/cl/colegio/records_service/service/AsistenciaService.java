package cl.colegio.records_service.service;

import cl.colegio.records_service.dto.AsistenciaRequestDTO;
import cl.colegio.records_service.dto.AsistenciaResponseDTO;
import cl.colegio.records_service.entity.Asistencia;

import java.time.LocalDate;
import java.util.List;

public interface AsistenciaService {

    List<AsistenciaResponseDTO> obtenerTodas();

    AsistenciaResponseDTO obtenerPorId(Long id);

    AsistenciaResponseDTO crear(AsistenciaRequestDTO dto);

    AsistenciaResponseDTO actualizar(Long id, AsistenciaRequestDTO dto);

    void eliminar(Long id);

    List<AsistenciaResponseDTO> buscarPorEstado(String estado);

    List<AsistenciaResponseDTO> buscarPorFecha(LocalDate inicio, LocalDate fin);

    List<AsistenciaResponseDTO> buscarPorDocente(Long idDocente);
}
