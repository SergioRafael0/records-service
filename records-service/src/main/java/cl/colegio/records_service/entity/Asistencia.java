package cl.colegio.records_service.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "asistencia")

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Asistencia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_asistencia")
    private Long idAsistencia;

    @Column(name = "id_estudiante", nullable = false)
    private Long idEstudiante;

    @Column(name = "id_docente", nullable = false)
    private Long idDocente;

    @Column(name = "id_curso", nullable = false)
    private Long idCurso;

    @Column(nullable = false)
    private LocalDate fecha;

    @Column(name = "estado_asistencia", nullable = false)
    private String estadoAsistencia;

    private String observacion;
}