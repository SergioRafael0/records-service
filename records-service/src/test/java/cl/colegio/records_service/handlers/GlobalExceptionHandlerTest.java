package cl.colegio.records_service.handlers;

import cl.colegio.records_service.exception.BusinessRuleException;
import cl.colegio.records_service.exception.GlobalExceptionHandler;
import cl.colegio.records_service.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class GlobalExceptionHandlerTest {

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new TestController())
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void handleBusinessRuleException_returns400() throws Exception {
        mockMvc.perform(get("/business-rule"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Business Rule Violation"))
                .andExpect(jsonPath("$.message").value("Regla de negocio violada"));
    }

    @Test
    void handleResourceNotFound_returns404() throws Exception {
        mockMvc.perform(get("/not-found"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message").value("Recurso no encontrado"));
    }

    @Test
    void handleValidationErrors_returns400() throws Exception {
        mockMvc.perform(post("/validation-error")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.errors").exists());
    }

    @RestController
    static class TestController {

        @GetMapping("/business-rule")
        public Object businessRule() {
            throw new BusinessRuleException("Regla de negocio violada");
        }

        @GetMapping("/not-found")
        public Object notFound() {
            throw new ResourceNotFoundException("Recurso no encontrado");
        }

        @PostMapping("/validation-error")
        public Object validationError(@Valid @RequestBody TestDto dto) {
            return "ok";
        }

        record TestDto(@NotBlank String name) {}
    }
}
