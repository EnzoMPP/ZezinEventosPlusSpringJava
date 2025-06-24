package br.eventos.zezinEventos.model.dto;

import br.eventos.zezinEventos.model.Evento;
import java.util.List;

/**
 * DTO para transportar dados da listagem de eventos administrativos
 * Encapsula eventos e estatísticas em um único objeto
 */
public class EventosListaDTO {
    private List<Evento> eventos;
    private String busca;
    private Long totalEventos;
    private Long eventosAtivos;
    private Long eventosInativos;
    private Long eventosFuturos;
    private Long eventosPassados;
    private Integer totalVagas;
    private Integer totalOcupadas;
    private Double ocupacaoMedia;

    // Construtor padrão
    public EventosListaDTO() {}

    // Construtor completo
    public EventosListaDTO(List<Evento> eventos, String busca, Long totalEventos,
                          Long eventosAtivos, Long eventosInativos, Long eventosFuturos,
                          Long eventosPassados, Integer totalVagas, Integer totalOcupadas,
                          Double ocupacaoMedia) {
        this.eventos = eventos;
        this.busca = busca;
        this.totalEventos = totalEventos;
        this.eventosAtivos = eventosAtivos;
        this.eventosInativos = eventosInativos;
        this.eventosFuturos = eventosFuturos;
        this.eventosPassados = eventosPassados;
        this.totalVagas = totalVagas;
        this.totalOcupadas = totalOcupadas;
        this.ocupacaoMedia = ocupacaoMedia;
    }

    // Getters e Setters
    public List<Evento> getEventos() {
        return eventos;
    }

    public void setEventos(List<Evento> eventos) {
        this.eventos = eventos;
    }

    public String getBusca() {
        return busca;
    }

    public void setBusca(String busca) {
        this.busca = busca;
    }

    public Long getTotalEventos() {
        return totalEventos;
    }

    public void setTotalEventos(Long totalEventos) {
        this.totalEventos = totalEventos;
    }

    public Long getEventosAtivos() {
        return eventosAtivos;
    }

    public void setEventosAtivos(Long eventosAtivos) {
        this.eventosAtivos = eventosAtivos;
    }

    public Long getEventosInativos() {
        return eventosInativos;
    }

    public void setEventosInativos(Long eventosInativos) {
        this.eventosInativos = eventosInativos;
    }

    public Long getEventosFuturos() {
        return eventosFuturos;
    }

    public void setEventosFuturos(Long eventosFuturos) {
        this.eventosFuturos = eventosFuturos;
    }

    public Long getEventosPassados() {
        return eventosPassados;
    }

    public void setEventosPassados(Long eventosPassados) {
        this.eventosPassados = eventosPassados;
    }

    public Integer getTotalVagas() {
        return totalVagas;
    }

    public void setTotalVagas(Integer totalVagas) {
        this.totalVagas = totalVagas;
    }

    public Integer getTotalOcupadas() {
        return totalOcupadas;
    }

    public void setTotalOcupadas(Integer totalOcupadas) {
        this.totalOcupadas = totalOcupadas;
    }

    public Double getOcupacaoMedia() {
        return ocupacaoMedia;
    }

    public void setOcupacaoMedia(Double ocupacaoMedia) {
        this.ocupacaoMedia = ocupacaoMedia;
    }
}
