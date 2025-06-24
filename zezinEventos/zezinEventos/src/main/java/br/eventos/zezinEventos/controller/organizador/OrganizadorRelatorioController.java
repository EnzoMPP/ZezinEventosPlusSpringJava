package br.eventos.zezinEventos.controller.organizador;

import br.eventos.zezinEventos.model.dto.organizador.OrganizadorRelatorioDTO;
import br.eventos.zezinEventos.service.interfaces.organizador.OrganizadorRelatorioServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller responsável pelos relatórios do organizador.
 * Gerencia estatísticas detalhadas e relatórios analíticos.
 */
@Controller
@RequestMapping("/organizador")
@PreAuthorize("hasRole('ORGANIZADOR')")
public class OrganizadorRelatorioController {

    @Autowired
    private OrganizadorRelatorioServiceInterface relatorioService;

    /**
     * Exibe a página de relatórios com estatísticas detalhadas.
     */
    @GetMapping("/relatorios")
    public String relatorios(Model model, Authentication authentication) {
        try {
            OrganizadorRelatorioDTO relatorioData = relatorioService.gerarRelatorioCompleto(authentication.getName());
            
            if (relatorioData == null) {
                model.addAttribute("error", "Erro ao gerar relatórios");
                return "organizador/relatorios";
            }
            
            model.addAttribute("pageTitle", "Relatórios");
            model.addAttribute("relatorioData", relatorioData);
            
            return "organizador/relatorios";
            
        } catch (Exception e) {
            model.addAttribute("error", "Erro ao gerar relatórios: " + e.getMessage());
            return "organizador/relatorios";
        }
    }
}
