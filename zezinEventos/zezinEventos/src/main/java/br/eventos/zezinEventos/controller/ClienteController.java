package br.eventos.zezinEventos.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/cliente")
@PreAuthorize("hasRole('CLIENTE')")
public class ClienteController {

    @GetMapping("/home")
    public String clienteHome(Model model) {
        model.addAttribute("pageTitle", "Meus Eventos");
        return "cliente/home";
    }
}