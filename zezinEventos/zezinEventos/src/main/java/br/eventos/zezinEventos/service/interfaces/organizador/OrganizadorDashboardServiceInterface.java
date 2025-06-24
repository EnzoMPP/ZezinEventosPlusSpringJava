package br.eventos.zezinEventos.service.interfaces.organizador;

import br.eventos.zezinEventos.model.dto.organizador.OrganizadorDashboardDTO;

/**
 * Interface para serviços relacionados ao dashboard do organizador.
 * Define operações específicas para obtenção de estatísticas e dados do dashboard.
 */
public interface OrganizadorDashboardServiceInterface {
    
    /**
     * Obtém os dados do dashboard para um organizador específico.
     * 
     * @param loginOrganizador Login do organizador autenticado
     * @return DTO com dados do dashboard ou null se organizador não encontrado
     */
    OrganizadorDashboardDTO obterDashboard(String loginOrganizador);
    
    /**
     * Atualiza as estatísticas do dashboard em tempo real.
     * 
     * @param loginOrganizador Login do organizador
     * @return DTO com estatísticas atualizadas
     */
    OrganizadorDashboardDTO atualizarEstatisticas(String loginOrganizador);
}
