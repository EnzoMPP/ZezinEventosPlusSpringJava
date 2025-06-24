package br.eventos.zezinEventos.model.dto.organizador;

import br.eventos.zezinEventos.model.Evento;
import java.util.List;
import java.util.Map;

/**
 * DTO para transferência de dados de relatórios do organizador.
 * Encapsula informações e estatísticas para relatórios organizacionais.
 */
public class OrganizadorRelatorioDTO {
    
    private List<Evento> eventos;
    private Long totalEventos;
    private Long totalInscricoes;
    private Double mediaInscricoesPorEvento;
    private Map<String, Long> inscricoesPorEvento;
    private Map<String, Long> eventosPeríodo; // Por mês, por exemplo
    private Evento eventoMaisPopular;
    private Double taxaOcupacaoMedia;
    
    // Construtores
    public OrganizadorRelatorioDTO() {}
    
    public OrganizadorRelatorioDTO(List<Evento> eventos, Long totalInscricoes) {
        this.eventos = eventos;
        this.totalInscricoes = totalInscricoes;
        
        if (eventos != null) {
            this.totalEventos = (long) eventos.size();
            
            // Calcular média de inscrições por evento
            if (totalEventos > 0 && totalInscricoes != null) {
                this.mediaInscricoesPorEvento = totalInscricoes.doubleValue() / totalEventos.doubleValue();
            } else {
                this.mediaInscricoesPorEvento = 0.0;
            }
        } else {
            this.totalEventos = 0L;
            this.mediaInscricoesPorEvento = 0.0;
        }
        
        if (totalInscricoes == null) {
            this.totalInscricoes = 0L;
        }
    }
    
    // Getters e Setters
    public List<Evento> getEventos() {
        return eventos;
    }
    
    public void setEventos(List<Evento> eventos) {
        this.eventos = eventos;
        if (eventos != null) {
            this.totalEventos = (long) eventos.size();
            recalcularMedia();
        }
    }
    
    public Long getTotalEventos() {
        return totalEventos;
    }
    
    public void setTotalEventos(Long totalEventos) {
        this.totalEventos = totalEventos;
        recalcularMedia();
    }
    
    public Long getTotalInscricoes() {
        return totalInscricoes;
    }
    
    public void setTotalInscricoes(Long totalInscricoes) {
        this.totalInscricoes = totalInscricoes;
        recalcularMedia();
    }
    
    public Double getMediaInscricoesPorEvento() {
        return mediaInscricoesPorEvento;
    }
    
    public void setMediaInscricoesPorEvento(Double mediaInscricoesPorEvento) {
        this.mediaInscricoesPorEvento = mediaInscricoesPorEvento;
    }
    
    public Map<String, Long> getInscricoesPorEvento() {
        return inscricoesPorEvento;
    }
    
    public void setInscricoesPorEvento(Map<String, Long> inscricoesPorEvento) {
        this.inscricoesPorEvento = inscricoesPorEvento;
    }
    
    public Map<String, Long> getEventosPeríodo() {
        return eventosPeríodo;
    }
    
    public void setEventosPeríodo(Map<String, Long> eventosPeríodo) {
        this.eventosPeríodo = eventosPeríodo;
    }
    
    public Evento getEventoMaisPopular() {
        return eventoMaisPopular;
    }
    
    public void setEventoMaisPopular(Evento eventoMaisPopular) {
        this.eventoMaisPopular = eventoMaisPopular;
    }
    
    public Double getTaxaOcupacaoMedia() {
        return taxaOcupacaoMedia;
    }
    
    public void setTaxaOcupacaoMedia(Double taxaOcupacaoMedia) {
        this.taxaOcupacaoMedia = taxaOcupacaoMedia;
    }
    
    // Métodos utilitários
    
    /**
     * Verifica se há dados para relatório.
     */
    public boolean temDados() {
        return eventos != null && !eventos.isEmpty();
    }
    
    /**
     * Verifica se há inscrições.
     */
    public boolean temInscricoes() {
        return totalInscricoes != null && totalInscricoes > 0;
    }
    
    /**
     * Recalcula a média de inscrições por evento.
     */
    private void recalcularMedia() {
        if (totalEventos != null && totalEventos > 0 && totalInscricoes != null) {
            this.mediaInscricoesPorEvento = totalInscricoes.doubleValue() / totalEventos.doubleValue();
        } else {
            this.mediaInscricoesPorEvento = 0.0;
        }
    }
}
