package cl.colegio.records_service.client;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceClientTest {

    @Mock
    private WebClient webClient;

    @Mock
    private WebClient.RequestHeadersUriSpec requestSpec;

    @Mock
    private WebClient.ResponseSpec responseSpec;

    private UserServiceClient userServiceClient;

    @BeforeEach
    void setUp() {
        userServiceClient = new UserServiceClient(webClient);
        ReflectionTestUtils.setField(userServiceClient, "userExistsPath", "/api/v1/usuarios/%d/exists");
    }

    @Test
    void usuarioExists_returnsTrue() {
        when(webClient.get()).thenReturn(requestSpec);
        when(requestSpec.uri("/api/v1/usuarios/1/exists")).thenReturn(requestSpec);
        when(requestSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(Boolean.class)).thenReturn(Mono.just(true));

        boolean result = userServiceClient.usuarioExists(1L);

        assertThat(result).isTrue();
    }

    @Test
    void usuarioExists_returnsFalse() {
        when(webClient.get()).thenReturn(requestSpec);
        when(requestSpec.uri("/api/v1/usuarios/99/exists")).thenReturn(requestSpec);
        when(requestSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(Boolean.class)).thenReturn(Mono.just(false));

        boolean result = userServiceClient.usuarioExists(99L);

        assertThat(result).isFalse();
    }

    @Test
    void usuarioExists_returnsFalseWhenException() {
        when(webClient.get()).thenReturn(requestSpec);
        when(requestSpec.uri("/api/v1/usuarios/1/exists")).thenReturn(requestSpec);
        when(requestSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(Boolean.class)).thenThrow(new RuntimeException("Connection error"));

        boolean result = userServiceClient.usuarioExists(1L);

        assertThat(result).isFalse();
    }

    @Test
    void estudianteExists_delegatesToUsuarioExists() {
        when(webClient.get()).thenReturn(requestSpec);
        when(requestSpec.uri("/api/v1/usuarios/1/exists")).thenReturn(requestSpec);
        when(requestSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(Boolean.class)).thenReturn(Mono.just(true));

        boolean result = userServiceClient.estudianteExists(1L);

        assertThat(result).isTrue();
    }

    @Test
    void docenteExists_delegatesToUsuarioExists() {
        when(webClient.get()).thenReturn(requestSpec);
        when(requestSpec.uri("/api/v1/usuarios/2/exists")).thenReturn(requestSpec);
        when(requestSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(Boolean.class)).thenReturn(Mono.just(true));

        boolean result = userServiceClient.docenteExists(2L);

        assertThat(result).isTrue();
    }
}
