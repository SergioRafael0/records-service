package cl.colegio.records_service.dto;

import java.time.LocalDate;

public record AnotacionResponseDTO(
        Long idAnotacion,
        Long idEstudiante,
        Long idDocente,
        LocalDate fecha,
        String tipoAnotacion,
        String descripcion
) {
}