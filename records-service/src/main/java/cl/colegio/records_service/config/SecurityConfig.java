package cl.colegio.records_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfig {
    private final JwtValidationFilter jwtValidationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
            .csrf(AbstractHttpConfigurer::disable)
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                // Solo ADMIN o DOCENTE pueden crear/editarsi ben Asistencia
                .requestMatchers(HttpMethod.POST, "/api/v1/asistencias/**").hasAnyRole("ADMIN", "DOCENTE")
                .requestMatchers(HttpMethod.PUT, "/api/v1/asistencias/**").hasAnyRole("ADMIN", "DOCENTE")
                .requestMatchers(HttpMethod.DELETE, "/api/v1/asistencias/**").hasRole("ADMIN")
                
                // Solo ADMIN o DOCENTE pueden crear/editar anotaciones
                .requestMatchers(HttpMethod.POST, "/api/v1/anotaciones/**").hasAnyRole("ADMIN", "DOCENTE")
                .requestMatchers(HttpMethod.PUT, "/api/v1/anotaciones/**").hasAnyRole("ADMIN", "DOCENTE")
                .requestMatchers(HttpMethod.DELETE, "/api/v1/anotaciones/**").hasRole("ADMIN")
                
                // Cualquiera autenticado puede leer datos (GET)
                .requestMatchers(HttpMethod.GET, "/api/v1/**").authenticated()
                
                // Swagger y actuator sin autenticación
                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/actuator/**").permitAll()
                
                .anyRequest().authenticated()
            )
            .addFilterBefore(jwtValidationFilter, UsernamePasswordAuthenticationFilter.class)
            .build();
    }
}