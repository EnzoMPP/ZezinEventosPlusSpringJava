package br.eventos.zezinEventos.controller;

import br.eventos.zezinEventos.model.Cliente;
import br.eventos.zezinEventos.model.Evento;
import br.eventos.zezinEventos.model.Inscricao;
import br.eventos.zezinEventos.service.ClienteService;
import br.eventos.zezinEventos.service.EventoService;
import br.eventos.zezinEventos.service.InscricaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/cliente")
@PreAuthorize("hasRole('CLIENTE')")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;
    
    @Autowired
    private EventoService eventoService;
    
    @Autowired
    private InscricaoService inscricaoService;

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
    
    @GetMapping("/eventos-disponiveis")
    public String eventosDisponiveis(Model model, Authentication authentication) {
        Cliente cliente = clienteService.buscarPorLogin(authentication.getName());
        List<Evento> eventos = eventoService.listarEventosAbertos();
        
        model.addAttribute("pageTitle", "Eventos Disponíveis");
        model.addAttribute("cliente", cliente);
        model.addAttribute("eventos", eventos);
        
        return "cliente/eventos-disponiveis";
    }
    
    @PostMapping("/inscrever/{eventoId}")
    public String inscreverEvento(@PathVariable Long eventoId, 
                                 Authentication authentication,
                                 RedirectAttributes redirectAttributes) {
        try {
            Cliente cliente = clienteService.buscarPorLogin(authentication.getName());
            Evento evento = eventoService.buscarPorId(eventoId);
            
            inscricaoService.inscrever(cliente, evento);
            redirectAttributes.addFlashAttribute("success", "Inscrição realizada com sucesso!");
            
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        
        return "redirect:/cliente/eventos-disponiveis";
    }
    
    @PostMapping("/cancelar-inscricao/{eventoId}")
    public String cancelarInscricao(@PathVariable Long eventoId,
                                   Authentication authentication,
                                   RedirectAttributes redirectAttributes) {
        try {
            Cliente cliente = clienteService.buscarPorLogin(authentication.getName());
            Evento evento = eventoService.buscarPorId(eventoId);
            
            inscricaoService.cancelarInscricao(cliente, evento);
            redirectAttributes.addFlashAttribute("success", "Inscrição cancelada com sucesso!");
            
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        
        return "redirect:/cliente/eventos";
    }
}