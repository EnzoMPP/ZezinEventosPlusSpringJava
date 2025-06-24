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

import java.util.List;

/**
 * Controller responsável pelos relatórios do organizador.
 * Gerencia estatísticas detalhadas e relatórios analíticos.
 */
@Controller
@RequestMapping("/organizador")
@PreAuthorize("hasRole('ORGANIZADOR')")
public class OrganizadorRelatorioController {

    @Autowired
    private OrganizadorRelatorioServiceInterface relatorioService;    /**
     * Exibe a página de relatórios com estatísticas detalhadas.
     */
    @GetMapping("/relatorios")
    public String relatorios(Model model, Authentication authentication) {
        try {
            OrganizadorRelatorioDTO relatorioData = relatorioService.gerarRelatorioCompleto(authentication.getName());
            
            if (relatorioData == null || !relatorioData.temDados()) {
                model.addAttribute("pageTitle", "Relatórios");
                model.addAttribute("eventos", List.of());
                model.addAttribute("totalEventos", 0L);
                model.addAttribute("eventosAtivos", 0L);
                model.addAttribute("eventosFinalizados", 0L);
                model.addAttribute("totalInscricoes", 0L);
                model.addAttribute("totalVagas", 0L);
                model.addAttribute("taxaOcupacao", 0.0);
                model.addAttribute("eventoMaisPopular", null);
                return "organizador/relatorios";
            }
            
            // Calcular eventos ativos e finalizados
            long eventosAtivos = relatorioData.getEventos().stream()
                .mapToLong(evento -> evento.getDataEvento().isAfter(java.time.LocalDateTime.now()) ? 1 : 0)
                .sum();
            
            long eventosFinalizados = relatorioData.getTotalEventos() - eventosAtivos;
            
            // Calcular total de vagas
            long totalVagas = relatorioData.getEventos().stream()
                .mapToLong(evento -> evento.getVagasTotais())
                .sum();
            
            // Enviar dados individuais que o template espera
            model.addAttribute("pageTitle", "Relatórios");
            model.addAttribute("relatorioData", relatorioData);
            model.addAttribute("eventos", relatorioData.getEventos());
            model.addAttribute("totalEventos", relatorioData.getTotalEventos());
            model.addAttribute("eventosAtivos", eventosAtivos);
            model.addAttribute("eventosFinalizados", eventosFinalizados);
            model.addAttribute("totalInscricoes", relatorioData.getTotalInscricoes());
            model.addAttribute("totalVagas", totalVagas);
            model.addAttribute("taxaOcupacao", relatorioData.getTaxaOcupacaoMedia());
            model.addAttribute("eventoMaisPopular", relatorioData.getEventoMaisPopular());
            
            return "organizador/relatorios";
            
        } catch (Exception e) {
            model.addAttribute("error", "Erro ao gerar relatórios: " + e.getMessage());
            model.addAttribute("pageTitle", "Relatórios");
            model.addAttribute("eventos", List.of());
            model.addAttribute("totalEventos", 0L);
            model.addAttribute("eventosAtivos", 0L);
            model.addAttribute("eventosFinalizados", 0L);
            model.addAttribute("totalInscricoes", 0L);
            model.addAttribute("totalVagas", 0L);
            model.addAttribute("taxaOcupacao", 0.0);
            model.addAttribute("eventoMaisPopular", null);
            return "organizador/relatorios";
        }
    }
}
