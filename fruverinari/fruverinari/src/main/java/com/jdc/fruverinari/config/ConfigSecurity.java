 package com.jdc.fruverinari.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class ConfigSecurity {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/Login", "/registro", "/css/**", "/js/**", "/images/**", "/h2-console/**").permitAll()
                        .requestMatchers("/productos/**", "/proveedores/**").hasRole("ADMIN")
                        .requestMatchers("/alertas", "/alertas/**").hasAnyRole("ADMIN", "CAJERO")
                        .requestMatchers("/consultarInventario", "/cajero/**").hasAnyRole("ADMIN", "CAJERO")
                        .requestMatchers("/movimientos/**", "/historial/**").hasAnyRole("ADMIN", "CAJERO")
                        .requestMatchers("/nutricion", "/nutricion/**").hasAnyRole("ADMIN", "CAJERO")
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/Login")
                        .loginProcessingUrl("/Login")
                        .successHandler((request, response, authentication) -> {
                            String role = authentication.getAuthorities()
                                    .iterator().next().getAuthority();

                            if (role.equals("ROLE_ADMIN")) {
                                response.sendRedirect("/productos");
                            } else if (role.equals("ROLE_CAJERO")) {
                                response.sendRedirect("/cajero/consultarInventario");
                            } else {
                                response.sendRedirect("/Login");
                            }
                        })
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/Login?logout")
                        .permitAll()
                )
                .csrf(csrf -> csrf.disable())
                .headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()));

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
