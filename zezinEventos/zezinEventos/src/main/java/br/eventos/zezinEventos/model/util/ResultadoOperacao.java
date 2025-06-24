package br.eventos.zezinEventos.model.util;

/**
 * Classe utilitária para encapsular o resultado de operações de negócio.
 * Fornece informações sobre sucesso/falha e mensagens associadas.
 */
public class ResultadoOperacao {
    
    private final boolean sucesso;
    private final String mensagem;
    private final String tipoMensagem; // "success", "error", "warning"
    private final Object dados; // Para retornar dados quando necessário
    
    private ResultadoOperacao(boolean sucesso, String mensagem, String tipoMensagem, Object dados) {
        this.sucesso = sucesso;
        this.mensagem = mensagem;
        this.tipoMensagem = tipoMensagem;
        this.dados = dados;
    }
    
    // Getters
    public boolean isSucesso() { 
        return sucesso; 
    }
    
    public String getMensagem() { 
        return mensagem; 
    }
    
    public String getTipoMensagem() { 
        return tipoMensagem; 
    }
    
    @SuppressWarnings("unchecked")
    public <T> T getDados() {
        return (T) dados;
    }
    
    // Métodos estáticos para facilitar criação
    public static ResultadoOperacao sucesso(String mensagem) {
        return new ResultadoOperacao(true, mensagem, "success", null);
    }
    
    public static ResultadoOperacao sucesso(String mensagem, Object dados) {
        return new ResultadoOperacao(true, mensagem, "success", dados);
    }
    
    public static ResultadoOperacao erro(String mensagem) {
        return new ResultadoOperacao(false, mensagem, "error", null);
    }
    
    public static ResultadoOperacao erro(String mensagem, Object dados) {
        return new ResultadoOperacao(false, mensagem, "error", dados);
    }
    
    public static ResultadoOperacao aviso(String mensagem) {
        return new ResultadoOperacao(false, mensagem, "warning", null);
    }
    
    public static ResultadoOperacao aviso(String mensagem, Object dados) {
        return new ResultadoOperacao(false, mensagem, "warning", dados);
    }
}
