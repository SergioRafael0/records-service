package cl.colegio.records_service.mapper;

import cl.colegio.records_service.dto.AsistenciaResponseDTO;
import cl.colegio.records_service.entity.Asistencia;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class AsistenciaMapperTest {

    private final AsistenciaMapper mapper = new AsistenciaMapper();

    @Test
    void toResponseDTO_mapsAllFields() {
        Asistencia entity = Asistencia.builder()
                .idAsistencia(1L)
                .idEstudiante(10L)
                .idDocente(20L)
                .idCurso(30L)
                .fecha(LocalDate.of(2026, 6, 24))
                .estadoAsistencia("PRESENTE")
                .observacion("A tiempo")
                .build();

        AsistenciaResponseDTO dto = mapper.toResponseDTO(entity);

        assertThat(dto.idAsistencia()).isEqualTo(1L);
        assertThat(dto.idEstudiante()).isEqualTo(10L);
        assertThat(dto.idDocente()).isEqualTo(20L);
        assertThat(dto.idCurso()).isEqualTo(30L);
        assertThat(dto.fecha()).isEqualTo(LocalDate.of(2026, 6, 24));
        assertThat(dto.estadoAsistencia()).isEqualTo("PRESENTE");
        assertThat(dto.observacion()).isEqualTo("A tiempo");
    }

    @Test
    void toResponseList_mapsAllEntities() {
        List<Asistencia> entities = List.of(
                Asistencia.builder().idAsistencia(1L).build(),
                Asistencia.builder().idAsistencia(2L).build()
        );

        List<AsistenciaResponseDTO> dtos = mapper.toResponseList(entities);

        assertThat(dtos).hasSize(2);
        assertThat(dtos.get(0).idAsistencia()).isEqualTo(1L);
        assertThat(dtos.get(1).idAsistencia()).isEqualTo(2L);
    }

    @Test
    void toResponseList_returnsEmptyList_whenInputEmpty() {
        List<AsistenciaResponseDTO> dtos = mapper.toResponseList(List.of());
        assertThat(dtos).isEmpty();
    }
}
