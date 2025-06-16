package br.eventos.zezinEventos.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

     @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> {
                auth.requestMatchers("/", "/home", "/login", "/cadastro", "/css/**", "/js/**").permitAll();
                auth.anyRequest().permitAll(); // Temporário para desenvolvimento
            })
            .formLogin(form -> {
                form.loginPage("/login");
                form.defaultSuccessUrl("/home");
                form.permitAll();
            })
            .logout(logout -> {
                logout.permitAll();
            });
            
        return http.build();
    }

}
