package br.eventos.zezinEventos.service.interfaces.cliente;

import br.eventos.zezinEventos.model.dto.cliente.ClienteDashboardDTO;

/**
 * Interface para serviços relacionados ao dashboard do cliente.
 * Define operações específicas para obtenção de estatísticas e dados do dashboard.
 */
public interface ClienteDashboardServiceInterface {
    
    /**
     * Obtém os dados do dashboard para um cliente específico.
     * 
     * @param loginCliente Login do cliente autenticado
     * @return DTO com dados do dashboard ou null se cliente não encontrado
     */
    ClienteDashboardDTO obterDashboard(String loginCliente);
    
    /**
     * Atualiza as estatísticas do dashboard em tempo real.
     * 
     * @param loginCliente Login do cliente
     * @return DTO com estatísticas atualizadas
     */
    ClienteDashboardDTO atualizarEstatisticas(String loginCliente);
}
