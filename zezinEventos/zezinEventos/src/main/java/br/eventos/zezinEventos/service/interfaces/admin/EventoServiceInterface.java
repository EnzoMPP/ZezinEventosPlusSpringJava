package br.eventos.zezinEventos.service.interfaces.admin;

import br.eventos.zezinEventos.model.Evento;
import br.eventos.zezinEventos.model.dto.admin.EventosListaDTO;

/**
 * Interface para service de gerenciamento de eventos administrativos
 */
public interface EventoServiceInterface {
    
    /**
     * Lista eventos com filtro de busca opcional e estatísticas
     * @param busca termo de busca (opcional)
     * @return DTO com eventos e estatísticas
     */
    EventosListaDTO listarEventosComEstatisticas(String busca);
    
    /**
     * Ativa um evento
     * @param id ID do evento
     * @throws IllegalArgumentException se evento não encontrado
     */
    void ativarEvento(Long id);
    
    /**
     * Desativa um evento
     * @param id ID do evento
     * @throws IllegalArgumentException se evento não encontrado
     */
    void desativarEvento(Long id);
    
    /**
     * Busca evento por ID para edição
     * @param id ID do evento
     * @return evento encontrado
     * @throws IllegalArgumentException se não encontrado
     */
    Evento buscarEventoParaEdicao(Long id);
    
    /**
     * Salva edição de evento feita pelo administrador
     * @param id ID do evento
     * @param evento dados atualizados
     * @throws IllegalArgumentException se dados inválidos
     */
    void salvarEdicaoEvento(Long id, Evento evento);
}
