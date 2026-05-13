package cl.colegio.records_service.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserServiceClient {

    private final WebClient webClient;

    @Value("${api.user.exists}")
    private String userExistsPath;

    public boolean usuarioExists(Long usuarioId) {
        try {
            Boolean existe = webClient.get()
                    .uri(String.format(userExistsPath, usuarioId))
                    .retrieve()
                    .bodyToMono(Boolean.class)
                    .block();
            return Boolean.TRUE.equals(existe);
        } catch (Exception e) {
            return false;
        }
    }

    public boolean estudianteExists(Long estudianteId) {
        return usuarioExists(estudianteId);
    }

    public boolean docenteExists(Long docenteId) {
        return usuarioExists(docenteId);
    }
}