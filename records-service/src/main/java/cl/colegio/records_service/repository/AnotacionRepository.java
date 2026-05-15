package cl.colegio.records_service.repository;

import cl.colegio.records_service.entity.Anotacion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnotacionRepository extends JpaRepository<Anotacion, Long> {

    List<Anotacion> findByTipoAnotacion(String tipoAnotacion);

    List<Anotacion> findByIdEstudiante(Long idEstudiante);

    List<Anotacion> findByIdDocente(Long idDocente);
}