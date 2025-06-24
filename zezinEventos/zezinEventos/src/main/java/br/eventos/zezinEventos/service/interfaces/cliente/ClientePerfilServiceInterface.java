package br.eventos.zezinEventos.service.interfaces.cliente;

import br.eventos.zezinEventos.model.dto.cliente.ClientePerfilDTO;

/**
 * Interface para serviços relacionados ao gerenciamento de perfil do cliente.
 * Define operações específicas para visualização e atualização do perfil do cliente.
 */
public interface ClientePerfilServiceInterface {
    
    /**
     * Busca e prepara os dados do perfil do cliente para visualização.
     * 
     * @param login Login do cliente autenticado
     * @return DTO com dados do perfil ou null se não encontrado
     */
    ClientePerfilDTO obterPerfilPorLogin(String login);
    
    /**
     * Atualiza o perfil do cliente com validações de negócio.
     * 
     * @param perfilAtualizado DTO com dados atualizados do perfil
     * @param loginAtual Login atual do cliente (para validações)
     * @return Resultado da operação com mensagens de sucesso/erro
     */
    ResultadoOperacao atualizarPerfil(ClientePerfilDTO perfilAtualizado, String loginAtual);
    
    /**
     * Valida se um email está disponível para uso.
     * 
     * @param novoEmail Email a ser validado
     * @param emailAtual Email atual do cliente (para excluir da validação)
     * @return true se o email está disponível, false caso contrário
     */
    boolean validarDisponibilidadeEmail(String novoEmail, String emailAtual);
    
    /**
     * Valida se um CPF está disponível para uso.
     * 
     * @param novoCpf CPF a ser validado
     * @param cpfAtual CPF atual do cliente (para excluir da validação)
     * @return true se o CPF está disponível, false caso contrário
     */
    boolean validarDisponibilidadeCpf(String novoCpf, String cpfAtual);
    
    /**
     * Classe para encapsular o resultado de operações de atualização.
     */
    class ResultadoOperacao {
        private boolean sucesso;
        private String mensagem;
        
        public ResultadoOperacao(boolean sucesso, String mensagem) {
            this.sucesso = sucesso;
            this.mensagem = mensagem;
        }
        
        // Getters
        public boolean isSucesso() { return sucesso; }
        public String getMensagem() { return mensagem; }
        
        // Métodos estáticos para facilitar criação
        public static ResultadoOperacao sucesso(String mensagem) {
            return new ResultadoOperacao(true, mensagem);
        }
        
        public static ResultadoOperacao erro(String mensagem) {
            return new ResultadoOperacao(false, mensagem);
        }
    }
}
