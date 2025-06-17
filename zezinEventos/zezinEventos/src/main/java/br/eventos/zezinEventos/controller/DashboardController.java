package br.eventos.zezinEventos.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    @GetMapping("/dashboard")
    public String dashboard(Authentication authentication) {
        System.out.println("=== DASHBOARD DEBUG ===");
        System.out.println("Usuario autenticado: " + authentication.getName());
        System.out.println("Authorities: " + authentication.getAuthorities());
        
        // Redireciona baseado no papel do usuário
        if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            System.out.println("Redirecionando para /admin/home");
            return "redirect:/admin/home";
        } else if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ORGANIZADOR"))) {
            System.out.println("Redirecionando para /organizador/home");
            return "redirect:/organizador/home";
        } else if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_CLIENTE"))) {
            System.out.println("Redirecionando para /cliente/home");
            return "redirect:/cliente/home";
        }
        
        System.out.println("Redirecionando para /home (default)");
        return "redirect:/home";
    }
}