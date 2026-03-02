package com.tienda.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
            .authorizeHttpRequests(auth -> auth

                /* ============================================================
                   RUTAS PÚBLICAS
                   ============================================================ */
                .requestMatchers(
                        "/", 
                        "/catalog",
                        "/auth/login",
                        "/auth/register",
                        "/css/**",
                        "/js/**",
                        "/images/**",
                        "/webjars/**"
                ).permitAll()

                /* ============================================================
                   RUTAS SOLO PARA COMPRADORES
                   ============================================================ */
                .requestMatchers(
                        "/cart/**",
                        "/checkout/**",
                        "/purchases/**",
                        "/edit"
                ).hasRole("COMPRADOR")

                /* ============================================================
                   RUTAS SOLO PARA ADMINISTRADORES
                   ============================================================ */
                .requestMatchers("/admin/**").hasRole("ADMIN")

                /* ============================================================
                   CUALQUIER OTRA RUTA → requiere login
                   ============================================================ */
                .anyRequest().authenticated()
            )

            /* ============================================================
               LOGIN PERSONALIZADO
               ============================================================ */
            .formLogin(form -> form
                    .loginPage("/auth/login")     // <-- ESTA ES LA CLAVE
                    .loginProcessingUrl("/login") // <-- Spring procesa POST /login
                    .defaultSuccessUrl("/", true)
                    .permitAll()
            )

            /* ============================================================
               LOGOUT
               ============================================================ */
            .logout(logout -> logout
                    .logoutUrl("/logout")
                    .logoutSuccessUrl("/")
            );

        return http.build();
    }
}