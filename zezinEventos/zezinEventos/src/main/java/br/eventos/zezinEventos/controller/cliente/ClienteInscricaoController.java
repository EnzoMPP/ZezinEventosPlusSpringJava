package br.eventos.zezinEventos.controller.cliente;

import br.eventos.zezinEventos.model.dto.cliente.ClienteInscricoesDTO;
import br.eventos.zezinEventos.service.interfaces.cliente.ClienteInscricaoServiceInterface;
import br.eventos.zezinEventos.service.interfaces.cliente.ClienteInscricaoServiceInterface.ResultadoInscricao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controller especializado para inscrições do cliente.
 * 
 * Este controller segue o Princípio da Responsabilidade Única (SRP)
 * ao focar exclusivamente no gerenciamento de inscrições do cliente.
 */
@Controller
@RequestMapping("/cliente")
@PreAuthorize("hasRole('CLIENTE')")
public class ClienteInscricaoController {
    
    private final ClienteInscricaoServiceInterface inscricaoService;
    
    @Autowired
    public ClienteInscricaoController(ClienteInscricaoServiceInterface inscricaoService) {
        this.inscricaoService = inscricaoService;
    }
    
    /**
     * Exibe os eventos ativos do cliente (inscrições ativas).
     * 
     * @param model Modelo para a view
     * @param authentication Dados de autenticação do usuário
     * @return Nome da view
     */
    @GetMapping("/eventos")
    public String exibirEventosAtivos(Model model, Authentication authentication) {
        try {
            String loginCliente = authentication.getName();
            ClienteInscricoesDTO inscricoesDTO = inscricaoService.obterInscricoesAtivas(loginCliente);
            
            model.addAttribute("pageTitle", "Meus Eventos Ativos");
            model.addAttribute("inscricoesDTO", inscricoesDTO);
            model.addAttribute("inscricoes", inscricoesDTO.getInscricoesAtivas());
            
            return "cliente/eventos";
            
        } catch (Exception e) {
            return "redirect:/cliente/home?erro=ErroAoCarregarEventosAtivos";
        }
    }
    
    /**
     * Exibe o histórico completo de inscrições do cliente.
     * 
     * @param model Modelo para a view
     * @param authentication Dados de autenticação do usuário
     * @return Nome da view
     */
    @GetMapping("/historico")
    public String exibirHistorico(Model model, Authentication authentication) {
        try {
            String loginCliente = authentication.getName();
            ClienteInscricoesDTO inscricoesDTO = inscricaoService.obterHistoricoCompleto(loginCliente);
            
            model.addAttribute("pageTitle", "Histórico de Eventos");
            model.addAttribute("inscricoesDTO", inscricoesDTO);
            model.addAttribute("inscricoes", inscricoesDTO.getHistoricoInscricoes());
            
            return "cliente/historico";
            
        } catch (Exception e) {
            return "redirect:/cliente/home?erro=ErroAoCarregarHistorico";
        }
    }
    
    /**
     * Processa a inscrição do cliente em um evento.
     * 
     * @param eventoId ID do evento
     * @param authentication Dados de autenticação do usuário
     * @param redirectAttributes Atributos para redirect
     * @return Redirecionamento
     */
    @PostMapping("/inscrever/{eventoId}")
    public String inscreverEmEvento(@PathVariable Long eventoId,
                                   Authentication authentication,
                                   RedirectAttributes redirectAttributes) {
        try {
            String loginCliente = authentication.getName();
            ResultadoInscricao resultado = inscricaoService.inscreverEmEvento(loginCliente, eventoId);
            
            // Adicionar mensagem baseada no resultado
            redirectAttributes.addFlashAttribute(resultado.getTipoMensagem(), resultado.getMensagem());
            
            return "redirect:/cliente/eventos/disponiveis";
            
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", 
                "Erro inesperado ao realizar inscrição: " + e.getMessage());
            return "redirect:/cliente/eventos/disponiveis";
        }
    }
    
    /**
     * Processa o cancelamento de inscrição do cliente.
     * 
     * @param eventoId ID do evento
     * @param authentication Dados de autenticação do usuário
     * @param redirectAttributes Atributos para redirect
     * @return Redirecionamento
     */
    @PostMapping("/cancelar-inscricao/{eventoId}")
    public String cancelarInscricao(@PathVariable Long eventoId,
                                   Authentication authentication,
                                   RedirectAttributes redirectAttributes) {
        try {
            String loginCliente = authentication.getName();
            ResultadoInscricao resultado = inscricaoService.cancelarInscricao(loginCliente, eventoId);
            
            // Adicionar mensagem baseada no resultado
            redirectAttributes.addFlashAttribute(resultado.getTipoMensagem(), resultado.getMensagem());
            
            return "redirect:/cliente/eventos";
            
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", 
                "Erro inesperado ao cancelar inscrição: " + e.getMessage());
            return "redirect:/cliente/eventos";
        }
    }
    
    /**
     * Endpoint AJAX para verificar se o cliente pode se inscrever em um evento.
     * 
     * @param eventoId ID do evento
     * @param authentication Dados de autenticação
     * @return Resposta JSON com disponibilidade
     */
    @GetMapping("/pode-inscrever/{eventoId}")
    @ResponseBody
    public boolean podeSeInscrever(@PathVariable Long eventoId, 
                                  Authentication authentication) {
        try {
            String loginCliente = authentication.getName();
            return inscricaoService.podeSeInscrever(loginCliente, eventoId);
        } catch (Exception e) {
            return false; // Em caso de erro, considerar que não pode se inscrever
        }
    }
}
