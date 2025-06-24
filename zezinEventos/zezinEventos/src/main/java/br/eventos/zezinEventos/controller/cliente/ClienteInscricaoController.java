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
     */    @GetMapping("/historico")
    public String exibirHistorico(Model model, Authentication authentication) {
        try {
            String loginCliente = authentication.getName();
            ClienteInscricoesDTO inscricoesDTO = inscricaoService.obterHistoricoCompleto(loginCliente);
            
            // Calcular eventos próximos e finalizados
            long eventosProximos = 0;
            long eventosFinalizados = 0;
            
            if (inscricoesDTO != null && inscricoesDTO.getHistoricoInscricoes() != null) {
                java.time.LocalDateTime agora = java.time.LocalDateTime.now();
                
                for (Object inscricao : inscricoesDTO.getHistoricoInscricoes()) {
                    try {
                        // Assumindo que é um objeto Inscricao com getEvento().getDataEvento()
                        br.eventos.zezinEventos.model.Inscricao insc = (br.eventos.zezinEventos.model.Inscricao) inscricao;
                        if (insc.getEvento() != null && insc.getEvento().getDataEvento() != null) {
                            if (insc.getEvento().getDataEvento().isAfter(agora)) {
                                eventosProximos++;
                            } else {
                                eventosFinalizados++;
                            }
                        }
                    } catch (Exception ex) {
                        // Se houver erro de cast, ignora e continua
                    }
                }
            }
            
            model.addAttribute("pageTitle", "Histórico de Eventos");
            model.addAttribute("inscricoesDTO", inscricoesDTO);
            model.addAttribute("inscricoes", inscricoesDTO.getHistoricoInscricoes());
            model.addAttribute("eventosProximos", eventosProximos);
            model.addAttribute("eventosFinalizados", eventosFinalizados);
            
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
    }    /**
     * Processa a saída do cliente da lista de espera.
     * 
     * @param eventoId ID do evento
     * @param authentication Dados de autenticação do usuário
     * @param redirectAttributes Atributos para redirect
     * @return Redirecionamento
     */
    @PostMapping("/sair-lista-espera/{eventoId}")
    public String sairDaListaEspera(@PathVariable Long eventoId,
                                   Authentication authentication,
                                   RedirectAttributes redirectAttributes) {
        try {
            String loginCliente = authentication.getName();
            
            // Cast para acessar os métodos adicionais
            if (inscricaoService instanceof br.eventos.zezinEventos.service.cliente.ClienteInscricaoService) {
                br.eventos.zezinEventos.service.cliente.ClienteInscricaoService service = 
                    (br.eventos.zezinEventos.service.cliente.ClienteInscricaoService) inscricaoService;
                
                ResultadoInscricao resultado = service.sairDaListaEspera(loginCliente, eventoId);
                redirectAttributes.addFlashAttribute(resultado.getTipoMensagem(), resultado.getMensagem());
            } else {
                redirectAttributes.addFlashAttribute("error", "Erro interno do sistema.");
            }
            
            return "redirect:/cliente/eventos/disponiveis";
            
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", 
                "Erro inesperado ao sair da lista de espera: " + e.getMessage());
            return "redirect:/cliente/eventos/disponiveis";
        }
    }
    
    /**
     * Endpoint AJAX para consultar posição na lista de espera.
     * 
     * @param eventoId ID do evento
     * @param authentication Dados de autenticação
     * @return Posição na fila (-1 se não estiver na fila)
     */
    @GetMapping("/posicao-lista-espera/{eventoId}")
    @ResponseBody
    public int obterPosicaoListaEspera(@PathVariable Long eventoId, 
                                      Authentication authentication) {
        try {
            String loginCliente = authentication.getName();
            
            if (inscricaoService instanceof br.eventos.zezinEventos.service.cliente.ClienteInscricaoService) {
                br.eventos.zezinEventos.service.cliente.ClienteInscricaoService service = 
                    (br.eventos.zezinEventos.service.cliente.ClienteInscricaoService) inscricaoService;
                
                return service.obterPosicaoNaListaEspera(loginCliente, eventoId);
            }
            
            return -1;
        } catch (Exception e) {
            return -1;
        }
    }
    
    /**
     * Endpoint AJAX para verificar se está na lista de espera.
     * 
     * @param eventoId ID do evento
     * @param authentication Dados de autenticação
     * @return true se estiver na lista de espera
     */
    @GetMapping("/esta-na-lista-espera/{eventoId}")
    @ResponseBody
    public boolean estaListaEspera(@PathVariable Long eventoId, 
                                  Authentication authentication) {
        try {
            String loginCliente = authentication.getName();
            
            if (inscricaoService instanceof br.eventos.zezinEventos.service.cliente.ClienteInscricaoService) {
                br.eventos.zezinEventos.service.cliente.ClienteInscricaoService service = 
                    (br.eventos.zezinEventos.service.cliente.ClienteInscricaoService) inscricaoService;
                
                return service.clienteEstaNaListaEspera(loginCliente, eventoId);
            }
            
            return false;
        } catch (Exception e) {
            return false;
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
    
    /**
     * Redirect para compatibilidade com links antigos.
     */
    @GetMapping("/eventos-disponiveis")
    public String eventosDisponiveisRedirect() {
        return "redirect:/cliente/eventos/disponiveis";
    }
}
