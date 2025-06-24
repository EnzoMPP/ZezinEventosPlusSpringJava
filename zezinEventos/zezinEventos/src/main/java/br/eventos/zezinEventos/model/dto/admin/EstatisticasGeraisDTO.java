package br.eventos.zezinEventos.model.dto.admin;

/**
 * DTO para transportar estatísticas gerais do sistema
 * Encapsula dados de usuários, eventos e vagas
 */
public class EstatisticasGeraisDTO {
    // Totais gerais
    private Long totalEventos;
    private Long totalUsuarios;
    private Long totalClientes;
    private Long totalOrganizadores;
    private Long totalAdministradores;
    
    // Eventos
    private Long eventosAtivos;
    private Long eventosInativos;
    private Long eventosFuturos;
    private Long eventosPassados;
    private Long eventosGratuitos;
    private Long eventosPagos;
    
    // Usuários
    private Long clientesAtivos;
    private Long clientesInativos;
    private Long organizadoresAtivos;
    private Long organizadoresInativos;
    private Long adminsAtivos;
    private Long adminsInativos;
    
    // Vagas e inscrições
    private Long totalVagas;
    private Long totalInscricoes;
    private Long vagasDisponiveis;
    private Double taxaOcupacaoGeral;
    
    // Financeiro
    private Double receitaTotal;

    // Construtor padrão
    public EstatisticasGeraisDTO() {}

    // Construtor completo
    public EstatisticasGeraisDTO(Long totalEventos, Long totalUsuarios, Long totalClientes,
                                Long totalOrganizadores, Long totalAdministradores, Long eventosAtivos,
                                Long eventosInativos, Long eventosFuturos, Long eventosPassados,
                                Long eventosGratuitos, Long eventosPagos, Long clientesAtivos,
                                Long clientesInativos, Long organizadoresAtivos, Long organizadoresInativos,
                                Long adminsAtivos, Long adminsInativos, Long totalVagas,
                                Long totalInscricoes, Long vagasDisponiveis, Double taxaOcupacaoGeral,
                                Double receitaTotal) {
        this.totalEventos = totalEventos;
        this.totalUsuarios = totalUsuarios;
        this.totalClientes = totalClientes;
        this.totalOrganizadores = totalOrganizadores;
        this.totalAdministradores = totalAdministradores;
        this.eventosAtivos = eventosAtivos;
        this.eventosInativos = eventosInativos;
        this.eventosFuturos = eventosFuturos;
        this.eventosPassados = eventosPassados;
        this.eventosGratuitos = eventosGratuitos;
        this.eventosPagos = eventosPagos;
        this.clientesAtivos = clientesAtivos;
        this.clientesInativos = clientesInativos;
        this.organizadoresAtivos = organizadoresAtivos;
        this.organizadoresInativos = organizadoresInativos;
        this.adminsAtivos = adminsAtivos;
        this.adminsInativos = adminsInativos;
        this.totalVagas = totalVagas;
        this.totalInscricoes = totalInscricoes;
        this.vagasDisponiveis = vagasDisponiveis;
        this.taxaOcupacaoGeral = taxaOcupacaoGeral;
        this.receitaTotal = receitaTotal;
    }

    // Getters e Setters
    public Long getTotalEventos() { return totalEventos; }
    public void setTotalEventos(Long totalEventos) { this.totalEventos = totalEventos; }
    
    public Long getTotalUsuarios() { return totalUsuarios; }
    public void setTotalUsuarios(Long totalUsuarios) { this.totalUsuarios = totalUsuarios; }
    
    public Long getTotalClientes() { return totalClientes; }
    public void setTotalClientes(Long totalClientes) { this.totalClientes = totalClientes; }
    
    public Long getTotalOrganizadores() { return totalOrganizadores; }
    public void setTotalOrganizadores(Long totalOrganizadores) { this.totalOrganizadores = totalOrganizadores; }
    
    public Long getTotalAdministradores() { return totalAdministradores; }
    public void setTotalAdministradores(Long totalAdministradores) { this.totalAdministradores = totalAdministradores; }
    
    public Long getEventosAtivos() { return eventosAtivos; }
    public void setEventosAtivos(Long eventosAtivos) { this.eventosAtivos = eventosAtivos; }
    
    public Long getEventosInativos() { return eventosInativos; }
    public void setEventosInativos(Long eventosInativos) { this.eventosInativos = eventosInativos; }
    
    public Long getEventosFuturos() { return eventosFuturos; }
    public void setEventosFuturos(Long eventosFuturos) { this.eventosFuturos = eventosFuturos; }
    
    public Long getEventosPassados() { return eventosPassados; }
    public void setEventosPassados(Long eventosPassados) { this.eventosPassados = eventosPassados; }
    
    public Long getEventosGratuitos() { return eventosGratuitos; }
    public void setEventosGratuitos(Long eventosGratuitos) { this.eventosGratuitos = eventosGratuitos; }
    
    public Long getEventosPagos() { return eventosPagos; }
    public void setEventosPagos(Long eventosPagos) { this.eventosPagos = eventosPagos; }
    
    public Long getClientesAtivos() { return clientesAtivos; }
    public void setClientesAtivos(Long clientesAtivos) { this.clientesAtivos = clientesAtivos; }
    
    public Long getClientesInativos() { return clientesInativos; }
    public void setClientesInativos(Long clientesInativos) { this.clientesInativos = clientesInativos; }
    
    public Long getOrganizadoresAtivos() { return organizadoresAtivos; }
    public void setOrganizadoresAtivos(Long organizadoresAtivos) { this.organizadoresAtivos = organizadoresAtivos; }
    
    public Long getOrganizadoresInativos() { return organizadoresInativos; }
    public void setOrganizadoresInativos(Long organizadoresInativos) { this.organizadoresInativos = organizadoresInativos; }
    
    public Long getAdminsAtivos() { return adminsAtivos; }
    public void setAdminsAtivos(Long adminsAtivos) { this.adminsAtivos = adminsAtivos; }
    
    public Long getAdminsInativos() { return adminsInativos; }
    public void setAdminsInativos(Long adminsInativos) { this.adminsInativos = adminsInativos; }
    
    public Long getTotalVagas() { return totalVagas; }
    public void setTotalVagas(Long totalVagas) { this.totalVagas = totalVagas; }
    
    public Long getTotalInscricoes() { return totalInscricoes; }
    public void setTotalInscricoes(Long totalInscricoes) { this.totalInscricoes = totalInscricoes; }
    
    public Long getVagasDisponiveis() { return vagasDisponiveis; }
    public void setVagasDisponiveis(Long vagasDisponiveis) { this.vagasDisponiveis = vagasDisponiveis; }
    
    public Double getTaxaOcupacaoGeral() { return taxaOcupacaoGeral; }
    public void setTaxaOcupacaoGeral(Double taxaOcupacaoGeral) { this.taxaOcupacaoGeral = taxaOcupacaoGeral; }
    
    public Double getReceitaTotal() { return receitaTotal; }
    public void setReceitaTotal(Double receitaTotal) { this.receitaTotal = receitaTotal; }
}
