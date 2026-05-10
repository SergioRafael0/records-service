package cl.colegio.records_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record AsistenciaRequestDTO(
        @NotNull(message = "El ID del estudiante es obligatorio")
        Long idEstudiante,

        @NotNull(message = "El ID del docente es obligatorio")
        Long idDocente,

        @NotNull(message = "El ID del curso es obligatorio")
        Long idCurso,

        @NotNull(message = "La fecha es obligatoria")
        @PastOrPresent(message = "La fecha no puede ser futura")
        LocalDate fecha,

        @NotBlank(message = "El estado de asistencia es obligatorio")
        @Size(max = 20, message = "El estado no puede exceder 20 caracteres")
        String estadoAsistencia,

        @Size(max = 250, message = "La observación no puede exceder 250 caracteres")
        String observacion
) {
}