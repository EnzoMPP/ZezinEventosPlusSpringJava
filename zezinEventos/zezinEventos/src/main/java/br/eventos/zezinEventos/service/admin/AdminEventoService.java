package br.eventos.zezinEventos.service.admin;

import br.eventos.zezinEventos.model.Evento;
import br.eventos.zezinEventos.model.dto.EventosListaDTO;
import br.eventos.zezinEventos.service.interfaces.admin.EventoServiceInterface;
import br.eventos.zezinEventos.service.shared.EventoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Service para gerenciamento administrativo de eventos
 * Implementa princípios SOLID: SRP, OCP, DIP
 * Organizado na estrutura admin/ para melhor navegabilidade
 */
@Service
public class AdminEventoService implements EventoServiceInterface {

    private final EventoService eventoService;

    @Autowired
    public AdminEventoService(EventoService eventoService) {
        this.eventoService = eventoService;
    }

    @Override
    public EventosListaDTO listarEventosComEstatisticas(String busca) {
        // Buscar eventos com ou sem filtro
        List<Evento> eventos = buscarEventos(busca);
        
        // Calcular estatísticas
        long totalEventos = eventoService.contarTodos();
        long eventosAtivos = calcularEventosAtivos(eventos);
        long eventosInativos = totalEventos - eventosAtivos;
        long eventosFuturos = calcularEventosFuturos(eventos);
        long eventosPassados = totalEventos - eventosFuturos;
        
        int totalVagas = calcularTotalVagas(eventos);
        int totalOcupadas = calcularTotalOcupadas(eventos);
        double ocupacaoMedia = calcularOcupacaoMedia(totalVagas, totalOcupadas);
        
        return new EventosListaDTO(
            eventos, busca, totalEventos, eventosAtivos, eventosInativos,
            eventosFuturos, eventosPassados, totalVagas, totalOcupadas, ocupacaoMedia
        );
    }

    @Override
    public void ativarEvento(Long id) {
        Evento evento = eventoService.buscarPorId(id);
        if (evento == null) {
            throw new IllegalArgumentException("Evento não encontrado");
        }
        
        evento.setAtivo(true);
        eventoService.salvar(evento);
    }

    @Override
    public void desativarEvento(Long id) {
        Evento evento = eventoService.buscarPorId(id);
        if (evento == null) {
            throw new IllegalArgumentException("Evento não encontrado");
        }
        
        evento.setAtivo(false);
        eventoService.salvar(evento);
    }

    @Override
    public Evento buscarEventoParaEdicao(Long id) {
        Evento evento = eventoService.buscarPorId(id);
        if (evento == null) {
            throw new IllegalArgumentException("Evento não encontrado");
        }
        return evento;
    }

    @Override
    public void salvarEdicaoEvento(Long id, Evento evento) {
        if (eventoService.buscarPorId(id) == null) {
            throw new IllegalArgumentException("Evento não encontrado");
        }
        
        evento.setId(id);
        eventoService.salvarEdicaoAdmin(evento);
    }

    // === MÉTODOS PRIVADOS PARA ENCAPSULAR LÓGICA ===
    
    private List<Evento> buscarEventos(String busca) {
        if (busca != null && !busca.trim().isEmpty()) {
            return eventoService.buscarPorNome(busca);
        }
        return eventoService.listarTodos();
    }

    private long calcularEventosAtivos(List<Evento> eventos) {
        return eventos.stream().filter(Evento::getAtivo).count();
    }

    private long calcularEventosFuturos(List<Evento> eventos) {
        return eventos.stream()
                .filter(e -> e.getDataEvento().isAfter(LocalDateTime.now()))
                .count();
    }

    private int calcularTotalVagas(List<Evento> eventos) {
        return eventos.stream().mapToInt(Evento::getVagasTotais).sum();
    }

    private int calcularTotalOcupadas(List<Evento> eventos) {
        return eventos.stream().mapToInt(Evento::getVagasOcupadas).sum();
    }

    private double calcularOcupacaoMedia(int totalVagas, int totalOcupadas) {
        return totalVagas > 0 ? (totalOcupadas * 100.0) / totalVagas : 0;
    }
}
