package com.manuel.zaguan_inmobiliarias.config;

import com.manuel.zaguan_inmobiliarias.repository.user.JpaUserRepository;
import com.manuel.zaguan_inmobiliarias.service.jwt.JwtService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    //Todo pide login salvo /api/auth (login y registro). El JwtFilter lee la cookie y carga el
    //usuario; lo que ve cada uno dentro de su inmobiliaria lo decide AgencyScopeService
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, JwtService jwtService,
                                           JpaUserRepository jpaUserRepository) throws Exception {
        http
                //Sin esto el bean de abajo no se usa: el filtro de CORS es parte de la cadena
                .cors(Customizer.withDefaults())
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(new JwtFilter(jwtService, jpaUserRepository), UsernamePasswordAuthenticationFilter.class)
                //Sin login, 401. Sin esto Spring devuelve 403, que es para "logueado pero sin permiso"
                .exceptionHandling(e -> e.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))
                .authorizeHttpRequests(auth -> auth
                        //error: adonde Spring manda las excepciones no previstas; sin esto salian como 401
                        .requestMatchers("/api/auth/**", "/error").permitAll()
                        //Dar de alta, de baja, restaurar o listar inmobiliarias: solo el admin
                        .requestMatchers(HttpMethod.GET, "/api/agencies").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/agencies").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/agencies/*").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/api/agencies/*/restore").hasRole("ADMIN")
                        //Sumar a la inmobiliaria a alguien registrado: la inmobiliaria o el admin, no un agente
                        .requestMatchers(HttpMethod.POST, "/api/users/join-agency").hasAnyRole("ADMIN", "AGENCY")
                        //USER es el que se registra solo y no es de ninguna inmobiliaria: por ahora no ve nada
                        .anyRequest().hasAnyRole("ADMIN", "AGENCY", "AGENT"));

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
