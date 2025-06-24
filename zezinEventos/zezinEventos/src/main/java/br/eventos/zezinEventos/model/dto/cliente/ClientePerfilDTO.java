package br.eventos.zezinEventos.model.dto.cliente;

import br.eventos.zezinEventos.model.Cliente;

/**
 * DTO para transferência de dados do perfil do cliente.
 * Encapsula informações específicas do perfil do cliente para visualização e edição.
 * 
 * Este DTO segue o padrão de transferência de dados, evitando exposição direta
 * da entidade Cliente e permitindo controle sobre quais dados são transferidos.
 */
public class ClientePerfilDTO {
    
    private Long id;
    private String nome;
    private String email;
    private String telefone;
    private String cpf;
    private String login;
    private Boolean ativo;
    
    // Construtores
    public ClientePerfilDTO() {}
    
    /**
     * Construtor que cria um DTO a partir de um Cliente.
     * 
     * @param cliente Entidade cliente
     */
    public ClientePerfilDTO(Cliente cliente) {
        if (cliente != null) {
            this.id = cliente.getId();
            this.nome = cliente.getNome();
            this.email = cliente.getEmail();
            this.telefone = cliente.getTelefone();
            this.cpf = cliente.getCpf();
            this.login = cliente.getLogin();
            this.ativo = cliente.getAtivo();
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
    
    public String getCpf() {
        return cpf;
    }
    
    public void setCpf(String cpf) {
        this.cpf = cpf;
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
    
    // Métodos utilitários
    
    /**
     * Converte o DTO em uma entidade Cliente.
     * Útil para operações de persistência.
     * 
     * @return Nova instância de Cliente com dados do DTO
     */
    public Cliente paraEntidade() {
        Cliente cliente = new Cliente();
        cliente.setId(this.id);
        cliente.setNome(this.nome);
        cliente.setEmail(this.email);
        cliente.setTelefone(this.telefone);
        cliente.setCpf(this.cpf);
        cliente.setLogin(this.login);
        cliente.setAtivo(this.ativo);
        return cliente;
    }
    
    /**
     * Atualiza uma entidade Cliente existente com os dados do DTO.
     * Preserva campos que não devem ser alterados via DTO.
     * 
     * @param clienteExistente Entidade a ser atualizada
     */
    public void atualizarEntidade(Cliente clienteExistente) {
        if (clienteExistente != null) {
            clienteExistente.setNome(this.nome);
            clienteExistente.setEmail(this.email);
            clienteExistente.setTelefone(this.telefone);
            clienteExistente.setCpf(this.cpf);
            // Login e status ativo são preservados
        }
    }
    
    @Override
    public String toString() {
        return "ClientePerfilDTO{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", email='" + email + '\'' +
                ", telefone='" + telefone + '\'' +
                ", cpf='" + cpf + '\'' +
                ", login='" + login + '\'' +
                ", ativo=" + ativo +
                '}';
    }
}
