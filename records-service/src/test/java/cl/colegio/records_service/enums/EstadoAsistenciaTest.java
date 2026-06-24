package cl.colegio.records_service.enums;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

class EstadoAsistenciaTest {

    @ParameterizedTest
    @CsvSource({"PRESENTE,true", "AUSENTE,true", "ATRASADO,true", "JUSTIFICADO,true"})
    void esValido_returnsTrueForValidValues(String estado, boolean esperado) {
        assertThat(EstadoAsistencia.esValido(estado)).isEqualTo(esperado);
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "INVALIDO", "  "})
    void esValido_returnsFalseForInvalidValues(String estado) {
        assertThat(EstadoAsistencia.esValido(estado)).isFalse();
    }

    @ParameterizedTest
    @NullAndEmptySource
    void esValido_returnsFalseForNullOrEmpty(String estado) {
        assertThat(EstadoAsistencia.esValido(estado)).isFalse();
    }

    @Test
    void getMensajeError_containsAllValidValues() {
        String mensaje = EstadoAsistencia.getMensajeError();
        assertThat(mensaje).contains("PRESENTE", "AUSENTE", "ATRASADO", "JUSTIFICADO");
    }

    @Test
    void values_hasFourConstants() {
        assertThat(EstadoAsistencia.values()).hasSize(4);
    }
}
