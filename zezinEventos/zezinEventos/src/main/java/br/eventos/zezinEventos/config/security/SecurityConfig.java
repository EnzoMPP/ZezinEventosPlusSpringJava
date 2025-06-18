package br.eventos.zezinEventos.config.security;

import br.eventos.zezinEventos.config.security.service.UserDetalhesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private UserDetalhesService userDetalhesService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(userDetalhesService)
                .passwordEncoder(passwordEncoder())
                .and().build();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(auth -> {
            auth.requestMatchers("/", "/home", "/login", "/cadastro", "/cadastrar").permitAll()
                .requestMatchers("/css/**", "/js/**", "/script.js", "/img/**").permitAll()
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .requestMatchers("/organizador/**").hasRole("ORGANIZADOR")
                .requestMatchers("/cliente/**").hasRole("CLIENTE")
                .anyRequest().authenticated();
        }).formLogin(form -> {
            form.loginPage("/login")
                .defaultSuccessUrl("/dashboard", true)  
                .failureUrl("/login?error=true")
                .permitAll();
        }).logout(logout -> {
            logout.logoutUrl("/logout") // URL para fazer logout
                .logoutSuccessUrl("/login?logout=true") // Redireciona após logout
                .invalidateHttpSession(true)  // Invalida a sessão
                .deleteCookies("JSESSIONID") // Remove cookies
                .clearAuthentication(true) // Limpa autenticação
                .permitAll();
        });
        
        return http.build();
    }
}