package br.eventos.zezinEventos.service.interfaces.organizador;

import br.eventos.zezinEventos.model.Evento;
import br.eventos.zezinEventos.model.dto.organizador.OrganizadorEventosDTO;

/**
 * Interface para serviços relacionados ao gerenciamento de eventos do organizador.
 * Define operações específicas para CRUD de eventos e listagens.
 */
public interface OrganizadorEventoServiceInterface {
    
    /**
     * Obtém a lista de eventos de um organizador específico.
     * 
     * @param loginOrganizador Login do organizador autenticado
     * @return DTO com eventos do organizador
     */
    OrganizadorEventosDTO obterEventosDoOrganizador(String loginOrganizador);
    
    /**
     * Busca eventos do organizador com filtros específicos.
     * 
     * @param loginOrganizador Login do organizador
     * @param filtroNome Filtro por nome do evento (opcional)
     * @return DTO com eventos filtrados
     */
    OrganizadorEventosDTO buscarEventosComFiltro(String loginOrganizador, String filtroNome);
    
    /**
     * Prepara um novo evento para criação.
     * 
     * @param loginOrganizador Login do organizador
     * @return Evento vazio associado ao organizador
     */
    Evento prepararNovoEvento(String loginOrganizador);    /**
     * Salva um evento (criação ou edição).
     * 
     * @param evento Evento a ser salvo
     * @param loginOrganizador Login do organizador (para validação)
     * @return Evento salvo
     */
    Evento salvarEvento(Evento evento, String loginOrganizador);
    
    /**
     * Busca um evento específico do organizador para edição.
     * 
     * @param eventoId ID do evento
     * @param loginOrganizador Login do organizador (para validação de propriedade)
     * @return Evento encontrado
     */
    Evento buscarEventoParaEdicao(Long eventoId, String loginOrganizador);
    
    /**
     * Exclui um evento do organizador.
     * 
     * @param eventoId ID do evento
     * @param loginOrganizador Login do organizador (para validação de propriedade)
     */
    void excluirEvento(Long eventoId, String loginOrganizador);
}
