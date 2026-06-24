package cl.colegio.records_service.mapper;

import cl.colegio.records_service.dto.AnotacionResponseDTO;
import cl.colegio.records_service.entity.Anotacion;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class AnotacionMapperTest {

    private final AnotacionMapper mapper = new AnotacionMapper();

    @Test
    void toResponseDTO_mapsAllFields() {
        Anotacion entity = Anotacion.builder()
                .idAnotacion(1L)
                .idEstudiante(10L)
                .idDocente(20L)
                .fecha(LocalDate.of(2026, 6, 24))
                .tipoAnotacion("POSITIVA")
                .descripcion("Buena conducta")
                .build();

        AnotacionResponseDTO dto = mapper.toResponseDTO(entity);

        assertThat(dto.idAnotacion()).isEqualTo(1L);
        assertThat(dto.idEstudiante()).isEqualTo(10L);
        assertThat(dto.idDocente()).isEqualTo(20L);
        assertThat(dto.fecha()).isEqualTo(LocalDate.of(2026, 6, 24));
        assertThat(dto.tipoAnotacion()).isEqualTo("POSITIVA");
        assertThat(dto.descripcion()).isEqualTo("Buena conducta");
    }

    @Test
    void toResponseList_mapsAllEntities() {
        List<Anotacion> entities = List.of(
                Anotacion.builder().idAnotacion(1L).build(),
                Anotacion.builder().idAnotacion(2L).build()
        );

        List<AnotacionResponseDTO> dtos = mapper.toResponseList(entities);

        assertThat(dtos).hasSize(2);
        assertThat(dtos.get(0).idAnotacion()).isEqualTo(1L);
        assertThat(dtos.get(1).idAnotacion()).isEqualTo(2L);
    }

    @Test
    void toResponseList_returnsEmptyList_whenInputEmpty() {
        List<AnotacionResponseDTO> dtos = mapper.toResponseList(List.of());
        assertThat(dtos).isEmpty();
    }
}
