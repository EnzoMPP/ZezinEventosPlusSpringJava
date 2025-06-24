package br.eventos.zezinEventos.model.dto.admin;

import br.eventos.zezinEventos.model.Administrador;

/**
 * DTO para transferência de dados do perfil do administrador.
 * Encapsula informações específicas do perfil administrativo para visualização e edição.
 * 
 * Este DTO segue o padrão de transferência de dados, evitando exposição direta
 * da entidade Administrador e permitindo controle sobre quais dados são transferidos.
 */
public class PerfilDTO {
    
    private Long id;
    private String nome;
    private String email;
    private String login;
    private String senha; // Para atualização - será tratada com cuidado
    private Boolean ativo;
    
    // Construtores
    public PerfilDTO() {}
    
    /**
     * Construtor que cria um DTO a partir de um Administrador.
     * A senha não é incluída por motivos de segurança.
     * 
     * @param administrador Entidade administrador
     */
    public PerfilDTO(Administrador administrador) {
        if (administrador != null) {
            this.id = administrador.getId();
            this.nome = administrador.getNome();
            this.email = administrador.getEmail();
            this.login = administrador.getLogin();
            this.ativo = administrador.getAtivo();
            // Senha não é copiada por segurança
        }
    }
    
    /**
     * Construtor completo para casos específicos.
     */
    public PerfilDTO(Long id, String nome, String email, String login, Boolean ativo) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.login = login;
        this.ativo = ativo;
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
    
    public Boolean getAtivo() {
        return ativo;
    }
    
    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }
    
    // Métodos utilitários
    
    /**
     * Verifica se a senha foi fornecida para atualização.
     * 
     * @return true se a senha está presente e não vazia
     */
    public boolean temSenhaParaAtualizar() {
        return senha != null && !senha.trim().isEmpty();
    }
    
    /**
     * Converte o DTO em uma entidade Administrador.
     * Útil para operações de persistência.
     * 
     * @return Nova instância de Administrador com dados do DTO
     */
    public Administrador paraEntidade() {
        Administrador admin = new Administrador();
        admin.setId(this.id);
        admin.setNome(this.nome);
        admin.setEmail(this.email);
        admin.setLogin(this.login);
        admin.setSenha(this.senha);
        admin.setAtivo(this.ativo);
        return admin;
    }
    
    /**
     * Atualiza uma entidade Administrador existente com os dados do DTO.
     * Preserva campos que não devem ser alterados via DTO.
     * 
     * @param administradorExistente Entidade a ser atualizada
     */
    public void atualizarEntidade(Administrador administradorExistente) {
        if (administradorExistente != null) {
            administradorExistente.setNome(this.nome);
            administradorExistente.setEmail(this.email);
            administradorExistente.setLogin(this.login);
            
            // Só atualiza a senha se foi fornecida
            if (temSenhaParaAtualizar()) {
                administradorExistente.setSenha(this.senha);
            }
            
            // O status ativo é preservado - não é alterado via perfil
        }
    }
    
    @Override
    public String toString() {
        return "PerfilDTO{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", email='" + email + '\'' +
                ", login='" + login + '\'' +
                ", ativo=" + ativo +
                '}';
    }
}
