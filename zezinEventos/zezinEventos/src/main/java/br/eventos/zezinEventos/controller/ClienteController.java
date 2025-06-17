package br.eventos.zezinEventos.controller;

import br.eventos.zezinEventos.model.Cliente;
import br.eventos.zezinEventos.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/cliente")
@PreAuthorize("hasRole('CLIENTE')")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @GetMapping("/home")
    public String clienteHome(Model model, Authentication authentication) {
        // Busca dados do cliente logado
        Cliente cliente = clienteService.buscarPorLogin(authentication.getName());
        
        model.addAttribute("pageTitle", "Meus Eventos");
        model.addAttribute("cliente", cliente);
        model.addAttribute("totalEventosInscritos", 0); // Por enquanto hardcoded
        model.addAttribute("eventosProximos", 0);
        model.addAttribute("eventosFinalizados", 0);
        
        return "cliente/home";
    }
    
    @GetMapping("/eventos")
    public String meusEventos(Model model, Authentication authentication) {
        Cliente cliente = clienteService.buscarPorLogin(authentication.getName());
        
        model.addAttribute("pageTitle", "Meus Eventos");
        model.addAttribute("cliente", cliente);
        
        return "cliente/eventos";
    }
    
    @GetMapping("/perfil")
    public String perfil(Model model, Authentication authentication) {
        Cliente cliente = clienteService.buscarPorLogin(authentication.getName());
        
        model.addAttribute("pageTitle", "Meu Perfil");
        model.addAttribute("cliente", cliente);
        
        return "cliente/perfil";
    }
}