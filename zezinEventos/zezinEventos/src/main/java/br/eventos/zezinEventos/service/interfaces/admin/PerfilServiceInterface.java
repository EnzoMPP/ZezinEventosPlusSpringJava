package br.eventos.zezinEventos.service.interfaces.admin;

import br.eventos.zezinEventos.model.dto.admin.PerfilDTO;

/**
 * Interface para serviços relacionados ao gerenciamento de perfil do administrador.
 * Define operações específicas para visualização e atualização do perfil administrativo.
 * 
 * Esta interface segue o Princípio da Responsabilidade Única (SRP) ao focar 
 * exclusivamente nas operações de perfil do administrador.
 */
public interface PerfilServiceInterface {
    
    /**
     * Busca e prepara os dados do perfil do administrador para visualização.
     * 
     * @param login Login do administrador autenticado
     * @return DTO com dados do perfil ou null se não encontrado
     */
    PerfilDTO obterPerfilPorLogin(String login);
    
    /**
     * Atualiza o perfil do administrador com validações de negócio.
     * 
     * @param perfilAtualizado DTO com dados atualizados do perfil
     * @param loginAtual Login atual do administrador (para validações)
     * @return Resultado da operação com mensagens de sucesso/erro
     */
    ResultadoOperacao atualizarPerfil(PerfilDTO perfilAtualizado, String loginAtual);
    
    /**
     * Valida se um login está disponível para uso (não está sendo usado por outro admin).
     * 
     * @param novoLogin Login a ser validado
     * @param loginAtual Login atual do administrador (para excluir da validação)
     * @return true se o login está disponível, false caso contrário
     */
    boolean validarDisponibilidadeLogin(String novoLogin, String loginAtual);
    
    /**
     * Valida se um email está disponível para uso.
     * 
     * @param novoEmail Email a ser validado
     * @param emailAtual Email atual do administrador (para excluir da validação)
     * @return true se o email está disponível, false caso contrário
     */
    boolean validarDisponibilidadeEmail(String novoEmail, String emailAtual);
    
    /**
     * Classe para encapsular o resultado de operações de atualização.
     */
    class ResultadoOperacao {
        private boolean sucesso;
        private String mensagem;
        private boolean requerLogout; // Indica se é necessário logout após a operação
        
        public ResultadoOperacao(boolean sucesso, String mensagem) {
            this(sucesso, mensagem, false);
        }
        
        public ResultadoOperacao(boolean sucesso, String mensagem, boolean requerLogout) {
            this.sucesso = sucesso;
            this.mensagem = mensagem;
            this.requerLogout = requerLogout;
        }
        
        // Getters
        public boolean isSucesso() { return sucesso; }
        public String getMensagem() { return mensagem; }
        public boolean isRequerLogout() { return requerLogout; }
        
        // Métodos estáticos para facilitar criação
        public static ResultadoOperacao sucesso(String mensagem) {
            return new ResultadoOperacao(true, mensagem);
        }
        
        public static ResultadoOperacao sucessoComLogout(String mensagem) {
            return new ResultadoOperacao(true, mensagem, true);
        }
        
        public static ResultadoOperacao erro(String mensagem) {
            return new ResultadoOperacao(false, mensagem);
        }
    }
}
