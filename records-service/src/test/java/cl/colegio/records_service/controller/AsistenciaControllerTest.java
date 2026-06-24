package cl.colegio.records_service.controller;

import cl.colegio.records_service.dto.AsistenciaRequestDTO;
import cl.colegio.records_service.dto.AsistenciaResponseDTO;
import cl.colegio.records_service.exception.BusinessRuleException;
import cl.colegio.records_service.exception.ResourceNotFoundException;
import cl.colegio.records_service.service.AsistenciaService;
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

@WebMvcTest(AsistenciaController.class)
class AsistenciaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AsistenciaService asistenciaService;

    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
    private final AsistenciaResponseDTO dtoResponse = new AsistenciaResponseDTO(
            1L, 10L, 20L, 30L, LocalDate.of(2026, 6, 24), "PRESENTE", "A tiempo");

    @Test
    @WithMockUser
    void obtenerTodas_returns200() throws Exception {
        when(asistenciaService.obtenerTodas()).thenReturn(List.of(dtoResponse));

        mockMvc.perform(get("/api/v1/asistencias"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].idAsistencia").value(1));
    }

    @Test
    @WithMockUser
    void obtenerPorId_returns200() throws Exception {
        when(asistenciaService.obtenerPorId(1L)).thenReturn(dtoResponse);

        mockMvc.perform(get("/api/v1/asistencias/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idAsistencia").value(1));
    }

    @Test
    @WithMockUser
    void obtenerPorId_returns404_whenNotFound() throws Exception {
        when(asistenciaService.obtenerPorId(99L)).thenThrow(new ResourceNotFoundException("Asistencia no encontrada con ID: 99"));

        mockMvc.perform(get("/api/v1/asistencias/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void crear_returns201() throws Exception {
        var request = new AsistenciaRequestDTO(10L, 20L, 30L, LocalDate.of(2026, 6, 24), "PRESENTE", "A tiempo");
        when(asistenciaService.crear(any())).thenReturn(dtoResponse);

        mockMvc.perform(post("/api/v1/asistencias")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idAsistencia").value(1));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void crear_returns400_whenInvalidEstado() throws Exception {
        var request = new AsistenciaRequestDTO(10L, 20L, 30L, LocalDate.now(), "INVALIDO", null);
        when(asistenciaService.crear(any())).thenThrow(new BusinessRuleException("Estado de asistencia inválido"));

        mockMvc.perform(post("/api/v1/asistencias")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void actualizar_returns200() throws Exception {
        var request = new AsistenciaRequestDTO(10L, 20L, 30L, LocalDate.of(2026, 6, 23), "AUSENTE", "Actualizada");
        when(asistenciaService.actualizar(eq(1L), any())).thenReturn(dtoResponse);

        mockMvc.perform(put("/api/v1/asistencias/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idAsistencia").value(1));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void eliminar_returns204() throws Exception {
        mockMvc.perform(delete("/api/v1/asistencias/1")
                        .with(csrf()))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser
    void buscarPorEstado_returns200() throws Exception {
        when(asistenciaService.buscarPorEstado("PRESENTE")).thenReturn(List.of(dtoResponse));

        mockMvc.perform(get("/api/v1/asistencias/estado/PRESENTE"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1));
    }

    @Test
    @WithMockUser
    void buscarPorFecha_returns200() throws Exception {
        when(asistenciaService.buscarPorFecha(LocalDate.of(2026, 6, 1), LocalDate.of(2026, 6, 30)))
                .thenReturn(List.of(dtoResponse));

        mockMvc.perform(get("/api/v1/asistencias/fecha")
                        .param("inicio", "2026-06-01")
                        .param("fin", "2026-06-30"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1));
    }

    @Test
    @WithMockUser
    void buscarPorDocente_returns200() throws Exception {
        when(asistenciaService.buscarPorDocente(20L)).thenReturn(List.of(dtoResponse));

        mockMvc.perform(get("/api/v1/asistencias/docente/20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1));
    }
}
