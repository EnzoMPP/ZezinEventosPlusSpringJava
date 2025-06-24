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
    Evento prepararNovoEvento(String loginOrganizador);
    
    /**
     * Salva um evento (criação ou edição).
     * 
     * @param evento Evento a ser salvo
     * @param loginOrganizador Login do organizador (para validação)
     * @return Resultado da operação
     */
    ResultadoOperacao salvarEvento(Evento evento, String loginOrganizador);
    
    /**
     * Busca um evento específico do organizador para edição.
     * 
     * @param eventoId ID do evento
     * @param loginOrganizador Login do organizador (para validação de propriedade)
     * @return Evento encontrado ou null
     */
    Evento buscarEventoParaEdicao(Long eventoId, String loginOrganizador);
    
    /**
     * Exclui um evento do organizador.
     * 
     * @param eventoId ID do evento
     * @param loginOrganizador Login do organizador (para validação de propriedade)
     * @return Resultado da operação
     */
    ResultadoOperacao excluirEvento(Long eventoId, String loginOrganizador);
    
    /**
     * Classe para encapsular o resultado de operações.
     */
    class ResultadoOperacao {
        private boolean sucesso;
        private String mensagem;
        private String tipoMensagem; // "success", "error", "warning"
        
        public ResultadoOperacao(boolean sucesso, String mensagem, String tipoMensagem) {
            this.sucesso = sucesso;
            this.mensagem = mensagem;
            this.tipoMensagem = tipoMensagem;
        }
        
        // Getters
        public boolean isSucesso() { return sucesso; }
        public String getMensagem() { return mensagem; }
        public String getTipoMensagem() { return tipoMensagem; }
        
        // Métodos estáticos para facilitar criação
        public static ResultadoOperacao sucesso(String mensagem) {
            return new ResultadoOperacao(true, mensagem, "success");
        }
        
        public static ResultadoOperacao erro(String mensagem) {
            return new ResultadoOperacao(false, mensagem, "error");
        }
        
        public static ResultadoOperacao aviso(String mensagem) {
            return new ResultadoOperacao(false, mensagem, "warning");
        }
    }
}
