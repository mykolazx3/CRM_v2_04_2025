package com.mykola.crm.config.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .cors(cors -> cors.configurationSource(request -> {
                    CorsConfiguration config = new CorsConfiguration();
                    config.addAllowedOrigin("*"); // Вкажіть точний домен замість "*"
                    config.addAllowedMethod("*"); // Дозволяє всі HTTP методи
                    config.addAllowedHeader("*"); // Дозволяє всі заголовки
                    config.setAllowCredentials(true); // Дозволяє передавати credentials (наприклад, кукі)
                    return config;
                }))
//        У випадку використання JWT для аутентифікації (яка є безстанною і не використовує сесії на сервері), механізм CSRF є зайвим,
//        тому що CSRF атакує зазвичай сайти, де сесія користувача зберігається на сервері через кукі. З JWT ви передаєте токен у заголовках запиту,
//        і якщо зловмисник не може отримати токен, атакувати не можна.
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests((auth -> auth
                        .requestMatchers(
                                "/auth/register", "/auth/login").permitAll()
                        .requestMatchers(
                                "/index", "/index.html").permitAll()
                        .requestMatchers(
                                "/static/**", "/url.js").permitAll() //не обовязково, доступ до /index канає без цього
                        .anyRequest().authenticated()
                ))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }


    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}