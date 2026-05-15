package cl.colegio.records_service.repository;

import cl.colegio.records_service.entity.Asistencia;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface AsistenciaRepository extends JpaRepository<Asistencia, Long> {

    List<Asistencia> findByEstadoAsistencia(String estadoAsistencia);

    List<Asistencia> findByFechaBetween(LocalDate inicio, LocalDate fin);

    List<Asistencia> findByIdDocente(Long idDocente);
}