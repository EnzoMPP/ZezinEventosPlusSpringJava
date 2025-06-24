package br.eventos.zezinEventos.controller.shared;

import br.eventos.zezinEventos.model.Cliente;
import br.eventos.zezinEventos.model.Evento;
import br.eventos.zezinEventos.model.ListaEspera;
import br.eventos.zezinEventos.service.shared.EventoService;
import br.eventos.zezinEventos.service.shared.InscricaoService;
import br.eventos.zezinEventos.service.shared.ListaEsperaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller para gerenciamento de listas de espera.
 * 
 * Permite que administradores e organizadores visualizem as listas de espera dos eventos.
 */
@Controller
@RequestMapping("/lista-espera")
@PreAuthorize("hasRole('ADMIN') or hasRole('ORGANIZADOR')")
public class ListaEsperaController {    
    private final ListaEsperaService listaEsperaService;
    private final EventoService eventoService;
    private final InscricaoService inscricaoService;
    
    @Autowired
    public ListaEsperaController(ListaEsperaService listaEsperaService, EventoService eventoService, InscricaoService inscricaoService) {
        this.listaEsperaService = listaEsperaService;
        this.eventoService = eventoService;
        this.inscricaoService = inscricaoService;
    }
    
    /**
     * Exibe a lista de espera de um evento específico.
     * 
     * @param eventoId ID do evento
     * @param model Modelo para a view
     * @return Nome da view
     */
    @GetMapping("/evento/{eventoId}")
    public String visualizarListaEspera(@PathVariable Long eventoId, Model model) {
        try {
            Evento evento = eventoService.buscarPorId(eventoId);
            if (evento == null) {
                return "redirect:/admin/eventos?erro=EventoNaoEncontrado";
            }
            
            List<ListaEspera> listaEspera = listaEsperaService.listarFilaCompleta(evento);
            
            model.addAttribute("evento", evento);
            model.addAttribute("listaEspera", listaEspera);
            model.addAttribute("totalNaFila", listaEspera.size());
            model.addAttribute("pageTitle", "Lista de Espera - " + evento.getNome());
            
            return "shared/lista-espera";
            
        } catch (Exception e) {
            return "redirect:/admin/eventos?erro=ErroAoCarregarListaEspera";
        }
    }
    
    /**
     * Endpoint AJAX para obter dados da lista de espera em formato JSON.
     * 
     * @param eventoId ID do evento
     * @return Lista de espera em JSON
     */
    @GetMapping("/evento/{eventoId}/json")
    @ResponseBody
    public List<ListaEspera> obterListaEsperaJson(@PathVariable Long eventoId) {
        try {
            Evento evento = eventoService.buscarPorId(eventoId);
            if (evento == null) {
                return List.of();
            }
            
            return listaEsperaService.listarFilaCompleta(evento);
            
        } catch (Exception e) {
            return List.of();
        }
    }
      /**
     * Promove manualmente o próximo da fila (apenas para admin).
     * 
     * @param eventoId ID do evento
     * @return Redirecionamento
     */
    @PostMapping("/evento/{eventoId}/promover-proximo")
    @PreAuthorize("hasRole('ADMIN')")
    public String promoverProximo(@PathVariable Long eventoId) {
        try {
            Evento evento = eventoService.buscarPorId(eventoId);
            if (evento != null) {
                Cliente proximoCliente = listaEsperaService.promoverProximoDaFila(evento);
                if (proximoCliente != null) {
                    // Realizar a inscrição manualmente
                    inscricaoService.inscrever(proximoCliente, evento);
                    return "redirect:/lista-espera/evento/" + eventoId + "?sucesso=ClientePromovido";
                } else {
                    return "redirect:/lista-espera/evento/" + eventoId + "?erro=FilaVazia";
                }
            }
            
            return "redirect:/lista-espera/evento/" + eventoId + "?erro=EventoNaoEncontrado";
            
        } catch (Exception e) {
            return "redirect:/lista-espera/evento/" + eventoId + "?erro=ErroAoPromover";
        }
    }
}
