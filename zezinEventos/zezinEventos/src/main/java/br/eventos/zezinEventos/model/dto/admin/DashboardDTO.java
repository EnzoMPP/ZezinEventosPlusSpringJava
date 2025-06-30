package br.eventos.zezinEventos.model.dto.admin;

import br.eventos.zezinEventos.model.Evento;
import java.util.List;

/**
 * Data Transfer Object para dados do Dashboard Administrativo
 * 
 * Encapsula todas as estatísticas e dados necessários para o dashboard
 */
public class DashboardDTO {
    
    // === ESTATÍSTICAS GERAIS ===
    private long totalEventos;
    private long totalUsuarios;
    private long totalClientes;
    private long totalOrganizadores;
    private long totalAdministradores;
    
    // === ESTATÍSTICAS DE EVENTOS ===
    private long eventosAtivos;
    private long eventosFinalizados;
    private long totalInscricoes;
    private long totalVagas;
    private double taxaOcupacaoGlobal;
    
    // === DADOS ESPECIAIS ===
    private Evento eventoMaisPopular;
    private List<Evento> eventosRecentes;
    private List<String> alertas;
    
    // === CONSTRUTORES ===
    public DashboardDTO() {}
    
    // === BUILDER PATTERN para construção limpa ===
    public static class Builder {
        private DashboardDTO dashboard = new DashboardDTO();
        
        public Builder totalEventos(long totalEventos) {
            dashboard.totalEventos = totalEventos;
            return this;
        }
        
        public Builder totalUsuarios(long totalUsuarios) {
            dashboard.totalUsuarios = totalUsuarios;
            return this;
        }
        
        public Builder totalClientes(long totalClientes) {
            dashboard.totalClientes = totalClientes;
            return this;
        }
        
        public Builder totalOrganizadores(long totalOrganizadores) {
            dashboard.totalOrganizadores = totalOrganizadores;
            return this;
        }
        
        public Builder totalAdministradores(long totalAdministradores) {
            dashboard.totalAdministradores = totalAdministradores;
            return this;
        }
        
        public Builder eventosAtivos(long eventosAtivos) {
            dashboard.eventosAtivos = eventosAtivos;
            return this;
        }
        
        public Builder eventosFinalizados(long eventosFinalizados) {
            dashboard.eventosFinalizados = eventosFinalizados;
            return this;
        }
        
        public Builder totalInscricoes(long totalInscricoes) {
            dashboard.totalInscricoes = totalInscricoes;
            return this;
        }
        
        public Builder totalVagas(long totalVagas) {
            dashboard.totalVagas = totalVagas;
            return this;
        }
        
        public Builder taxaOcupacaoGlobal(double taxaOcupacaoGlobal) {
            dashboard.taxaOcupacaoGlobal = taxaOcupacaoGlobal;
            return this;
        }
        
        public Builder eventoMaisPopular(Evento eventoMaisPopular) {
            dashboard.eventoMaisPopular = eventoMaisPopular;
            return this;
        }
        
        public Builder eventosRecentes(List<Evento> eventosRecentes) {
            dashboard.eventosRecentes = eventosRecentes;
            return this;
        }
        
        public Builder alertas(List<String> alertas) {
            dashboard.alertas = alertas;
            return this;
        }
        
        public DashboardDTO build() {
            return dashboard;
        }
    }
    
    public static Builder builder() {
        return new Builder();
    }
    
    // === GETTERS ===
    public long getTotalEventos() { return totalEventos; }
    public long getTotalUsuarios() { return totalUsuarios; }
    public long getTotalClientes() { return totalClientes; }
    public long getTotalOrganizadores() { return totalOrganizadores; }
    public long getTotalAdministradores() { return totalAdministradores; }
    public long getEventosAtivos() { return eventosAtivos; }
    public long getEventosFinalizados() { return eventosFinalizados; }
    public long getTotalInscricoes() { return totalInscricoes; }
    public long getTotalVagas() { return totalVagas; }
    public double getTaxaOcupacaoGlobal() { return taxaOcupacaoGlobal; }
    public Evento getEventoMaisPopular() { return eventoMaisPopular; }
    public List<Evento> getEventosRecentes() { return eventosRecentes; }
    public List<String> getAlertas() { return alertas; }
    
    // === SETTERS (para frameworks que precisam) ===
    public void setTotalEventos(long totalEventos) { this.totalEventos = totalEventos; }
    public void setTotalUsuarios(long totalUsuarios) { this.totalUsuarios = totalUsuarios; }
    public void setTotalClientes(long totalClientes) { this.totalClientes = totalClientes; }
    public void setTotalOrganizadores(long totalOrganizadores) { this.totalOrganizadores = totalOrganizadores; }
    public void setTotalAdministradores(long totalAdministradores) { this.totalAdministradores = totalAdministradores; }
    public void setEventosAtivos(long eventosAtivos) { this.eventosAtivos = eventosAtivos; }
    public void setEventosFinalizados(long eventosFinalizados) { this.eventosFinalizados = eventosFinalizados; }
    public void setTotalInscricoes(long totalInscricoes) { this.totalInscricoes = totalInscricoes; }
    public void setTotalVagas(long totalVagas) { this.totalVagas = totalVagas; }
    public void setTaxaOcupacaoGlobal(double taxaOcupacaoGlobal) { this.taxaOcupacaoGlobal = taxaOcupacaoGlobal; }
    public void setEventoMaisPopular(Evento eventoMaisPopular) { this.eventoMaisPopular = eventoMaisPopular; }
    public void setEventosRecentes(List<Evento> eventosRecentes) { this.eventosRecentes = eventosRecentes; }
    public void setAlertas(List<String> alertas) { this.alertas = alertas; }
}
