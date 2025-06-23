package br.eventos.zezinEventos.controller;

import br.eventos.zezinEventos.model.Evento;
import br.eventos.zezinEventos.model.Organizador;
import br.eventos.zezinEventos.model.TipoEvento;
import br.eventos.zezinEventos.service.EventoService;
import br.eventos.zezinEventos.service.OrganizadorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/organizador")
@PreAuthorize("hasRole('ORGANIZADOR')")
public class OrganizadorController {    @Autowired
    private OrganizadorService organizadorService;
    
    @Autowired
    private EventoService eventoService;

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
        List<Evento> eventos = eventoService.listarPorOrganizador(organizador);
        
        model.addAttribute("pageTitle", "Meus Eventos");
        model.addAttribute("organizador", organizador);
        model.addAttribute("eventos", eventos);
        
        return "organizador/eventos";
    }
    
    @GetMapping("/criar-evento")
    public String criarEvento(Model model, Authentication authentication) {
        Organizador organizador = organizadorService.buscarPorLogin(authentication.getName());
        
        model.addAttribute("pageTitle", "Criar Evento");
        model.addAttribute("organizador", organizador);
        model.addAttribute("evento", new Evento());
        model.addAttribute("tiposEvento", TipoEvento.values());
        
        return "organizador/criar-evento";
    }
    
    @PostMapping("/eventos/salvar")
    public String salvarEvento(@ModelAttribute Evento evento, 
                              Authentication authentication,
                              RedirectAttributes redirectAttributes) {
        try {
            Organizador organizador = organizadorService.buscarPorLogin(authentication.getName());
            evento.setOrganizador(organizador);
            
            eventoService.salvar(evento);
            redirectAttributes.addFlashAttribute("success", "Evento criado com sucesso!");
            
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/organizador/criar-evento";
        }
        
        return "redirect:/organizador/eventos";
    }
    
    @GetMapping("/eventos/editar/{id}")
    public String editarEvento(@PathVariable Long id, Model model, Authentication authentication) {
        try {
            Evento evento = eventoService.buscarPorId(id);
            Organizador organizador = organizadorService.buscarPorLogin(authentication.getName());
            
            // Verifica se o evento pertence ao organizador
            if (!evento.getOrganizador().getId().equals(organizador.getId())) {
                throw new RuntimeException("Você não tem permissão para editar este evento");
            }
            
            model.addAttribute("pageTitle", "Editar Evento");
            model.addAttribute("organizador", organizador);
            model.addAttribute("evento", evento);
            model.addAttribute("tiposEvento", TipoEvento.values());
            
            return "organizador/editar-evento";
            
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "redirect:/organizador/eventos";
        }
    }
    
    @PostMapping("/eventos/excluir/{id}")
    public String excluirEvento(@PathVariable Long id, 
                               Authentication authentication,
                               RedirectAttributes redirectAttributes) {
        try {
            Evento evento = eventoService.buscarPorId(id);
            Organizador organizador = organizadorService.buscarPorLogin(authentication.getName());
            
            // Verifica se o evento pertence ao organizador
            if (!evento.getOrganizador().getId().equals(organizador.getId())) {
                throw new RuntimeException("Você não tem permissão para excluir este evento");
            }
            
            eventoService.excluir(id);
            redirectAttributes.addFlashAttribute("success", "Evento excluído com sucesso!");
            
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        
        return "redirect:/organizador/eventos";
    }
    
    @GetMapping("/relatorios")
    public String relatorios(Model model, Authentication authentication) {
        Organizador organizador = organizadorService.buscarPorLogin(authentication.getName());
        
        model.addAttribute("pageTitle", "Relatórios");
        model.addAttribute("organizador", organizador);
        
        return "organizador/relatorios";
    }
}