package br.eventos.zezinEventos.model.dto.organizador;

import br.eventos.zezinEventos.model.Organizador;

/**
 * DTO para transferência de dados do perfil completo do organizador.
 * Encapsula tanto informações pessoais herdadas de usuário quanto dados específicos de organizador.
 */
public class OrganizadorPerfilDTO {
    
    private Long id;
    private String nome;
    private String email;
    private String telefone;
    private String login;
    private String senha;
    private String confirmarSenha;
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
            this.telefone = organizador.getTelefone();
            this.login = organizador.getLogin();
            this.empresa = organizador.getEmpresa();
            this.cnpj = organizador.getCnpj();
            this.ativo = organizador.getAtivo();
            // Senha não é preenchida por segurança
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
    
    public String getSenha() {
        return senha;
    }
    
    public void setSenha(String senha) {
        this.senha = senha;
    }
    
    public String getConfirmarSenha() {
        return confirmarSenha;
    }
    
    public void setConfirmarSenha(String confirmarSenha) {
        this.confirmarSenha = confirmarSenha;
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
            organizadorExistente.setEmail(this.email);
            organizadorExistente.setTelefone(this.telefone);
            if (this.login != null && !this.login.trim().isEmpty()) {
                organizadorExistente.setLogin(this.login);
            }
            organizadorExistente.setEmpresa(this.empresa);
            organizadorExistente.setCnpj(this.cnpj);
            // Status ativo é preservado, senha é tratada separadamente por segurança
        }
    }
      @Override
    public String toString() {
        return "OrganizadorPerfilDTO{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", email='" + email + '\'' +
                ", telefone='" + telefone + '\'' +
                ", login='" + login + '\'' +
                ", empresa='" + empresa + '\'' +
                ", cnpj='" + cnpj + '\'' +
                ", ativo=" + ativo +
                '}';
    }
}
