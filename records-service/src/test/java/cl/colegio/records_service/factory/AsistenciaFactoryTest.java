package cl.colegio.records_service.factory;

import cl.colegio.records_service.dto.AsistenciaRequestDTO;
import cl.colegio.records_service.entity.Asistencia;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class AsistenciaFactoryTest {

    private final AsistenciaFactory factory = new AsistenciaFactory();

    @Test
    void crearDesdeRequest_mapsAllFields() {
        var dto = new AsistenciaRequestDTO(1L, 2L, 3L, LocalDate.of(2026, 6, 24), "PRESENTE", "A tiempo");

        Asistencia result = factory.crearDesdeRequest(dto);

        assertThat(result.getIdAsistencia()).isNull();
        assertThat(result.getIdEstudiante()).isEqualTo(1L);
        assertThat(result.getIdDocente()).isEqualTo(2L);
        assertThat(result.getIdCurso()).isEqualTo(3L);
        assertThat(result.getFecha()).isEqualTo(LocalDate.of(2026, 6, 24));
        assertThat(result.getEstadoAsistencia()).isEqualTo("PRESENTE");
        assertThat(result.getObservacion()).isEqualTo("A tiempo");
    }

    @Test
    void actualizarDesdeRequest_updatesAllFields() {
        var dto = new AsistenciaRequestDTO(4L, 5L, 6L, LocalDate.of(2026, 7, 1), "AUSENTE", "Actualizada");
        Asistencia asistencia = Asistencia.builder()
                .idAsistencia(20L)
                .idEstudiante(1L)
                .idDocente(2L)
                .idCurso(3L)
                .fecha(LocalDate.of(2026, 1, 1))
                .estadoAsistencia("PRESENTE")
                .observacion("Original")
                .build();

        factory.actualizarDesdeRequest(asistencia, dto);

        assertThat(asistencia.getIdAsistencia()).isEqualTo(20L);
        assertThat(asistencia.getIdEstudiante()).isEqualTo(4L);
        assertThat(asistencia.getIdDocente()).isEqualTo(5L);
        assertThat(asistencia.getIdCurso()).isEqualTo(6L);
        assertThat(asistencia.getFecha()).isEqualTo(LocalDate.of(2026, 7, 1));
        assertThat(asistencia.getEstadoAsistencia()).isEqualTo("AUSENTE");
        assertThat(asistencia.getObservacion()).isEqualTo("Actualizada");
    }
}
