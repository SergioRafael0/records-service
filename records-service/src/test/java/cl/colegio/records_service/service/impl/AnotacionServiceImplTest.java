package cl.colegio.records_service.service.impl;

import cl.colegio.records_service.client.UserServiceClient;
import cl.colegio.records_service.dto.AnotacionRequestDTO;
import cl.colegio.records_service.dto.AnotacionResponseDTO;
import cl.colegio.records_service.entity.Anotacion;
import cl.colegio.records_service.exception.BusinessRuleException;
import cl.colegio.records_service.exception.ResourceNotFoundException;
import cl.colegio.records_service.factory.AnotacionFactory;
import cl.colegio.records_service.mapper.AnotacionMapper;
import cl.colegio.records_service.repository.AnotacionRepository;
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
class AnotacionServiceImplTest {

    @Mock
    private AnotacionRepository anotacionRepository;

    @Mock
    private AnotacionFactory anotacionFactory;

    @Mock
    private AnotacionMapper anotacionMapper;

    @Mock
    private UserServiceClient userServiceClient;

    @InjectMocks
    private AnotacionServiceImpl anotacionService;

    private final AnotacionRequestDTO dtoValido = new AnotacionRequestDTO(
            1L, 2L, LocalDate.of(2026, 6, 24), "POSITIVA", "Buena conducta");

    private final Anotacion anotacion = Anotacion.builder()
            .idAnotacion(10L)
            .idEstudiante(1L)
            .idDocente(2L)
            .fecha(LocalDate.of(2026, 6, 24))
            .tipoAnotacion("POSITIVA")
            .descripcion("Buena conducta")
            .build();

    @Test
    void obtenerTodas_returnsList() {
        when(anotacionRepository.findAll()).thenReturn(List.of(anotacion));
        when(anotacionMapper.toResponseList(any())).thenReturn(List.of(
                new AnotacionResponseDTO(10L, 1L, 2L, LocalDate.of(2026, 6, 24), "POSITIVA", "Buena conducta")));

        var result = anotacionService.obtenerTodas();

        assertThat(result).hasSize(1);
        verify(anotacionRepository).findAll();
    }

    @Test
    void obtenerTodas_returnsEmptyList() {
        when(anotacionRepository.findAll()).thenReturn(List.of());
        when(anotacionMapper.toResponseList(any())).thenReturn(List.of());

        var result = anotacionService.obtenerTodas();

        assertThat(result).isEmpty();
    }

    @Test
    void obtenerPorId_returnsAnotacion() {
        when(anotacionRepository.findById(10L)).thenReturn(Optional.of(anotacion));
        when(anotacionMapper.toResponseDTO(anotacion)).thenReturn(
                new AnotacionResponseDTO(10L, 1L, 2L, LocalDate.of(2026, 6, 24), "POSITIVA", "Buena conducta"));

        var result = anotacionService.obtenerPorId(10L);

        assertThat(result.idAnotacion()).isEqualTo(10L);
        assertThat(result.idEstudiante()).isEqualTo(1L);
    }

