package br.eventos.zezinEventos.model.dto;

import br.eventos.zezinEventos.model.Evento;
import java.util.List;
import java.util.Map;

/**
 * DTO para transportar dados completos de relatórios
 * Consolida estatísticas, rankings e listas em um objeto
 */
public class RelatorioCompletoDTO {
    private EstatisticasGeraisDTO estatisticas;
    private List<Map<String, Object>> top5EventosPopulares;
    private List<Map<String, Object>> top5OrganizadoresAtivos;
    private Map<String, Long> eventosPorTipo;
    private List<Evento> eventosRecentes;

    // Construtor padrão
    public RelatorioCompletoDTO() {}

    // Construtor completo
    public RelatorioCompletoDTO(EstatisticasGeraisDTO estatisticas,
                               List<Map<String, Object>> top5EventosPopulares,
                               List<Map<String, Object>> top5OrganizadoresAtivos,
                               Map<String, Long> eventosPorTipo,
                               List<Evento> eventosRecentes) {
        this.estatisticas = estatisticas;
        this.top5EventosPopulares = top5EventosPopulares;
        this.top5OrganizadoresAtivos = top5OrganizadoresAtivos;
        this.eventosPorTipo = eventosPorTipo;
        this.eventosRecentes = eventosRecentes;
    }

    // Getters e Setters
    public EstatisticasGeraisDTO getEstatisticas() {
        return estatisticas;
    }

    public void setEstatisticas(EstatisticasGeraisDTO estatisticas) {
        this.estatisticas = estatisticas;
    }

    public List<Map<String, Object>> getTop5EventosPopulares() {
        return top5EventosPopulares;
    }

    public void setTop5EventosPopulares(List<Map<String, Object>> top5EventosPopulares) {
        this.top5EventosPopulares = top5EventosPopulares;
    }

    public List<Map<String, Object>> getTop5OrganizadoresAtivos() {
        return top5OrganizadoresAtivos;
    }

    public void setTop5OrganizadoresAtivos(List<Map<String, Object>> top5OrganizadoresAtivos) {
        this.top5OrganizadoresAtivos = top5OrganizadoresAtivos;
    }

    public Map<String, Long> getEventosPorTipo() {
        return eventosPorTipo;
    }

    public void setEventosPorTipo(Map<String, Long> eventosPorTipo) {
        this.eventosPorTipo = eventosPorTipo;
    }

    public List<Evento> getEventosRecentes() {
        return eventosRecentes;
    }

    public void setEventosRecentes(List<Evento> eventosRecentes) {
        this.eventosRecentes = eventosRecentes;
    }
}
