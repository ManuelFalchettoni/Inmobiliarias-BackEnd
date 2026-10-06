package com.manuel.zaguan_inmobiliarias.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

//Hashea las contraseñas de usuarios e inmobiliarias antes de guardarlas. Va aparte de
//SecurityConfig para que el JwtFilter pueda depender de los services sin armar un ciclo
@Configuration
public class PasswordConfig {
    @Bean
    public PasswordEncoder passwordEncoder(){ return new BCryptPasswordEncoder(); }
}
