package br.eventos.zezinEventos.model.dto.admin;

import java.util.List;

/**
 * DTO para transportar dados da listagem de usuários
 * Encapsula informações de busca e estatísticas
 */
public class UsuariosListaDTO {
    private List<UsuarioDTO> usuarios;
    private String busca;
    private Integer totalUsuarios;
    private Integer totalClientes;
    private Integer totalOrganizadores;
    private Integer totalAdministradores;

    // Construtor padrão
    public UsuariosListaDTO() {}

    // Construtor completo
    public UsuariosListaDTO(List<UsuarioDTO> usuarios, String busca, 
                           Integer totalUsuarios, Integer totalClientes,
                           Integer totalOrganizadores, Integer totalAdministradores) {
        this.usuarios = usuarios;
        this.busca = busca;
        this.totalUsuarios = totalUsuarios;
        this.totalClientes = totalClientes;
        this.totalOrganizadores = totalOrganizadores;
        this.totalAdministradores = totalAdministradores;
    }

    // Getters e Setters
    public List<UsuarioDTO> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(List<UsuarioDTO> usuarios) {
        this.usuarios = usuarios;
    }

    public String getBusca() {
        return busca;
    }

    public void setBusca(String busca) {
        this.busca = busca;
    }

    public Integer getTotalUsuarios() {
        return totalUsuarios;
    }

    public void setTotalUsuarios(Integer totalUsuarios) {
        this.totalUsuarios = totalUsuarios;
    }

    public Integer getTotalClientes() {
        return totalClientes;
    }

    public void setTotalClientes(Integer totalClientes) {
        this.totalClientes = totalClientes;
    }

    public Integer getTotalOrganizadores() {
        return totalOrganizadores;
    }

    public void setTotalOrganizadores(Integer totalOrganizadores) {
        this.totalOrganizadores = totalOrganizadores;
    }

    public Integer getTotalAdministradores() {
        return totalAdministradores;
    }

    public void setTotalAdministradores(Integer totalAdministradores) {
        this.totalAdministradores = totalAdministradores;
    }
}
