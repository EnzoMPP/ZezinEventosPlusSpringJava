package br.eventos.zezinEventos.service.interfaces.organizador;

import br.eventos.zezinEventos.model.dto.organizador.OrganizadorRelatorioDTO;

/**
 * Interface para serviços relacionados aos relatórios do organizador.
 * Define operações específicas para geração e visualização de relatórios organizacionais.
 */
public interface OrganizadorRelatorioServiceInterface {
    
    /**
     * Gera relatório completo dos eventos do organizador.
     * 
     * @param loginOrganizador Login do organizador autenticado
     * @return DTO com dados do relatório
     */
    OrganizadorRelatorioDTO gerarRelatorioCompleto(String loginOrganizador);
    
    /**
     * Gera relatório de um período específico.
     * 
     * @param loginOrganizador Login do organizador
     * @param dataInicio Data de início do período (formato yyyy-MM-dd)
     * @param dataFim Data de fim do período (formato yyyy-MM-dd)
     * @return DTO com dados do relatório do período
     */
    OrganizadorRelatorioDTO gerarRelatorioPorPeriodo(String loginOrganizador, 
                                                    String dataInicio, String dataFim);
    
    /**
     * Obtém estatísticas resumidas para o dashboard de relatórios.
     * 
     * @param loginOrganizador Login do organizador
     * @return DTO com estatísticas básicas
     */
    OrganizadorRelatorioDTO obterEstatisticasResumo(String loginOrganizador);
}
