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
    private OrganizadorDashboardServiceInterface dashboardService;    /**
     * Exibe o dashboard principal do organizador com estatísticas.
     */
    @GetMapping("/home")
    public String organizadorHome(Model model, Authentication authentication) {
        try {
            OrganizadorDashboardDTO dashboardData = dashboardService.obterDashboard(authentication.getName());
            
            if (dashboardData == null) {
                model.addAttribute("error", "Erro ao carregar dados do dashboard");
                // Adicionar dados vazios para evitar null pointer
                model.addAttribute("totalEventos", 0L);
                model.addAttribute("eventosAtivos", 0L);
                model.addAttribute("eventosFinalizados", 0L);
                model.addAttribute("totalInscricoes", 0L);
                return "organizador/home";
            }              model.addAttribute("pageTitle", "Dashboard do Organizador");
            model.addAttribute("dashboardData", dashboardData);
            // Criar um objeto organizador com todas as propriedades para compatibilidade com o template
            model.addAttribute("organizador", new OrganizadorTemplateData(
                dashboardData.getNomeOrganizador(), 
                dashboardData.getEmailOrganizador(),
                dashboardData.getEmpresa(),
                dashboardData.getCnpj(),
                dashboardData.getTelefone()
            ));
            model.addAttribute("totalEventos", dashboardData.getTotalEventos());
            model.addAttribute("eventosAtivos", dashboardData.getEventosAtivos());
            model.addAttribute("eventosFinalizados", dashboardData.getEventosFinalizados());
            model.addAttribute("totalInscricoes", dashboardData.getTotalInscricoes());
            
            return "organizador/home";
            
        } catch (Exception e) {
            model.addAttribute("error", "Erro interno: " + e.getMessage());
            // Adicionar dados vazios para evitar null pointer
            model.addAttribute("totalEventos", 0L);
            model.addAttribute("eventosAtivos", 0L);
            model.addAttribute("eventosFinalizados", 0L);
            model.addAttribute("totalInscricoes", 0L);
            return "organizador/home";
        }    }
      /**
     * Classe auxiliar para compatibilidade com templates que esperam propriedades completas do organizador.
     */
    public static class OrganizadorTemplateData {
        private String nome;
        private String email;
        private String empresa;
        private String cnpj;
        private String telefone;
        
        public OrganizadorTemplateData(String nome, String email, String empresa, String cnpj, String telefone) {
            this.nome = nome;
            this.email = email;
            this.empresa = empresa;
            this.cnpj = cnpj;
            this.telefone = telefone;
        }
        
        public String getNome() {
            return nome;
        }
        
        public String getEmail() {
            return email;
        }
        
        public String getEmpresa() {
            return empresa;
        }
        
        public String getCnpj() {
            return cnpj;
        }
        
        public String getTelefone() {
            return telefone;
        }
    }
}
