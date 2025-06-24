package br.eventos.zezinEventos.model.dto.organizador;

import br.eventos.zezinEventos.model.Organizador;

/**
 * DTO para transferência de dados do dashboard do organizador.
 * Encapsula informações do dashboard e estatísticas dos eventos do organizador.
 */
public class OrganizadorDashboardDTO {      private Long organizadorId;
    private String nomeOrganizador;
    private String emailOrganizador;
    private String empresa;
    private String cnpj;
    private String telefone;
    private Long totalEventos;
    private Long eventosAtivos;
    private Long eventosFinalizados;
    private Long totalInscricoes;
    private Double mediaInscricoesPorEvento;
    
    // Construtores
    public OrganizadorDashboardDTO() {}
    
    /**
     * Construtor que cria um DTO com as estatísticas do organizador.
     */
    public OrganizadorDashboardDTO(Organizador organizador, Long totalEventos, 
                                  Long eventosAtivos, Long eventosFinalizados, 
                                  Long totalInscricoes) {        if (organizador != null) {
            this.organizadorId = organizador.getId();
            this.nomeOrganizador = organizador.getNome();
            this.emailOrganizador = organizador.getEmail();
            this.empresa = organizador.getEmpresa();
            this.cnpj = organizador.getCnpj();
            this.telefone = organizador.getTelefone();
        }
        this.totalEventos = totalEventos;
        this.eventosAtivos = eventosAtivos;
        this.eventosFinalizados = eventosFinalizados;
        this.totalInscricoes = totalInscricoes;
        
        // Calcular média de inscrições por evento
        if (totalEventos != null && totalEventos > 0 && totalInscricoes != null) {
            this.mediaInscricoesPorEvento = totalInscricoes.doubleValue() / totalEventos.doubleValue();
        } else {
            this.mediaInscricoesPorEvento = 0.0;
        }
    }
    
    // Getters e Setters
    public Long getOrganizadorId() {
        return organizadorId;
    }
    
    public void setOrganizadorId(Long organizadorId) {
        this.organizadorId = organizadorId;
    }
    
    public String getNomeOrganizador() {
        return nomeOrganizador;
    }
    
    public void setNomeOrganizador(String nomeOrganizador) {
        this.nomeOrganizador = nomeOrganizador;
    }      public String getEmpresa() {
        return empresa;
    }
    
    public void setEmpresa(String empresa) {
        this.empresa = empresa;
    }
    
    public String getEmailOrganizador() {
        return emailOrganizador;
    }
    
    public void setEmailOrganizador(String emailOrganizador) {
        this.emailOrganizador = emailOrganizador;
    }
    
    public String getCnpj() {
        return cnpj;
    }
    
    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }
    
    public String getTelefone() {
        return telefone;
    }
    
    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }
    
    public Long getTotalEventos() {
        return totalEventos;
    }
    
    public void setTotalEventos(Long totalEventos) {
        this.totalEventos = totalEventos;
        recalcularMedia();
    }
    
    public Long getEventosAtivos() {
        return eventosAtivos;
    }
    
    public void setEventosAtivos(Long eventosAtivos) {
        this.eventosAtivos = eventosAtivos;
    }
    
    public Long getEventosFinalizados() {
        return eventosFinalizados;
    }
    
    public void setEventosFinalizados(Long eventosFinalizados) {
        this.eventosFinalizados = eventosFinalizados;
    }
    
    public Long getTotalInscricoes() {
        return totalInscricoes;
    }
    
    public void setTotalInscricoes(Long totalInscricoes) {
        this.totalInscricoes = totalInscricoes;
        recalcularMedia();
    }
    
    public Double getMediaInscricoesPorEvento() {
        return mediaInscricoesPorEvento;
    }
    
    public void setMediaInscricoesPorEvento(Double mediaInscricoesPorEvento) {
        this.mediaInscricoesPorEvento = mediaInscricoesPorEvento;
    }
    
    // Métodos utilitários
    
    /**
     * Verifica se o organizador tem eventos.
     */
    public boolean temEventos() {
        return totalEventos != null && totalEventos > 0;
    }
    
    /**
     * Verifica se o organizador tem eventos ativos.
     */
    public boolean temEventosAtivos() {
        return eventosAtivos != null && eventosAtivos > 0;
    }
    
    /**
     * Recalcula a média de inscrições por evento.
     */
    private void recalcularMedia() {
        if (totalEventos != null && totalEventos > 0 && totalInscricoes != null) {
            this.mediaInscricoesPorEvento = totalInscricoes.doubleValue() / totalEventos.doubleValue();
        } else {
            this.mediaInscricoesPorEvento = 0.0;
        }
    }
}
