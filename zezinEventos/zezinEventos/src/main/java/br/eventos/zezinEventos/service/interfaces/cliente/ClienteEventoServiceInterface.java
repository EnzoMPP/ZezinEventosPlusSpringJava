package br.eventos.zezinEventos.service.interfaces.cliente;

import br.eventos.zezinEventos.model.dto.cliente.ClienteEventosDTO;

/**
 * Interface para serviços relacionados aos eventos do cliente.
 * Define operações para listagem de eventos disponíveis e gerenciamento de visualizações.
 */
public interface ClienteEventoServiceInterface {
    
    /**
     * Obtém a lista de eventos disponíveis para um cliente específico.
     * 
     * @param loginCliente Login do cliente autenticado
     * @return DTO com eventos disponíveis e status de inscrições
     */
    ClienteEventosDTO obterEventosDisponiveis(String loginCliente);
    
    /**
     * Busca eventos disponíveis com filtros específicos.
     * 
     * @param loginCliente Login do cliente
     * @param filtroNome Filtro por nome do evento (opcional)
     * @param filtroTipo Filtro por tipo do evento (opcional)
     * @return DTO com eventos filtrados
     */
    ClienteEventosDTO buscarEventosComFiltro(String loginCliente, String filtroNome, String filtroTipo);
}
