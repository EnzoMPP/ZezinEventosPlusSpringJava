package br.eventos.zezinEventos.model.dto.organizador;

import br.eventos.zezinEventos.model.Evento;
import java.util.List;

/**
 * DTO para transferência de dados de eventos do organizador.
 * Encapsula informações de eventos e estatísticas relacionadas.
 */
public class OrganizadorEventosDTO {
    
    private List<Evento> eventos;
    private String filtroNome;
    private Long totalEventos;
    private Long eventosAtivos;
    private Long eventosFinalizados;
    private Long totalInscricoes;
    
    // Construtores
    public OrganizadorEventosDTO() {}
    
    public OrganizadorEventosDTO(List<Evento> eventos, String filtroNome) {
        this.eventos = eventos;
        this.filtroNome = filtroNome;
        
        if (eventos != null) {
            this.totalEventos = (long) eventos.size();
            
            // Calcular estatísticas básicas
            this.eventosAtivos = eventos.stream()
                .filter(e -> e.getDataEvento() != null && 
                            e.getDataEvento().isAfter(java.time.LocalDateTime.now()))
                .count();
            
            this.eventosFinalizados = this.totalEventos - this.eventosAtivos;
        } else {
            this.totalEventos = 0L;
            this.eventosAtivos = 0L;
            this.eventosFinalizados = 0L;
        }
        
        this.totalInscricoes = 0L; // Será calculado pelo service se necessário
    }
    
    // Getters e Setters
    public List<Evento> getEventos() {
        return eventos;
    }
    
    public void setEventos(List<Evento> eventos) {
        this.eventos = eventos;
        // Recalcular estatísticas quando eventos forem definidos
        if (eventos != null) {
            this.totalEventos = (long) eventos.size();
            this.eventosAtivos = eventos.stream()
                .filter(e -> e.getDataEvento() != null && 
                            e.getDataEvento().isAfter(java.time.LocalDateTime.now()))
                .count();
            this.eventosFinalizados = this.totalEventos - this.eventosAtivos;
        }
    }
    
    public String getFiltroNome() {
        return filtroNome;
    }
    
    public void setFiltroNome(String filtroNome) {
        this.filtroNome = filtroNome;
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
    
    public Long getEventosFinalizados() {
        return eventosFinalizados;
    }
    
    public void setEventosFinalizados(Long eventosFinalizados) {
        this.eventosFinalizados = eventosFinalizados;
    }
    
    public Long getTotalInscricoes() {
        return totalInscricoes;
    }
    
    public void setTotalInscricoes(Long totalInscricoes) {
        this.totalInscricoes = totalInscricoes;
    }
    
    // Métodos utilitários
    
    /**
     * Verifica se há eventos na lista.
     */
    public boolean temEventos() {
        return eventos != null && !eventos.isEmpty();
    }
    
    /**
     * Verifica se há filtro ativo.
     */
    public boolean temFiltro() {
        return filtroNome != null && !filtroNome.trim().isEmpty();
    }
}
