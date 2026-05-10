package cl.colegio.records_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record AnotacionRequestDTO(
        @NotNull(message = "El ID del estudiante es obligatorio")
        Long idEstudiante,

        @NotNull(message = "El ID del docente es obligatorio")
        Long idDocente,

        @NotNull(message = "La fecha es obligatoria")
        @PastOrPresent(message = "La fecha no puede ser futura")
        LocalDate fecha,

        @NotBlank(message = "El tipo de anotación es obligatorio")
        @Size(max = 20, message = "El tipo no puede exceder 20 caracteres")
        String tipoAnotacion,

        @NotBlank(message = "La descripción es obligatoria")
        @Size(max = 500, message = "La descripción no puede exceder 500 caracteres")
        String descripcion
) {
}