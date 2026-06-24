package cl.colegio.records_service.factory;

import cl.colegio.records_service.dto.AnotacionRequestDTO;
import cl.colegio.records_service.entity.Anotacion;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class AnotacionFactoryTest {

    private final AnotacionFactory factory = new AnotacionFactory();

    @Test
    void crearDesdeRequest_mapsAllFields() {
        var dto = new AnotacionRequestDTO(1L, 2L, LocalDate.of(2026, 6, 24), "POSITIVA", "Buena conducta");

        Anotacion result = factory.crearDesdeRequest(dto);

        assertThat(result.getIdAnotacion()).isNull();
        assertThat(result.getIdEstudiante()).isEqualTo(1L);
        assertThat(result.getIdDocente()).isEqualTo(2L);
        assertThat(result.getFecha()).isEqualTo(LocalDate.of(2026, 6, 24));
        assertThat(result.getTipoAnotacion()).isEqualTo("POSITIVA");
        assertThat(result.getDescripcion()).isEqualTo("Buena conducta");
    }

    @Test
    void actualizarDesdeRequest_updatesAllFields() {
        var dto = new AnotacionRequestDTO(3L, 4L, LocalDate.of(2026, 7, 1), "NEGATIVA", "Actualizada");
        Anotacion anotacion = Anotacion.builder()
                .idAnotacion(10L)
                .idEstudiante(1L)
                .idDocente(2L)
                .fecha(LocalDate.of(2026, 1, 1))
                .tipoAnotacion("POSITIVA")
                .descripcion("Original")
                .build();

        factory.actualizarDesdeRequest(anotacion, dto);

        assertThat(anotacion.getIdAnotacion()).isEqualTo(10L);
        assertThat(anotacion.getIdEstudiante()).isEqualTo(3L);
        assertThat(anotacion.getIdDocente()).isEqualTo(4L);
        assertThat(anotacion.getFecha()).isEqualTo(LocalDate.of(2026, 7, 1));
        assertThat(anotacion.getTipoAnotacion()).isEqualTo("NEGATIVA");
        assertThat(anotacion.getDescripcion()).isEqualTo("Actualizada");
    }
}
