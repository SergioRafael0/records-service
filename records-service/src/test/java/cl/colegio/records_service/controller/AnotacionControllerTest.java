package cl.colegio.records_service.controller;

import cl.colegio.records_service.dto.AnotacionRequestDTO;
import cl.colegio.records_service.dto.AnotacionResponseDTO;
import cl.colegio.records_service.exception.BusinessRuleException;
import cl.colegio.records_service.exception.ResourceNotFoundException;
import cl.colegio.records_service.service.AnotacionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AnotacionController.class)
class AnotacionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AnotacionService anotacionService;

    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
    private final AnotacionResponseDTO dtoResponse = new AnotacionResponseDTO(
            1L, 10L, 20L, LocalDate.of(2026, 6, 24), "POSITIVA", "Buena conducta");

    @Test
    @WithMockUser
    void obtenerTodas_returns200() throws Exception {
        when(anotacionService.obtenerTodas()).thenReturn(List.of(dtoResponse));

        mockMvc.perform(get("/api/v1/anotaciones"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].idAnotacion").value(1));
    }

    @Test
    @WithMockUser
    void obtenerPorId_returns200() throws Exception {
        when(anotacionService.obtenerPorId(1L)).thenReturn(dtoResponse);

        mockMvc.perform(get("/api/v1/anotaciones/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idAnotacion").value(1));
    }

    @Test
    @WithMockUser
    void obtenerPorId_returns404_whenNotFound() throws Exception {
        when(anotacionService.obtenerPorId(99L)).thenThrow(new ResourceNotFoundException("Anotación no encontrada con ID: 99"));

        mockMvc.perform(get("/api/v1/anotaciones/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void crear_returns201() throws Exception {
        var request = new AnotacionRequestDTO(10L, 20L, LocalDate.of(2026, 6, 24), "POSITIVA", "Buena conducta");
        when(anotacionService.crear(any())).thenReturn(dtoResponse);

        mockMvc.perform(post("/api/v1/anotaciones")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idAnotacion").value(1));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void crear_returns400_whenInvalidTipo() throws Exception {
        var request = new AnotacionRequestDTO(10L, 20L, LocalDate.now(), "INVALIDO", "Test");
        when(anotacionService.crear(any())).thenThrow(new BusinessRuleException("Tipo de anotación inválido"));

        mockMvc.perform(post("/api/v1/anotaciones")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void actualizar_returns200() throws Exception {
        var request = new AnotacionRequestDTO(10L, 20L, LocalDate.of(2026, 6, 23), "NEGATIVA", "Actualizada");
        when(anotacionService.actualizar(eq(1L), any())).thenReturn(dtoResponse);

        mockMvc.perform(put("/api/v1/anotaciones/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idAnotacion").value(1));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void eliminar_returns204() throws Exception {
        mockMvc.perform(delete("/api/v1/anotaciones/1")
                        .with(csrf()))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser
    void buscarPorTipo_returns200() throws Exception {
        when(anotacionService.buscarPorTipo("POSITIVA")).thenReturn(List.of(dtoResponse));

        mockMvc.perform(get("/api/v1/anotaciones/tipo/POSITIVA"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1));
    }

    @Test
    @WithMockUser
    void buscarPorEstudiante_returns200() throws Exception {
        when(anotacionService.buscarPorEstudiante(10L)).thenReturn(List.of(dtoResponse));

        mockMvc.perform(get("/api/v1/anotaciones/estudiante/10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1));
    }

    @Test
    @WithMockUser
    void buscarPorDocente_returns200() throws Exception {
        when(anotacionService.buscarPorDocente(20L)).thenReturn(List.of(dtoResponse));

        mockMvc.perform(get("/api/v1/anotaciones/docente/20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1));
    }
}
