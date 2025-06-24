package br.eventos.zezinEventos.model.dto.cliente;

import br.eventos.zezinEventos.model.Inscricao;
import java.util.List;

/**
 * DTO para transferência de dados de inscrições do cliente.
 * Encapsula informações sobre inscrições ativas, futuras e históricas.
 */
public class ClienteInscricoesDTO {
    
    private List<Inscricao> inscricoesAtivas;
    private List<Inscricao> historicoInscricoes;
    private Long totalInscricoes;
    private Long inscricoesAtivas_count;
    private Long inscricoesFinalizadas;
    
    // Construtores
    public ClienteInscricoesDTO() {}
    
    public ClienteInscricoesDTO(List<Inscricao> inscricoesAtivas, List<Inscricao> historicoInscricoes) {
        this.inscricoesAtivas = inscricoesAtivas;
        this.historicoInscricoes = historicoInscricoes;
        this.inscricoesAtivas_count = (long) (inscricoesAtivas != null ? inscricoesAtivas.size() : 0);
        this.totalInscricoes = (long) (historicoInscricoes != null ? historicoInscricoes.size() : 0);
        
        // Calcular finalizadas (total - ativas)
        this.inscricoesFinalizadas = this.totalInscricoes - this.inscricoesAtivas_count;
        if (this.inscricoesFinalizadas < 0) {
            this.inscricoesFinalizadas = 0L;
        }
    }
    
    // Getters e Setters
    public List<Inscricao> getInscricoesAtivas() {
        return inscricoesAtivas;
    }
    
    public void setInscricoesAtivas(List<Inscricao> inscricoesAtivas) {
        this.inscricoesAtivas = inscricoesAtivas;
        this.inscricoesAtivas_count = (long) (inscricoesAtivas != null ? inscricoesAtivas.size() : 0);
        recalcularFinalizadas();
    }
    
    public List<Inscricao> getHistoricoInscricoes() {
        return historicoInscricoes;
    }
    
    public void setHistoricoInscricoes(List<Inscricao> historicoInscricoes) {
        this.historicoInscricoes = historicoInscricoes;
        this.totalInscricoes = (long) (historicoInscricoes != null ? historicoInscricoes.size() : 0);
        recalcularFinalizadas();
    }
    
    public Long getTotalInscricoes() {
        return totalInscricoes;
    }
    
    public void setTotalInscricoes(Long totalInscricoes) {
        this.totalInscricoes = totalInscricoes;
        recalcularFinalizadas();
    }
    
    public Long getInscricoesAtivas_count() {
        return inscricoesAtivas_count;
    }
    
    public void setInscricoesAtivas_count(Long inscricoesAtivas_count) {
        this.inscricoesAtivas_count = inscricoesAtivas_count;
        recalcularFinalizadas();
    }
    
    public Long getInscricoesFinalizadas() {
        return inscricoesFinalizadas;
    }
    
    public void setInscricoesFinalizadas(Long inscricoesFinalizadas) {
        this.inscricoesFinalizadas = inscricoesFinalizadas;
    }
    
    // Métodos utilitários
    
    /**
     * Verifica se o cliente possui inscrições ativas.
     * 
     * @return true se há inscrições ativas
     */
    public boolean temInscricoesAtivas() {
        return inscricoesAtivas_count != null && inscricoesAtivas_count > 0;
    }
    
    /**
     * Verifica se o cliente possui histórico de inscrições.
     * 
     * @return true se há histórico
     */
    public boolean temHistorico() {
        return totalInscricoes != null && totalInscricoes > 0;
    }
    
    /**
     * Recalcula o número de inscrições finalizadas.
     */
    private void recalcularFinalizadas() {
        if (totalInscricoes != null && inscricoesAtivas_count != null) {
            this.inscricoesFinalizadas = Math.max(0, totalInscricoes - inscricoesAtivas_count);
        }
    }
}
