package com.manuel.zaguan_inmobiliarias.config;

import com.manuel.zaguan_inmobiliarias.entity.user.User;
import com.manuel.zaguan_inmobiliarias.repository.user.JpaUserRepository;
import com.manuel.zaguan_inmobiliarias.service.jwt.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;

//Sin @Component a proposito: un Filter que es bean, Spring Boot lo registra para todos los
//requests por fuera de Security. Lo crea SecurityConfig y lo mete en su cadena.
//No corta ningun request: si hay un token valido deja cargado el usuario, y si no sigue sin
//usuario. Que una ruta pida login o no lo decide SecurityConfig
@AllArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final JpaUserRepository jpaUserRepository;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws IOException, ServletException{
        String token = null;
        if (request.getCookies() != null) {
            token = Arrays.stream(request.getCookies())
                    .filter(c -> c.getName().equals("token"))
                    .map(Cookie::getValue)
                    .findFirst()
                    .orElse(null);
        }

        if (token != null && jwtService.isTokenValid(token)) {
            //Activo: uno dado de baja despues del login no entra aunque su token siga vigente
            Long userId = jwtService.getUserIdFromToken(token);
            User user = jpaUserRepository.findByIdAndActiveTrue(userId).orElse(null);

            if (user != null) {
                UserPrincipal userPrincipal = new UserPrincipal(
                        user.getId(),
                        user.getEmail(),
                        user.getName(),
                        user.getRol(),
                        user.getAgencyId()
                );

                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(userPrincipal, null, userPrincipal.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        filterChain.doFilter(request, response);
    }
}
