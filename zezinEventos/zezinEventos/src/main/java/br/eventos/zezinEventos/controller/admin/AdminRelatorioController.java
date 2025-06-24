package br.eventos.zezinEventos.controller.admin;

import br.eventos.zezinEventos.model.dto.admin.RelatorioCompletoDTO;
import br.eventos.zezinEventos.service.interfaces.admin.RelatorioServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Controller responsável pelos relatórios administrativos
 * Seguindo o Princípio da Responsabilidade Única (SRP)
 * Usa injeção de dependência (DIP) para desacoplamento
 */
@Controller
@RequestMapping("/admin/relatorios")
@PreAuthorize("hasRole('ADMIN')")
public class AdminRelatorioController {

    private final RelatorioServiceInterface relatorioService;

    @Autowired
    public AdminRelatorioController(RelatorioServiceInterface relatorioService) {
        this.relatorioService = relatorioService;
    }

    /**
     * Exibe relatório completo (padrão)
     */
    @GetMapping
    public String relatorioCompleto(Model model) {
        return gerarRelatorio(model, "completo");
    }

    /**
     * Relatórios específicos por tipo
     */
    @GetMapping("/tipo")
    public String relatoriosPorTipo(Model model, @RequestParam(defaultValue = "completo") String tipo) {
        return gerarRelatorio(model, tipo);
    }

    /**
     * Relatório de eventos
     */
    @GetMapping("/eventos")
    public String relatorioEventos(Model model) {
        return gerarRelatorio(model, "eventos");
    }

    /**
     * Relatório de usuários
     */
    @GetMapping("/usuarios")
    public String relatorioUsuarios(Model model) {
        return gerarRelatorio(model, "usuarios");
    }

    /**
     * Relatório financeiro
     */
    @GetMapping("/financeiro")
    public String relatorioFinanceiro(Model model) {
        return gerarRelatorio(model, "financeiro");
    }

    // === MÉTODO PRIVADO PARA CENTRALIZAR LÓGICA ===

    private String gerarRelatorio(Model model, String tipo) {
        try {
            RelatorioCompletoDTO relatorio;
            String pageTitle;

            switch (tipo.toLowerCase()) {
                case "eventos":
                    relatorio = relatorioService.gerarRelatorioEventos();
                    pageTitle = "Relatório de Eventos";
                    break;
                case "usuarios":
                    relatorio = relatorioService.gerarRelatorioUsuarios();
                    pageTitle = "Relatório de Usuários";
                    break;
                case "financeiro":
                    relatorio = relatorioService.gerarRelatorioFinanceiro();
                    pageTitle = "Relatório Financeiro";
                    break;
                default:
                    relatorio = relatorioService.gerarRelatorioCompleto();
                    pageTitle = "Relatórios do Sistema";
                    break;
            }

            // Adicionar dados ao modelo usando estrutura organizada
            adicionarDadosAoModelo(model, relatorio, pageTitle, tipo);

            return "admin/relatorios";

        } catch (Exception e) {
            model.addAttribute("error", "Erro ao gerar relatório: " + e.getMessage());
            model.addAttribute("pageTitle", "Erro no Relatório");
            return "admin/relatorios";
        }
    }

    private void adicionarDadosAoModelo(Model model, RelatorioCompletoDTO relatorio, String pageTitle, String tipo) {
        model.addAttribute("pageTitle", pageTitle);
        model.addAttribute("tipoRelatorio", tipo);

        if (relatorio.getEstatisticas() != null) {
            var stats = relatorio.getEstatisticas();
            
            // Estatísticas gerais
            model.addAttribute("totalEventos", stats.getTotalEventos());
            model.addAttribute("totalUsuarios", stats.getTotalUsuarios());
            model.addAttribute("totalClientes", stats.getTotalClientes());
            model.addAttribute("totalOrganizadores", stats.getTotalOrganizadores());
            model.addAttribute("totalAdministradores", stats.getTotalAdministradores());

            // Estatísticas de eventos
            model.addAttribute("eventosAtivos", stats.getEventosAtivos());
            model.addAttribute("eventosInativos", stats.getEventosInativos());
            model.addAttribute("eventosFuturos", stats.getEventosFuturos());
            model.addAttribute("eventosPassados", stats.getEventosPassados());
            model.addAttribute("eventosGratuitos", stats.getEventosGratuitos());
            model.addAttribute("eventosPagos", stats.getEventosPagos());

            // Estatísticas de usuários
            model.addAttribute("clientesAtivos", stats.getClientesAtivos());
            model.addAttribute("clientesInativos", stats.getClientesInativos());
            model.addAttribute("organizadoresAtivos", stats.getOrganizadoresAtivos());
            model.addAttribute("organizadoresInativos", stats.getOrganizadoresInativos());
            model.addAttribute("adminsAtivos", stats.getAdminsAtivos());
            model.addAttribute("adminsInativos", stats.getAdminsInativos());

            // Estatísticas de vagas
            model.addAttribute("totalVagas", stats.getTotalVagas());
            model.addAttribute("totalInscricoes", stats.getTotalInscricoes());
            model.addAttribute("vagasDisponiveis", stats.getVagasDisponiveis());
            model.addAttribute("taxaOcupacaoGeral", stats.getTaxaOcupacaoGeral());

            // Financeiro
            model.addAttribute("receitaTotal", stats.getReceitaTotal());
        }

        // Listas e rankings
        model.addAttribute("top5EventosPopulares", relatorio.getTop5EventosPopulares());
        model.addAttribute("top5OrganizadoresAtivos", relatorio.getTop5OrganizadoresAtivos());
        model.addAttribute("eventosPorTipo", relatorio.getEventosPorTipo());
        model.addAttribute("eventosRecentes", relatorio.getEventosRecentes());
    }
}
