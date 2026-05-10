package cl.colegio.records_service.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "anotacion")

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Anotacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_anotacion")
    private Long idAnotacion;

    @Column(name = "id_estudiante", nullable = false)
    private Long idEstudiante;

    @Column(name = "id_docente", nullable = false)
    private Long idDocente;

    @Column(nullable = false)
    private LocalDate fecha;

    @Column(name = "tipo_anotacion", nullable = false)
    private String tipoAnotacion;

    @Column(nullable = false, length = 500)
    private String descripcion;
}