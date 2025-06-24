package br.eventos.zezinEventos.service.interfaces.admin;

import br.eventos.zezinEventos.model.dto.DashboardDTO;

/**
 * Interface para o serviço de Dashboard Administrativo
 * Responsável por fornecer dados estatísticos e indicadores do sistema
 * 
 * Seguindo o princípio da Responsabilidade Única (SRP)
 * Seguindo o princípio da Inversão de Dependência (DIP)
 * Organizada na estrutura admin/ para melhor navegabilidade
 */
public interface DashboardServiceInterface {
    
    /**
     * Obtém todas as estatísticas do dashboard administrativo
     * @return DashboardDTO com todas as estatísticas compiladas
     */
    DashboardDTO obterEstatisticasDashboard();
    
    /**
     * Verifica se o sistema está funcionando corretamente
     * @return true se todos os serviços estão operacionais
     */
    boolean verificarSaudeSistema();
    
    /**
     * Obtém alertas importantes para o administrador
     * @return Lista de alertas do sistema
     */
    java.util.List<String> obterAlertas();
}
