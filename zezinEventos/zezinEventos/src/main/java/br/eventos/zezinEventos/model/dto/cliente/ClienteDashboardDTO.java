package br.eventos.zezinEventos.model.dto.cliente;

import br.eventos.zezinEventos.model.Cliente;

/**
 * DTO para transferência de dados do dashboard do cliente.
 * Encapsula informações do dashboard e estatísticas do cliente.
 */
public class ClienteDashboardDTO {
      private Long clienteId;
    private String nomeCliente;
    private String emailCliente;
    private String telefoneCliente;
    private String cpfCliente;
    private String dataNascCliente;
    private Long totalEventosInscritos;  
    private Long eventosProximos;
    private Long eventosFinalizados;
    
    // Construtores
    public ClienteDashboardDTO() {}    /**
     * Construtor que cria um DTO com as estatísticas do cliente.
     */
    public ClienteDashboardDTO(Cliente cliente, Long totalEventosInscritos, 
                              Long eventosProximos, Long eventosFinalizados) {        if (cliente != null) {
            this.clienteId = cliente.getId();
            this.nomeCliente = cliente.getNome();
            this.emailCliente = cliente.getEmail();
            this.telefoneCliente = cliente.getTelefone();
            this.cpfCliente = cliente.getCpf();
            this.dataNascCliente = cliente.getDataNasc() != null ? cliente.getDataNasc().toString() : null;
        }
        this.totalEventosInscritos = totalEventosInscritos;
        this.eventosProximos = eventosProximos;
        this.eventosFinalizados = eventosFinalizados;
    }
    
    // Getters e Setters
    public Long getClienteId() {
        return clienteId;
    }
    
    public void setClienteId(Long clienteId) {
        this.clienteId = clienteId;
    }
    
    public String getNomeCliente() {
        return nomeCliente;
    }
      public void setNomeCliente(String nomeCliente) {
        this.nomeCliente = nomeCliente;
    }
    
    public String getEmailCliente() {
        return emailCliente;
    }
      public void setEmailCliente(String emailCliente) {
        this.emailCliente = emailCliente;
    }
    
    public String getTelefoneCliente() {
        return telefoneCliente;
    }
      public void setTelefoneCliente(String telefoneCliente) {
        this.telefoneCliente = telefoneCliente;
    }
    
    public String getCpfCliente() {
        return cpfCliente;
    }
    
    public void setCpfCliente(String cpfCliente) {
        this.cpfCliente = cpfCliente;
    }
    
    public String getDataNascCliente() {
        return dataNascCliente;
    }
    
    public void setDataNascCliente(String dataNascCliente) {
        this.dataNascCliente = dataNascCliente;
    }
    
    public Long getTotalEventosInscritos() {
        return totalEventosInscritos;
    }
    
    public void setTotalEventosInscritos(Long totalEventosInscritos) {
        this.totalEventosInscritos = totalEventosInscritos;
    }
    
    public Long getEventosProximos() {
        return eventosProximos;
    }
    
    public void setEventosProximos(Long eventosProximos) {
        this.eventosProximos = eventosProximos;
    }
    
    public Long getEventosFinalizados() {
        return eventosFinalizados;
    }
    
    public void setEventosFinalizados(Long eventosFinalizados) {
        this.eventosFinalizados = eventosFinalizados;
    }
    
    /**
     * Método utilitário para verificar se o cliente tem eventos.
     */
    public boolean temEventos() {
        return totalEventosInscritos != null && totalEventosInscritos > 0;
    }
    
    /**
     * Método utilitário para verificar se o cliente tem eventos próximos.
     */
    public boolean temEventosProximos() {
        return eventosProximos != null && eventosProximos > 0;
    }
}
