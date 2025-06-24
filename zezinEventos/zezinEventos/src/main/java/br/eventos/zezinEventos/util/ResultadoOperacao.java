package br.eventos.zezinEventos.util;

/**
 * Classe utilitária para encapsular o resultado de operações.
 * Facilita o tratamento de retornos de métodos de serviço.
 */
public class ResultadoOperacao<T> {
    
    private boolean sucesso;
    private String mensagem;
    private String tipoMensagem; // "success", "error", "warning"
    private T dados;
    
    public ResultadoOperacao(boolean sucesso, String mensagem, String tipoMensagem) {
        this.sucesso = sucesso;
        this.mensagem = mensagem;
        this.tipoMensagem = tipoMensagem;
    }
    
    public ResultadoOperacao(boolean sucesso, String mensagem, String tipoMensagem, T dados) {
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
    
    public T getDados() {
        return dados;
    }
    
    // Setters
    public void setSucesso(boolean sucesso) {
        this.sucesso = sucesso;
    }
    
    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }
    
    public void setTipoMensagem(String tipoMensagem) {
        this.tipoMensagem = tipoMensagem;
    }
    
    public void setDados(T dados) {
        this.dados = dados;
    }
    
    // Métodos estáticos para facilitar criação
    public static <T> ResultadoOperacao<T> sucesso(String mensagem) {
        return new ResultadoOperacao<>(true, mensagem, "success");
    }
    
    public static <T> ResultadoOperacao<T> sucesso(String mensagem, T dados) {
        return new ResultadoOperacao<>(true, mensagem, "success", dados);
    }
    
    public static <T> ResultadoOperacao<T> erro(String mensagem) {
        return new ResultadoOperacao<>(false, mensagem, "error");
    }
    
    public static <T> ResultadoOperacao<T> erro(String mensagem, T dados) {
        return new ResultadoOperacao<>(false, mensagem, "error", dados);
    }
    
    public static <T> ResultadoOperacao<T> aviso(String mensagem) {
        return new ResultadoOperacao<>(false, mensagem, "warning");
    }
    
    public static <T> ResultadoOperacao<T> aviso(String mensagem, T dados) {
        return new ResultadoOperacao<>(false, mensagem, "warning", dados);
    }
}
