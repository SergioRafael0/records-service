package cl.colegio.records_service.mapper;

import cl.colegio.records_service.dto.AnotacionResponseDTO;
import cl.colegio.records_service.entity.Anotacion;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AnotacionMapper {

    public AnotacionResponseDTO toResponseDTO(Anotacion anotacion) {
        return new AnotacionResponseDTO(
                anotacion.getIdAnotacion(),
                anotacion.getIdEstudiante(),
                anotacion.getIdDocente(),
                anotacion.getFecha(),
                anotacion.getTipoAnotacion(),
                anotacion.getDescripcion()
        );
    }

    public List<AnotacionResponseDTO> toResponseList(List<Anotacion> anotacionList) {
        return anotacionList.stream()
                .map(this::toResponseDTO)
                .toList();
    }
}