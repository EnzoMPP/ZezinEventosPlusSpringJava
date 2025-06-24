package br.eventos.zezinEventos.controller.admin;

import br.eventos.zezinEventos.model.Evento;
import br.eventos.zezinEventos.model.dto.EventosListaDTO;
import br.eventos.zezinEventos.service.interfaces.admin.EventoServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controller responsável pelo gerenciamento administrativo de eventos
 * Seguindo o Princípio da Responsabilidade Única (SRP)
 * Usa injeção de dependência (DIP) para desacoplamento
 */
@Controller
@RequestMapping("/admin/eventos")
@PreAuthorize("hasRole('ADMIN')")
public class AdminEventoController {

    private final EventoServiceInterface eventoService;

    @Autowired
    public AdminEventoController(EventoServiceInterface eventoService) {
        this.eventoService = eventoService;
    }

    /**
     * Lista todos os eventos com estatísticas e busca opcional
     */
    @GetMapping
    public String listarEventos(Model model, @RequestParam(required = false) String busca) {
        try {
            EventosListaDTO eventosData = eventoService.listarEventosComEstatisticas(busca);
            
            model.addAttribute("pageTitle", "Gerenciar Eventos");
            model.addAttribute("eventos", eventosData.getEventos());
            model.addAttribute("busca", eventosData.getBusca());
            model.addAttribute("totalEventos", eventosData.getTotalEventos());
            model.addAttribute("eventosAtivos", eventosData.getEventosAtivos());
            model.addAttribute("eventosInativos", eventosData.getEventosInativos());
            model.addAttribute("eventosFuturos", eventosData.getEventosFuturos());
            model.addAttribute("eventosPassados", eventosData.getEventosPassados());
            model.addAttribute("totalVagas", eventosData.getTotalVagas());
            model.addAttribute("totalOcupadas", eventosData.getTotalOcupadas());
            model.addAttribute("ocupacaoMedia", eventosData.getOcupacaoMedia());
            
            return "admin/eventos";
        } catch (Exception e) {
            model.addAttribute("error", "Erro ao carregar eventos: " + e.getMessage());
            return "admin/eventos";
        }
    }

    /**
     * Ativa um evento
     */
    @PostMapping("/ativar/{id}")
    public String ativarEvento(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            eventoService.ativarEvento(id);
            redirectAttributes.addFlashAttribute("success", "Evento ativado com sucesso!");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erro ao ativar evento: " + e.getMessage());
        }
        return "redirect:/admin/eventos";
    }

    /**
     * Desativa um evento
     */
    @PostMapping("/desativar/{id}")
    public String desativarEvento(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            eventoService.desativarEvento(id);
            redirectAttributes.addFlashAttribute("success", "Evento excluído com sucesso!");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erro ao excluir evento: " + e.getMessage());
        }
        return "redirect:/admin/eventos";
    }

    /**
     * Exibe formulário para editar evento
     */
    @GetMapping("/editar/{id}")
    public String editarEvento(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            Evento evento = eventoService.buscarEventoParaEdicao(id);
            model.addAttribute("evento", evento);
            model.addAttribute("pageTitle", "Editar Evento");
            return "admin/editar-evento";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/admin/eventos";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erro ao carregar evento: " + e.getMessage());
            return "redirect:/admin/eventos";
        }
    }

    /**
     * Salva edição de evento
     */
    @PostMapping("/editar/{id}")
    public String salvarEdicaoEvento(@PathVariable Long id,
                                    @ModelAttribute("evento") Evento evento,
                                    RedirectAttributes redirectAttributes) {
        try {
            eventoService.salvarEdicaoEvento(id, evento);
            redirectAttributes.addFlashAttribute("success", "Evento editado com sucesso!");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erro ao editar evento: " + e.getMessage());
        }
        
        return "redirect:/admin/eventos";
    }
}
