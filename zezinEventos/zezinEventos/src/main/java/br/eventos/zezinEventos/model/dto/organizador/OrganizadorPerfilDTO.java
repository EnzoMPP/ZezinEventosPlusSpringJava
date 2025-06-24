package br.eventos.zezinEventos.model.dto.organizador;

import br.eventos.zezinEventos.model.Organizador;

/**
 * DTO para transferência de dados do perfil/empresa do organizador.
 * Encapsula informações específicas do perfil e dados da empresa.
 */
public class OrganizadorPerfilDTO {
    
    private Long id;
    private String nome;
    private String email;
    private String telefone;    private String login;
    private String empresa;
    private String cnpj;
    private Boolean ativo;
    
    // Construtores
    public OrganizadorPerfilDTO() {}
    
    /**
     * Construtor que cria um DTO a partir de um Organizador.
     */
    public OrganizadorPerfilDTO(Organizador organizador) {
        if (organizador != null) {
            this.id = organizador.getId();
            this.nome = organizador.getNome();
            this.email = organizador.getEmail();
            this.telefone = organizador.getTelefone();            this.login = organizador.getLogin();
            this.empresa = organizador.getEmpresa();
            this.cnpj = organizador.getCnpj();
            this.ativo = organizador.getAtivo();
        }
    }
    
    // Getters e Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getNome() {
        return nome;
    }
    
    public void setNome(String nome) {
        this.nome = nome;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getTelefone() {
        return telefone;
    }
    
    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }
    
    public String getLogin() {
        return login;
    }
    
    public void setLogin(String login) {
        this.login = login;
    }
      public String getEmpresa() {
        return empresa;
    }
    
    public void setEmpresa(String empresa) {
        this.empresa = empresa;
    }
    
    public String getCnpj() {
        return cnpj;
    }
    
    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }
    
    public Boolean getAtivo() {
        return ativo;
    }
    
    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }
    
    // Métodos utilitários
    
    /**
     * Converte o DTO em uma entidade Organizador.
     */
    public Organizador paraEntidade() {
        Organizador organizador = new Organizador();
        organizador.setId(this.id);
        organizador.setNome(this.nome);
        organizador.setEmail(this.email);
        organizador.setTelefone(this.telefone);        organizador.setLogin(this.login);
        organizador.setEmpresa(this.empresa);
        organizador.setCnpj(this.cnpj);
        organizador.setAtivo(this.ativo);
        return organizador;
    }
    
    /**
     * Atualiza uma entidade Organizador existente com os dados do DTO.
     */
    public void atualizarEntidade(Organizador organizadorExistente) {
        if (organizadorExistente != null) {
            organizadorExistente.setNome(this.nome);
            organizadorExistente.setEmail(this.email);            organizadorExistente.setTelefone(this.telefone);
            organizadorExistente.setEmpresa(this.empresa);
            organizadorExistente.setCnpj(this.cnpj);
            // Login e status ativo são preservados
        }
    }
    
    @Override
    public String toString() {
        return "OrganizadorPerfilDTO{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", email='" + email + '\'' +                ", telefone='" + telefone + '\'' +
                ", login='" + login + '\'' +
                ", empresa='" + empresa + '\'' +
                ", cnpj='" + cnpj + '\'' +
                ", ativo=" + ativo +
                '}';
    }
}
