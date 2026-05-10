package cl.colegio.records_service.factory;

import cl.colegio.records_service.dto.AsistenciaRequestDTO;
import cl.colegio.records_service.entity.Asistencia;
import org.springframework.stereotype.Component;

@Component
public class AsistenciaFactory {

    public Asistencia crearDesdeRequest(AsistenciaRequestDTO dto) {
        return Asistencia.builder()
                .idEstudiante(dto.idEstudiante())
                .idDocente(dto.idDocente())
                .idCurso(dto.idCurso())
                .fecha(dto.fecha())
                .estadoAsistencia(dto.estadoAsistencia())
                .observacion(dto.observacion())
                .build();
    }

    public void actualizarDesdeRequest(Asistencia asistencia, AsistenciaRequestDTO dto) {
        asistencia.setIdEstudiante(dto.idEstudiante());
        asistencia.setIdDocente(dto.idDocente());
        asistencia.setIdCurso(dto.idCurso());
        asistencia.setFecha(dto.fecha());
        asistencia.setEstadoAsistencia(dto.estadoAsistencia());
        asistencia.setObservacion(dto.observacion());
    }
}