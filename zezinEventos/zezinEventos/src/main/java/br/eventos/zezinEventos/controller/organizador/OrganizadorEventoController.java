package br.eventos.zezinEventos.controller.organizador;

import br.eventos.zezinEventos.model.Evento;
import br.eventos.zezinEventos.model.dto.organizador.OrganizadorEventosDTO;
import br.eventos.zezinEventos.model.enums.TipoEvento;
import br.eventos.zezinEventos.service.interfaces.organizador.OrganizadorEventoServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controller responsável pelo gerenciamento de eventos do organizador.
 * Gerencia CRUD de eventos, listagens e operações relacionadas.
 */
@Controller
@RequestMapping("/organizador")
@PreAuthorize("hasRole('ORGANIZADOR')")
public class OrganizadorEventoController {

    @Autowired
    private OrganizadorEventoServiceInterface eventoService;

    /**
     * Lista todos os eventos do organizador.
     */
    @GetMapping("/eventos")
    public String meusEventos(Model model, Authentication authentication) {
        try {
            OrganizadorEventosDTO eventosData = eventoService.obterEventosDoOrganizador(authentication.getName());
            
            if (eventosData == null) {
                model.addAttribute("error", "Erro ao carregar eventos");
                return "organizador/eventos";
            }
            
            model.addAttribute("pageTitle", "Meus Eventos");
            model.addAttribute("eventosData", eventosData);
            
            return "organizador/eventos";
            
        } catch (Exception e) {
            model.addAttribute("error", "Erro ao carregar eventos: " + e.getMessage());
            return "organizador/eventos";
        }
    }

    /**
     * Exibe o formulário para criação de um novo evento.
     */
    @GetMapping("/criar-evento")
    public String criarEvento(Model model, Authentication authentication) {
        try {
            Evento novoEvento = eventoService.prepararNovoEvento(authentication.getName());
            
            model.addAttribute("pageTitle", "Criar Evento");
            model.addAttribute("evento", novoEvento);
            model.addAttribute("tiposEvento", TipoEvento.values());
            
            return "organizador/criar-evento";
            
        } catch (Exception e) {
            model.addAttribute("error", "Erro ao preparar formulário: " + e.getMessage());
            return "redirect:/organizador/eventos";
        }
    }

    /**
     * Exibe o formulário para edição de um evento existente.
     */
    @GetMapping("/eventos/editar/{id}")
    public String editarEvento(@PathVariable Long id, Model model, Authentication authentication) {
        try {
            Evento evento = eventoService.buscarEventoParaEdicao(id, authentication.getName());
            
            model.addAttribute("pageTitle", "Editar Evento");
            model.addAttribute("evento", evento);
            model.addAttribute("tiposEvento", TipoEvento.values());
            
            return "organizador/editar-evento";
            
        } catch (Exception e) {
            model.addAttribute("error", "Erro ao carregar evento: " + e.getMessage());
            return "redirect:/organizador/eventos";
        }
    }

    /**
     * Processa o salvamento de um evento (criação ou edição).
     */
    @PostMapping("/eventos/salvar")
    public String salvarEvento(@ModelAttribute Evento evento, 
                              Authentication authentication,
                              RedirectAttributes redirectAttributes,
                              Model model) {
        try {
            eventoService.salvarEvento(evento, authentication.getName());
            
            String mensagem = evento.getId() == null ? 
                "Evento criado com sucesso!" : 
                "Evento atualizado com sucesso!";
            
            redirectAttributes.addFlashAttribute("success", mensagem);
            return "redirect:/organizador/eventos";
            
        } catch (Exception e) {
            // Prepara dados para retornar ao formulário
            model.addAttribute("evento", evento);
            model.addAttribute("tiposEvento", TipoEvento.values());
            model.addAttribute("error", e.getMessage());
            
            // Retorna o template correto baseado se é criação ou edição
            if (evento.getId() != null) {
                model.addAttribute("pageTitle", "Editar Evento");
                return "organizador/editar-evento";
            } else {
                model.addAttribute("pageTitle", "Criar Evento");
                return "organizador/criar-evento";
            }
        }
    }

    /**
     * Exclui um evento.
     */
    @PostMapping("/eventos/excluir/{id}")
    public String excluirEvento(@PathVariable Long id, 
                               Authentication authentication,
                               RedirectAttributes redirectAttributes) {
        try {
            eventoService.excluirEvento(id, authentication.getName());
            redirectAttributes.addFlashAttribute("success", "Evento excluído com sucesso!");
            
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        
        return "redirect:/organizador/eventos";
    }
}
