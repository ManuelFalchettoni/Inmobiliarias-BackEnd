package com.manuel.zaguan_inmobiliarias.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // API abierta para desarrollo.
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                //Sin esto el bean de abajo no se usa: el filtro de CORS es parte de la cadena
                .cors(Customizer.withDefaults())
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll());

        return http.build();
    }

    //Permisos para el front, que corre en otro puerto. Sin esto el navegador corta cualquier
    //llamada a la API, aunque la API este abierta.
    //Los origenes salen de cors.allowed-origins, que tiene los de local por defecto
    @Bean
    public CorsConfigurationSource corsConfigurationSource(
            @Value("${cors.allowed-origins}") List<String> allowedOrigins) {

        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(allowedOrigins);
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        //Con credenciales: el login manda el token en una cookie, y sin esto el navegador no la
        //guarda ni la reenvia a otro puerto. Por eso los origenes van con nombre y nunca "*".
        //El front tiene que llamar con credentials: "include" (fetch) o withCredentials (axios)
        config.setAllowCredentials(true);
        config.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/api/**", config);
        return source;
    }

}
