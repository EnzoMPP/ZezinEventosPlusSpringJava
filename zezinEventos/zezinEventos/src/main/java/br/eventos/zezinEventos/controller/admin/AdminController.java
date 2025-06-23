package br.eventos.zezinEventos.controller.admin;

import br.eventos.zezinEventos.model.Evento;
import br.eventos.zezinEventos.service.ClienteService;
import br.eventos.zezinEventos.service.EventoService;
import br.eventos.zezinEventos.service.InscricaoService;
import br.eventos.zezinEventos.service.OrganizadorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    @Autowired
    private EventoService eventoService;
    
    @Autowired
    private ClienteService clienteService;
    
    @Autowired
    private OrganizadorService organizadorService;
    
    @Autowired
    private InscricaoService inscricaoService;

    @GetMapping("/home")
    public String adminHome(Model model) {
        // Buscar todas as estatísticas globais do sistema
        List<Evento> todosEventos = eventoService.listarTodos();
        
        // Calcular estatísticas
        long totalEventos = todosEventos.size();
        long eventosAtivos = todosEventos.stream()
            .filter(e -> e.getDataEvento().isAfter(LocalDateTime.now()))
            .count();
        long eventosFinalizados = totalEventos - eventosAtivos;
        
        long totalClientes = clienteService.contarTodos();
        long totalOrganizadores = organizadorService.contarTodos();
        long totalUsuarios = totalClientes + totalOrganizadores;
        
        long totalInscricoes = todosEventos.stream()
            .mapToLong(evento -> inscricaoService.contarInscricoesPorEvento(evento))
            .sum();
            
        long totalVagas = todosEventos.stream()
            .mapToLong(Evento::getVagasTotais)
            .sum();
            
        double taxaOcupacaoGlobal = totalVagas > 0 ? (totalInscricoes * 100.0) / totalVagas : 0;
        
        // Evento mais popular do sistema
        Evento eventoMaisPopular = todosEventos.stream()
            .max((e1, e2) -> Long.compare(
                inscricaoService.contarInscricoesPorEvento(e1),
                inscricaoService.contarInscricoesPorEvento(e2)
            ))
            .orElse(null);
        
        // Eventos recentes (últimos 5)
        List<Evento> eventosRecentes = todosEventos.stream()
            .sorted((e1, e2) -> e2.getDataCriacao().compareTo(e1.getDataCriacao()))
            .limit(5)
            .toList();
        
        model.addAttribute("pageTitle", "Dashboard Administrativo");
        model.addAttribute("totalEventos", totalEventos);
        model.addAttribute("eventosAtivos", eventosAtivos);
        model.addAttribute("eventosFinalizados", eventosFinalizados);
        model.addAttribute("totalClientes", totalClientes);
        model.addAttribute("totalOrganizadores", totalOrganizadores);
        model.addAttribute("totalUsuarios", totalUsuarios);
        model.addAttribute("totalInscricoes", totalInscricoes);
        model.addAttribute("totalVagas", totalVagas);
        model.addAttribute("taxaOcupacaoGlobal", taxaOcupacaoGlobal);
        model.addAttribute("eventoMaisPopular", eventoMaisPopular);
        model.addAttribute("eventosRecentes", eventosRecentes);
        
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