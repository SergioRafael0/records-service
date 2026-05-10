package cl.colegio.records_service.factory;

import cl.colegio.records_service.dto.AnotacionRequestDTO;
import cl.colegio.records_service.entity.Anotacion;
import org.springframework.stereotype.Component;

@Component
public class AnotacionFactory {

    public Anotacion crearDesdeRequest(AnotacionRequestDTO dto) {
        return Anotacion.builder()
                .idEstudiante(dto.idEstudiante())
                .idDocente(dto.idDocente())
                .fecha(dto.fecha())
                .tipoAnotacion(dto.tipoAnotacion())
                .descripcion(dto.descripcion())
                .build();
    }

    public void actualizarDesdeRequest(Anotacion anotacion, AnotacionRequestDTO dto) {
        anotacion.setIdEstudiante(dto.idEstudiante());
        anotacion.setIdDocente(dto.idDocente());
        anotacion.setFecha(dto.fecha());
        anotacion.setTipoAnotacion(dto.tipoAnotacion());
        anotacion.setDescripcion(dto.descripcion());
    }
}