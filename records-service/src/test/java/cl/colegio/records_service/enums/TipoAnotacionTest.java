package cl.colegio.records_service.enums;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

class TipoAnotacionTest {

    @ParameterizedTest
    @CsvSource({"POSITIVA,true", "NEGATIVA,true", "OBSERVACION,true"})
    void esValido_returnsTrueForValidValues(String tipo, boolean esperado) {
        assertThat(TipoAnotacion.esValido(tipo)).isEqualTo(esperado);
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "INVALIDO", "  "})
    void esValido_returnsFalseForInvalidValues(String tipo) {
        assertThat(TipoAnotacion.esValido(tipo)).isFalse();
    }

    @ParameterizedTest
    @NullAndEmptySource
    void esValido_returnsFalseForNullOrEmpty(String tipo) {
        assertThat(TipoAnotacion.esValido(tipo)).isFalse();
    }

    @Test
    void getMensajeError_containsAllValidValues() {
        String mensaje = TipoAnotacion.getMensajeError();
        assertThat(mensaje).contains("POSITIVA", "NEGATIVA", "OBSERVACION");
    }

    @Test
    void values_hasThreeConstants() {
        assertThat(TipoAnotacion.values()).hasSize(3);
    }
}
