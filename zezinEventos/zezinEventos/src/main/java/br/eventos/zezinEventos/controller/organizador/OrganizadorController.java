package br.eventos.zezinEventos.controller;

import br.eventos.zezinEventos.model.Organizador;
import br.eventos.zezinEventos.service.OrganizadorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/organizador")
@PreAuthorize("hasRole('ORGANIZADOR')")
public class OrganizadorController {

    @Autowired
    private OrganizadorService organizadorService;

    @GetMapping("/home")
    public String organizadorHome(Model model, Authentication authentication) {
        // Busca dados do organizador logado
        Organizador organizador = organizadorService.buscarPorLogin(authentication.getName());
        
        model.addAttribute("pageTitle", "Dashboard do Organizador");
        model.addAttribute("organizador", organizador);
        model.addAttribute("totalEventos", 0); 
        model.addAttribute("eventosAtivos", 0);
        model.addAttribute("totalInscricoes", 0);
        model.addAttribute("eventosPendentes", 0);
        
        return "organizador/home";
    }
    
    @GetMapping("/eventos")
    public String meusEventos(Model model, Authentication authentication) {
        Organizador organizador = organizadorService.buscarPorLogin(authentication.getName());
        
        model.addAttribute("pageTitle", "Meus Eventos");
        model.addAttribute("organizador", organizador);
        
        return "organizador/eventos";
    }
    
    @GetMapping("/criar-evento")
    public String criarEvento(Model model, Authentication authentication) {
        Organizador organizador = organizadorService.buscarPorLogin(authentication.getName());
        
        model.addAttribute("pageTitle", "Criar Evento");
        model.addAttribute("organizador", organizador);
        
        return "organizador/criar-evento";
    }
    
    @GetMapping("/relatorios")
    public String relatorios(Model model, Authentication authentication) {
        Organizador organizador = organizadorService.buscarPorLogin(authentication.getName());
        
        model.addAttribute("pageTitle", "Relatórios");
        model.addAttribute("organizador", organizador);
        
        return "organizador/relatorios";
    }
}