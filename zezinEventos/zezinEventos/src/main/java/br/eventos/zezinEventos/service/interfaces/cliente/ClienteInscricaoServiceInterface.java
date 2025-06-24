package br.eventos.zezinEventos.service.interfaces.cliente;

import br.eventos.zezinEventos.model.dto.cliente.ClienteInscricoesDTO;

/**
 * Interface para serviços relacionados às inscrições do cliente.
 * Define operações para gerenciamento de inscrições, cancelamentos e visualização de histórico.
 */
public interface ClienteInscricaoServiceInterface {
    
    /**
     * Obtém as inscrições ativas do cliente.
     * 
     * @param loginCliente Login do cliente autenticado
     * @return DTO com inscrições ativas
     */
    ClienteInscricoesDTO obterInscricoesAtivas(String loginCliente);
    
    /**
     * Obtém o histórico completo de inscrições do cliente.
     * 
     * @param loginCliente Login do cliente autenticado
     * @return DTO com histórico completo de inscrições
     */
    ClienteInscricoesDTO obterHistoricoCompleto(String loginCliente);
    
    /**
     * Realiza a inscrição do cliente em um evento.
     * 
     * @param loginCliente Login do cliente
     * @param eventoId ID do evento
     * @return Resultado da operação
     */
    ResultadoInscricao inscreverEmEvento(String loginCliente, Long eventoId);
    
    /**
     * Cancela a inscrição do cliente em um evento.
     * 
     * @param loginCliente Login do cliente
     * @param eventoId ID do evento
     * @return Resultado da operação
     */
    ResultadoInscricao cancelarInscricao(String loginCliente, Long eventoId);
    
    /**
     * Verifica se o cliente pode se inscrever em um evento específico.
     * 
     * @param loginCliente Login do cliente
     * @param eventoId ID do evento
     * @return true se pode se inscrever, false caso contrário
     */
    boolean podeSeInscrever(String loginCliente, Long eventoId);
    
    /**
     * Classe para encapsular o resultado de operações de inscrição.
     */
    class ResultadoInscricao {
        private boolean sucesso;
        private String mensagem;
        private String tipoMensagem; // "success", "error", "warning"
        
        public ResultadoInscricao(boolean sucesso, String mensagem, String tipoMensagem) {
            this.sucesso = sucesso;
            this.mensagem = mensagem;
            this.tipoMensagem = tipoMensagem;
        }
        
        // Getters
        public boolean isSucesso() { return sucesso; }
        public String getMensagem() { return mensagem; }
        public String getTipoMensagem() { return tipoMensagem; }
        
        // Métodos estáticos para facilitar criação
        public static ResultadoInscricao sucesso(String mensagem) {
            return new ResultadoInscricao(true, mensagem, "success");
        }
        
        public static ResultadoInscricao erro(String mensagem) {
            return new ResultadoInscricao(false, mensagem, "error");
        }
        
        public static ResultadoInscricao aviso(String mensagem) {
            return new ResultadoInscricao(false, mensagem, "warning");
        }
    }
}
