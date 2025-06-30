package br.eventos.zezinEventos.service.interfaces.admin;

import br.eventos.zezinEventos.model.dto.admin.RelatorioCompletoDTO;

/**
 * Interface para service de relatórios administrativos
 */
public interface RelatorioServiceInterface {
    
    /**
     * Gera relatório completo do sistema
     * @return DTO com todas as estatísticas, rankings e listas
     */
    RelatorioCompletoDTO gerarRelatorioCompleto();
    
    /**
     * Gera relatório de desempenho de eventos
     * @return dados específicos sobre eventos
     */
    RelatorioCompletoDTO gerarRelatorioEventos();
    
    /**
     * Gera relatório de usuários
     * @return dados específicos sobre usuários
     */
    RelatorioCompletoDTO gerarRelatorioUsuarios();
    
    /**
     * Gera relatório financeiro
     * @return dados de receita e monetização
     */
    RelatorioCompletoDTO gerarRelatorioFinanceiro();
}
