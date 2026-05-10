package cl.colegio.records_service.dto;

import java.time.LocalDate;

public record AsistenciaResponseDTO(
        Long idAsistencia,
        Long idEstudiante,
        Long idDocente,
        Long idCurso,
        LocalDate fecha,
        String estadoAsistencia,
        String observacion
) {
}