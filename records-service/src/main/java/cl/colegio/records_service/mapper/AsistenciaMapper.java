package cl.colegio.records_service.mapper;

import cl.colegio.records_service.dto.AsistenciaResponseDTO;
import cl.colegio.records_service.entity.Asistencia;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AsistenciaMapper {

    public AsistenciaResponseDTO toResponseDTO(Asistencia asistencia) {
        return new AsistenciaResponseDTO(
                asistencia.getIdAsistencia(),
                asistencia.getIdEstudiante(),
                asistencia.getIdDocente(),
                asistencia.getIdCurso(),
                asistencia.getFecha(),
                asistencia.getEstadoAsistencia(),
                asistencia.getObservacion()
        );
    }

    public List<AsistenciaResponseDTO> toResponseList(List<Asistencia> asistenciaList) {
        return asistenciaList.stream()
                .map(this::toResponseDTO)
                .toList();
    }
}