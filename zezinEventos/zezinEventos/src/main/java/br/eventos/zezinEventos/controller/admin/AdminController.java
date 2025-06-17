package br.eventos.zezinEventos.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    @GetMapping("/home")
    public String adminHome(Model model) {
        model.addAttribute("pageTitle", "Dashboard Administrativo");
        return "admin/home";
    }
    
    @GetMapping("/usuarios")
    public String usuarios(Model model) {
        model.addAttribute("pageTitle", "Gerenciar Usuários");
        return "admin/usuarios";
    }
    
    @GetMapping("/eventos")
    public String eventos(Model model) {
        model.addAttribute("pageTitle", "Gerenciar Eventos");
        return "admin/eventos";
    }
}