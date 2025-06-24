package br.eventos.zezinEventos.controller.cliente;

import br.eventos.zezinEventos.model.dto.cliente.ClienteEventosDTO;
import br.eventos.zezinEventos.service.interfaces.cliente.ClienteEventoServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Controller especializado para eventos do cliente.
 * 
 * Este controller segue o Princípio da Responsabilidade Única (SRP)
 * ao focar exclusivamente na visualização de eventos disponíveis para o cliente.
 */
@Controller
@RequestMapping("/cliente/eventos")
@PreAuthorize("hasRole('CLIENTE')")
public class ClienteEventoController {
    
    private final ClienteEventoServiceInterface eventoService;
    
    @Autowired
    public ClienteEventoController(ClienteEventoServiceInterface eventoService) {
        this.eventoService = eventoService;
    }
    
    /**
     * Exibe a página de eventos disponíveis para inscrição.
     * 
     * @param model Modelo para a view
     * @param authentication Dados de autenticação do usuário
     * @return Nome da view
     */
    @GetMapping("/disponiveis")
    public String exibirEventosDisponiveis(Model model, Authentication authentication) {
        try {
            String loginCliente = authentication.getName();
            ClienteEventosDTO eventosDTO = eventoService.obterEventosDisponiveis(loginCliente);
            
            model.addAttribute("pageTitle", "Eventos Disponíveis");
            model.addAttribute("eventosDTO", eventosDTO);
            model.addAttribute("eventos", eventosDTO.getEventosDisponiveis());
            
            return "cliente/eventos-disponiveis";
            
        } catch (Exception e) {
            return "redirect:/cliente/home?erro=ErroAoCarregarEventos";
        }
    }
    
    /**
     * Busca eventos com filtros específicos.
     * 
     * @param filtroNome Filtro por nome do evento
     * @param filtroTipo Filtro por tipo do evento
     * @param model Modelo para a view
     * @param authentication Dados de autenticação do usuário
     * @return Nome da view
     */
    @GetMapping("/buscar")
    public String buscarEventos(@RequestParam(required = false) String filtroNome,
                               @RequestParam(required = false) String filtroTipo,
                               Model model, 
                               Authentication authentication) {
        try {
            String loginCliente = authentication.getName();
            ClienteEventosDTO eventosDTO = eventoService.buscarEventosComFiltro(
                loginCliente, filtroNome, filtroTipo);
            
            model.addAttribute("pageTitle", "Buscar Eventos");
            model.addAttribute("eventosDTO", eventosDTO);
            model.addAttribute("eventos", eventosDTO.getEventosDisponiveis());
            model.addAttribute("filtroNome", filtroNome);
            model.addAttribute("filtroTipo", filtroTipo);
            
            return "cliente/eventos-disponiveis";
            
        } catch (Exception e) {
            return "redirect:/cliente/home?erro=ErroAoBuscarEventos";
        }
    }
}
