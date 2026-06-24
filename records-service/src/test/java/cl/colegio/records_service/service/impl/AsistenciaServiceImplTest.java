package cl.colegio.records_service.service.impl;

import cl.colegio.records_service.client.UserServiceClient;
import cl.colegio.records_service.dto.AsistenciaRequestDTO;
import cl.colegio.records_service.dto.AsistenciaResponseDTO;
import cl.colegio.records_service.entity.Asistencia;
import cl.colegio.records_service.exception.BusinessRuleException;
import cl.colegio.records_service.exception.ResourceNotFoundException;
import cl.colegio.records_service.factory.AsistenciaFactory;
import cl.colegio.records_service.mapper.AsistenciaMapper;
import cl.colegio.records_service.repository.AsistenciaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AsistenciaServiceImplTest {

    @Mock
    private AsistenciaRepository asistenciaRepository;

    @Mock
    private AsistenciaFactory asistenciaFactory;

    @Mock
    private AsistenciaMapper asistenciaMapper;

    @Mock
    private UserServiceClient userServiceClient;

    @InjectMocks
    private AsistenciaServiceImpl asistenciaService;

    private final AsistenciaRequestDTO dtoValido = new AsistenciaRequestDTO(
            1L, 2L, 3L, LocalDate.of(2026, 6, 24), "PRESENTE", "A tiempo");

    private final Asistencia asistencia = Asistencia.builder()
            .idAsistencia(20L)
            .idEstudiante(1L)
            .idDocente(2L)
            .idCurso(3L)
            .fecha(LocalDate.of(2026, 6, 24))
            .estadoAsistencia("PRESENTE")
            .observacion("A tiempo")
            .build();

    @Test
    void obtenerTodas_returnsList() {
        when(asistenciaRepository.findAll()).thenReturn(List.of(asistencia));
        when(asistenciaMapper.toResponseList(any())).thenReturn(List.of(
                new AsistenciaResponseDTO(20L, 1L, 2L, 3L, LocalDate.of(2026, 6, 24), "PRESENTE", "A tiempo")));

        var result = asistenciaService.obtenerTodas();

        assertThat(result).hasSize(1);
        verify(asistenciaRepository).findAll();
    }

    @Test
    void obtenerTodas_returnsEmptyList() {
        when(asistenciaRepository.findAll()).thenReturn(List.of());
        when(asistenciaMapper.toResponseList(any())).thenReturn(List.of());

        var result = asistenciaService.obtenerTodas();

        assertThat(result).isEmpty();
    }

    @Test
    void obtenerPorId_returnsAsistencia() {
        when(asistenciaRepository.findById(20L)).thenReturn(Optional.of(asistencia));
        when(asistenciaMapper.toResponseDTO(asistencia)).thenReturn(
                new AsistenciaResponseDTO(20L, 1L, 2L, 3L, LocalDate.of(2026, 6, 24), "PRESENTE", "A tiempo"));

        var result = asistenciaService.obtenerPorId(20L);

        assertThat(result.idAsistencia()).isEqualTo(20L);
    }

    @Test
    void obtenerPorId_throwsWhenNotFound() {
        when(asistenciaRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> asistenciaService.obtenerPorId(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("99");
    }

    @Test
    void crear_returnsCreatedAsistencia() {
        when(userServiceClient.estudianteExists(1L)).thenReturn(true);
        when(userServiceClient.docenteExists(2L)).thenReturn(true);
        when(asistenciaFactory.crearDesdeRequest(dtoValido)).thenReturn(asistencia);
        when(asistenciaRepository.save(asistencia)).thenReturn(asistencia);
        when(asistenciaMapper.toResponseDTO(asistencia)).thenReturn(
                new AsistenciaResponseDTO(20L, 1L, 2L, 3L, LocalDate.of(2026, 6, 24), "PRESENTE", "A tiempo"));

        var result = asistenciaService.crear(dtoValido);

        assertThat(result.idAsistencia()).isEqualTo(20L);
        verify(asistenciaRepository).save(asistencia);
    }

    @Test
    void crear_throwsWhenInvalidEstado() {
        var dtoInvalido = new AsistenciaRequestDTO(1L, 2L, 3L, LocalDate.now(), "INVALIDO", null);

        assertThatThrownBy(() -> asistenciaService.crear(dtoInvalido))
                .isInstanceOf(BusinessRuleException.class)
                .hasMessageContaining("Estado de asistencia inválido");
    }

    @Test
    void crear_throwsWhenStudentNotFound() {
        when(userServiceClient.estudianteExists(1L)).thenReturn(false);

        assertThatThrownBy(() -> asistenciaService.crear(dtoValido))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("estudiante");
    }

    @Test
    void crear_throwsWhenDocenteNotFound() {
        when(userServiceClient.estudianteExists(1L)).thenReturn(true);
        when(userServiceClient.docenteExists(2L)).thenReturn(false);

        assertThatThrownBy(() -> asistenciaService.crear(dtoValido))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("docente");
    }

    @Test
    void actualizar_returnsUpdatedAsistencia() {
        when(userServiceClient.estudianteExists(4L)).thenReturn(true);
        when(userServiceClient.docenteExists(5L)).thenReturn(true);
        when(asistenciaRepository.findById(20L)).thenReturn(Optional.of(asistencia));
        when(asistenciaRepository.save(asistencia)).thenReturn(asistencia);
        when(asistenciaMapper.toResponseDTO(asistencia)).thenReturn(
                new AsistenciaResponseDTO(20L, 4L, 5L, 6L, LocalDate.of(2026, 7, 1), "AUSENTE", "Actualizada"));

        var dtoUpdate = new AsistenciaRequestDTO(4L, 5L, 6L, LocalDate.of(2026, 7, 1), "AUSENTE", "Actualizada");
        var result = asistenciaService.actualizar(20L, dtoUpdate);

        assertThat(result.idAsistencia()).isEqualTo(20L);
        verify(asistenciaFactory).actualizarDesdeRequest(asistencia, dtoUpdate);
    }

    @Test
    void actualizar_throwsWhenNotFound() {
        when(userServiceClient.estudianteExists(1L)).thenReturn(true);
        when(userServiceClient.docenteExists(2L)).thenReturn(true);
        when(asistenciaRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> asistenciaService.actualizar(99L, dtoValido))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("99");
    }

    @Test
    void actualizar_throwsWhenInvalidEstado() {
        var dtoInvalido = new AsistenciaRequestDTO(1L, 2L, 3L, LocalDate.now(), "MAL", null);

        assertThatThrownBy(() -> asistenciaService.actualizar(20L, dtoInvalido))
                .isInstanceOf(BusinessRuleException.class)
                .hasMessageContaining("inválido");
    }

    @Test
    void eliminar_deletesWhenExists() {
        when(asistenciaRepository.existsById(20L)).thenReturn(true);

        asistenciaService.eliminar(20L);

        verify(asistenciaRepository).deleteById(20L);
    }

    @Test
    void eliminar_throwsWhenNotFound() {
        when(asistenciaRepository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> asistenciaService.eliminar(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("99");
    }

    @Test
    void buscarPorEstado_returnsList() {
        when(asistenciaRepository.findByEstadoAsistencia("PRESENTE")).thenReturn(List.of(asistencia));
        when(asistenciaMapper.toResponseList(any())).thenReturn(List.of(
                new AsistenciaResponseDTO(20L, 1L, 2L, 3L, LocalDate.of(2026, 6, 24), "PRESENTE", "A tiempo")));

        var result = asistenciaService.buscarPorEstado("PRESENTE");

        assertThat(result).hasSize(1);
    }

    @Test
    void buscarPorFecha_returnsList() {
        var inicio = LocalDate.of(2026, 6, 1);
        var fin = LocalDate.of(2026, 6, 30);
        when(asistenciaRepository.findByFechaBetween(inicio, fin)).thenReturn(List.of(asistencia));
        when(asistenciaMapper.toResponseList(any())).thenReturn(List.of(
                new AsistenciaResponseDTO(20L, 1L, 2L, 3L, LocalDate.of(2026, 6, 24), "PRESENTE", "A tiempo")));

        var result = asistenciaService.buscarPorFecha(inicio, fin);

        assertThat(result).hasSize(1);
    }

    @Test
    void buscarPorDocente_returnsList() {
        when(asistenciaRepository.findByIdDocente(2L)).thenReturn(List.of(asistencia));
        when(asistenciaMapper.toResponseList(any())).thenReturn(List.of(
                new AsistenciaResponseDTO(20L, 1L, 2L, 3L, LocalDate.of(2026, 6, 24), "PRESENTE", "A tiempo")));

        var result = asistenciaService.buscarPorDocente(2L);

        assertThat(result).hasSize(1);
    }
}
