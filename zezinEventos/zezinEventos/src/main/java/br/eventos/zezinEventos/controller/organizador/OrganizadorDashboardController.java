package br.eventos.zezinEventos.controller.organizador;

import br.eventos.zezinEventos.model.dto.organizador.OrganizadorDashboardDTO;
import br.eventos.zezinEventos.service.interfaces.organizador.OrganizadorDashboardServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller responsável pelo dashboard do organizador.
 * Gerencia as estatísticas e informações principais do painel de controle.
 */
@Controller
@RequestMapping("/organizador")
@PreAuthorize("hasRole('ORGANIZADOR')")
public class OrganizadorDashboardController {

    @Autowired
    private OrganizadorDashboardServiceInterface dashboardService;

    /**
     * Exibe o dashboard principal do organizador com estatísticas.
     */
    @GetMapping("/home")
    public String organizadorHome(Model model, Authentication authentication) {
        try {
            OrganizadorDashboardDTO dashboardData = dashboardService.obterDashboard(authentication.getName());
            
            if (dashboardData == null) {
                model.addAttribute("error", "Erro ao carregar dados do dashboard");
                return "error/500";
            }
            
            model.addAttribute("pageTitle", "Dashboard do Organizador");
            model.addAttribute("dashboardData", dashboardData);
            
            return "organizador/home";
            
        } catch (Exception e) {
            model.addAttribute("error", "Erro interno: " + e.getMessage());
            return "error/500";
        }
    }
}
