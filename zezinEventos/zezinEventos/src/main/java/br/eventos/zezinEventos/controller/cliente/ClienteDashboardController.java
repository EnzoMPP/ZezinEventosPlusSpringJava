package br.eventos.zezinEventos.controller.cliente;

import br.eventos.zezinEventos.model.dto.cliente.ClienteDashboardDTO;
import br.eventos.zezinEventos.service.interfaces.cliente.ClienteDashboardServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller especializado para dashboard do cliente.
 * 
 * Este controller segue o Princípio da Responsabilidade Única (SRP)
 * ao focar exclusivamente nas operações de dashboard do cliente.
 */
@Controller
@RequestMapping("/cliente")
@PreAuthorize("hasRole('CLIENTE')")
public class ClienteDashboardController {
    
    private final ClienteDashboardServiceInterface dashboardService;
    
    @Autowired
    public ClienteDashboardController(ClienteDashboardServiceInterface dashboardService) {
        this.dashboardService = dashboardService;
    }
    
    /**
     * Exibe o dashboard/home do cliente com estatísticas.
     * 
     * @param model Modelo para a view
     * @param authentication Dados de autenticação do usuário
     * @return Nome da view
     */
    @GetMapping("/home")
    public String exibirDashboard(Model model, Authentication authentication) {
        try {
            String loginCliente = authentication.getName();
            ClienteDashboardDTO dashboard = dashboardService.obterDashboard(loginCliente);
            
            if (dashboard == null) {
                return "redirect:/login?erro=ClienteNaoEncontrado";
            }
            
            model.addAttribute("pageTitle", "Meus Eventos");
            model.addAttribute("cliente", dashboard);
            model.addAttribute("totalEventosInscritos", dashboard.getTotalEventosInscritos());
            model.addAttribute("eventosProximos", dashboard.getEventosProximos());
            model.addAttribute("eventosFinalizados", dashboard.getEventosFinalizados());
            
            return "cliente/home";
            
        } catch (Exception e) {
            return "redirect:/login?erro=ErroAoCarregarDashboard";
        }
    }
}