    @Test
    void obtenerPorId_throwsWhenNotFound() {
        when(anotacionRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> anotacionService.obtenerPorId(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("99");
    }

    @Test
    void crear_returnsCreatedAnotacion() {
        when(userServiceClient.estudianteExists(1L)).thenReturn(true);
        when(userServiceClient.docenteExists(2L)).thenReturn(true);
        when(anotacionFactory.crearDesdeRequest(dtoValido)).thenReturn(anotacion);
        when(anotacionRepository.save(anotacion)).thenReturn(anotacion);
        when(anotacionMapper.toResponseDTO(anotacion)).thenReturn(
                new AnotacionResponseDTO(10L, 1L, 2L, LocalDate.of(2026, 6, 24), "POSITIVA", "Buena conducta"));

        var result = anotacionService.crear(dtoValido);

        assertThat(result.idAnotacion()).isEqualTo(10L);
        verify(anotacionRepository).save(anotacion);
    }

    @Test
    void crear_throwsWhenInvalidTipo() {
        var dtoInvalido = new AnotacionRequestDTO(1L, 2L, LocalDate.now(), "INVALIDO", "Test");

        assertThatThrownBy(() -> anotacionService.crear(dtoInvalido))
                .isInstanceOf(BusinessRuleException.class)
                .hasMessageContaining("Tipo de anotación inválido");
    }

    @Test
    void crear_throwsWhenStudentNotFound() {
        when(userServiceClient.estudianteExists(1L)).thenReturn(false);

        assertThatThrownBy(() -> anotacionService.crear(dtoValido))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("estudiante");
    }

    @Test
    void crear_throwsWhenDocenteNotFound() {
        when(userServiceClient.estudianteExists(1L)).thenReturn(true);
        when(userServiceClient.docenteExists(2L)).thenReturn(false);

        assertThatThrownBy(() -> anotacionService.crear(dtoValido))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("docente");
    }

    @Test
    void actualizar_returnsUpdatedAnotacion() {
        when(userServiceClient.estudianteExists(3L)).thenReturn(true);
        when(userServiceClient.docenteExists(4L)).thenReturn(true);
        when(anotacionRepository.findById(10L)).thenReturn(Optional.of(anotacion));
        when(anotacionRepository.save(anotacion)).thenReturn(anotacion);
        when(anotacionMapper.toResponseDTO(anotacion)).thenReturn(
                new AnotacionResponseDTO(10L, 3L, 4L, LocalDate.of(2026, 7, 1), "NEGATIVA", "Actualizada"));

        var dtoUpdate = new AnotacionRequestDTO(3L, 4L, LocalDate.of(2026, 7, 1), "NEGATIVA", "Actualizada");
        var result = anotacionService.actualizar(10L, dtoUpdate);

        assertThat(result.idAnotacion()).isEqualTo(10L);
        verify(anotacionFactory).actualizarDesdeRequest(anotacion, dtoUpdate);
    }

    @Test
    void actualizar_throwsWhenNotFound() {
        when(userServiceClient.estudianteExists(1L)).thenReturn(true);
        when(userServiceClient.docenteExists(2L)).thenReturn(true);
        when(anotacionRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> anotacionService.actualizar(99L, dtoValido))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("99");
    }

    @Test
    void actualizar_throwsWhenInvalidTipo() {
        var dtoInvalido = new AnotacionRequestDTO(1L, 2L, LocalDate.now(), "MALA", "Test");

        assertThatThrownBy(() -> anotacionService.actualizar(10L, dtoInvalido))
                .isInstanceOf(BusinessRuleException.class)
                .hasMessageContaining("inválido");
    }

    @Test
    void eliminar_deletesWhenExists() {
        when(anotacionRepository.existsById(10L)).thenReturn(true);

        anotacionService.eliminar(10L);

        verify(anotacionRepository).deleteById(10L);
    }

    @Test
    void eliminar_throwsWhenNotFound() {
        when(anotacionRepository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> anotacionService.eliminar(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("99");
    }

    @Test
    void buscarPorTipo_returnsList() {
        when(anotacionRepository.findByTipoAnotacion("POSITIVA")).thenReturn(List.of(anotacion));
        when(anotacionMapper.toResponseList(any())).thenReturn(List.of(
                new AnotacionResponseDTO(10L, 1L, 2L, LocalDate.of(2026, 6, 24), "POSITIVA", "Buena conducta")));

        var result = anotacionService.buscarPorTipo("POSITIVA");

        assertThat(result).hasSize(1);
    }

    @Test
    void buscarPorEstudiante_returnsList() {
        when(anotacionRepository.findByIdEstudiante(1L)).thenReturn(List.of(anotacion));
        when(anotacionMapper.toResponseList(any())).thenReturn(List.of(
                new AnotacionResponseDTO(10L, 1L, 2L, LocalDate.of(2026, 6, 24), "POSITIVA", "Buena conducta")));

        var result = anotacionService.buscarPorEstudiante(1L);

        assertThat(result).hasSize(1);
    }

    @Test
    void buscarPorDocente_returnsList() {
        when(anotacionRepository.findByIdDocente(2L)).thenReturn(List.of(anotacion));
        when(anotacionMapper.toResponseList(any())).thenReturn(List.of(
                new AnotacionResponseDTO(10L, 1L, 2L, LocalDate.of(2026, 6, 24), "POSITIVA", "Buena conducta")));

        var result = anotacionService.buscarPorDocente(2L);

        assertThat(result).hasSize(1);
    }
}
