package br.eventos.zezinEventos.controller.admin;

import br.eventos.zezinEventos.model.dto.admin.DashboardDTO;
import br.eventos.zezinEventos.service.admin.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller responsável pelo Dashboard Administrativo
 * 
 * Responsabilidades:
 * - Exibir página inicial do admin
 * - Fornecer estatísticas do sistema
 * - Mostrar alertas importantes
 */
@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminDashboardController {
    
    private final DashboardService dashboardService;
    
    /**
     * Construtor com injeção de dependência
     */
    @Autowired
    public AdminDashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }
    
    /**
     * Exibe o dashboard administrativo principal
     * 
     * @param model Model para passar dados para a view
     * @return nome da view do dashboard
     */
    @GetMapping({"/", "/home", "/dashboard"})
    public String adminHome(Model model) {
        try {
            // Obter dados do dashboard via service
            DashboardDTO dashboard = dashboardService.obterEstatisticasDashboard();
            
            // Verificar saúde do sistema
            boolean sistemaOperacional = dashboardService.verificarSaudeSistema();
            
            // Adicionar dados ao modelo para a view
            adicionarDadosAoModelo(model, dashboard, sistemaOperacional);
            
            return "admin/home";
            
        } catch (Exception e) {
            // Em caso de erro, adicionar informação de erro ao modelo
            model.addAttribute("erro", "Erro ao carregar dashboard: " + e.getMessage());
            model.addAttribute("pageTitle", "Dashboard Administrativo - Erro");
            return "admin/home";
        }
    }
    
    /**
     * Endpoint específico para verificar saúde do sistema
     * Pode ser usado para monitoring/health checks
     * 
     * @param model Model para resposta
     * @return view de status do sistema
     */
    @GetMapping("/status")
    public String statusSistema(Model model) {
        boolean sistemaOperacional = dashboardService.verificarSaudeSistema();
        model.addAttribute("sistemaOperacional", sistemaOperacional);
        model.addAttribute("timestamp", java.time.LocalDateTime.now());
        return "admin/status";
    }
    
    // === MÉTODOS PRIVADOS PARA ORGANIZAÇÃO ===
    
    /**
     * Adiciona todos os dados necessários ao modelo da view
     * 
     * @param model Model do Spring
     * @param dashboard DTO com dados do dashboard
     * @param sistemaOperacional status de saúde do sistema
     */
    private void adicionarDadosAoModelo(Model model, DashboardDTO dashboard, boolean sistemaOperacional) {
        // Título da página
        model.addAttribute("pageTitle", "Dashboard Administrativo");
        
        // Estatísticas gerais
        model.addAttribute("totalEventos", dashboard.getTotalEventos());
        model.addAttribute("totalUsuarios", dashboard.getTotalUsuarios());
        model.addAttribute("totalClientes", dashboard.getTotalClientes());
        model.addAttribute("totalOrganizadores", dashboard.getTotalOrganizadores());
        model.addAttribute("totalAdministradores", dashboard.getTotalAdministradores());
        
        // Estatísticas de eventos
        model.addAttribute("eventosAtivos", dashboard.getEventosAtivos());
        model.addAttribute("eventosFinalizados", dashboard.getEventosFinalizados());
        model.addAttribute("totalInscricoes", dashboard.getTotalInscricoes());
        model.addAttribute("totalVagas", dashboard.getTotalVagas());
        model.addAttribute("taxaOcupacaoGlobal", dashboard.getTaxaOcupacaoGlobal());
        
        // Dados especiais
        model.addAttribute("eventoMaisPopular", dashboard.getEventoMaisPopular());
        model.addAttribute("eventosRecentes", dashboard.getEventosRecentes());
        model.addAttribute("alertas", dashboard.getAlertas());
        
        // Status do sistema
        model.addAttribute("sistemaOperacional", sistemaOperacional);
    }
}
