package br.eventos.zezinEventos.model.dto.cliente;

import br.eventos.zezinEventos.model.Evento;
import br.eventos.zezinEventos.model.Inscricao;
import java.util.List;

/**
 * DTO para transferência de dados de eventos disponíveis para o cliente.
 * Encapsula informações de eventos e status de inscrição do cliente.
 */
public class ClienteEventosDTO {
    
    private List<Evento> eventosDisponiveis;
    private List<Inscricao> inscricoesCliente;
    private Long totalEventosDisponiveis;
    private Long totalEventosInscritos;
    
    // Construtores
    public ClienteEventosDTO() {}
    
    public ClienteEventosDTO(List<Evento> eventosDisponiveis, List<Inscricao> inscricoesCliente) {
        this.eventosDisponiveis = eventosDisponiveis;
        this.inscricoesCliente = inscricoesCliente;
        this.totalEventosDisponiveis = (long) (eventosDisponiveis != null ? eventosDisponiveis.size() : 0);
        this.totalEventosInscritos = (long) (inscricoesCliente != null ? inscricoesCliente.size() : 0);
    }
    
    // Getters e Setters
    public List<Evento> getEventosDisponiveis() {
        return eventosDisponiveis;
    }
    
    public void setEventosDisponiveis(List<Evento> eventosDisponiveis) {
        this.eventosDisponiveis = eventosDisponiveis;
        this.totalEventosDisponiveis = (long) (eventosDisponiveis != null ? eventosDisponiveis.size() : 0);
    }
    
    public List<Inscricao> getInscricoesCliente() {
        return inscricoesCliente;
    }
    
    public void setInscricoesCliente(List<Inscricao> inscricoesCliente) {
        this.inscricoesCliente = inscricoesCliente;
        this.totalEventosInscritos = (long) (inscricoesCliente != null ? inscricoesCliente.size() : 0);
    }
    
    public Long getTotalEventosDisponiveis() {
        return totalEventosDisponiveis;
    }
    
    public void setTotalEventosDisponiveis(Long totalEventosDisponiveis) {
        this.totalEventosDisponiveis = totalEventosDisponiveis;
    }
    
    public Long getTotalEventosInscritos() {
        return totalEventosInscritos;
    }
    
    public void setTotalEventosInscritos(Long totalEventosInscritos) {
        this.totalEventosInscritos = totalEventosInscritos;
    }
    
    // Métodos utilitários
    
    /**
     * Verifica se o cliente já está inscrito em um evento específico.
     * 
     * @param eventoId ID do evento
     * @return true se o cliente está inscrito
     */
    public boolean clienteInscritoEm(Long eventoId) {
        if (inscricoesCliente == null || eventoId == null) {
            return false;
        }
        
        return inscricoesCliente.stream()
                .anyMatch(inscricao -> inscricao.getEvento().getId().equals(eventoId));
    }
    
    /**
     * Verifica se há eventos disponíveis para inscrição.
     * 
     * @return true se há eventos disponíveis
     */
    public boolean temEventosDisponiveis() {
        return eventosDisponiveis != null && !eventosDisponiveis.isEmpty();
    }
}
