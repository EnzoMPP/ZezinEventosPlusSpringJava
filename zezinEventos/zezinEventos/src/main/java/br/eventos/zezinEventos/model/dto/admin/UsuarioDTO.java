package br.eventos.zezinEventos.model.dto.admin;

/**
 * DTO para transportar dados de usuários do sistema
 * Seguindo o princípio da Responsabilidade Única (SRP)
 */
public class UsuarioDTO {
    private Long id;
    private String nome;
    private String email;
    private String login;
    private Boolean ativo;
    private String tipo;          // CLIENTE, ORGANIZADOR, ADMIN
    private String tipoTexto;     // "Cliente", "Organizador", "Administrador"
    private String tipoClasse;    // "bg-success", "bg-warning", "bg-danger"
    private String documento;     // CPF ou CNPJ
    private String telefone;
    private String empresa;       // Apenas para Organizador
    private String cargo;         // Apenas para Administrador

    // Construtor padrão
    public UsuarioDTO() {}

    // Construtor completo
    public UsuarioDTO(Long id, String nome, String email, String login, Boolean ativo, 
                     String tipo, String tipoTexto, String tipoClasse, String documento, 
                     String telefone, String empresa, String cargo) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.login = login;
        this.ativo = ativo;
        this.tipo = tipo;
        this.tipoTexto = tipoTexto;
        this.tipoClasse = tipoClasse;
        this.documento = documento;
        this.telefone = telefone;
        this.empresa = empresa;
        this.cargo = cargo;
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

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getTipoTexto() {
        return tipoTexto;
    }

    public void setTipoTexto(String tipoTexto) {
        this.tipoTexto = tipoTexto;
    }

    public String getTipoClasse() {
        return tipoClasse;
    }

    public void setTipoClasse(String tipoClasse) {
        this.tipoClasse = tipoClasse;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEmpresa() {
        return empresa;
    }

    public void setEmpresa(String empresa) {
        this.empresa = empresa;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }
}
