package br.eventos.zezinEventos.model.repository;

import br.eventos.zezinEventos.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

//Repository para operações de banco de dados relacionadas a Clientes
@Repository
public interface ClienteDAO extends JpaRepository<Cliente, Long> {
    
    //Busca um cliente pelo CPF
    Cliente findByCpf(String cpf);
    
    // Busca um cliente pelo login e senha
    Cliente findByLoginAndSenha(String login, String senha);
    
    // Busca clientes pelo login ignorando case
    List<Cliente> findByLoginIgnoreCase(String login);

    // Busca clientes cujo nome contenha a string especificada (case insensitive)
    List<Cliente> findByNomeContainingIgnoreCase(String nome);
    
    // Busca um cliente específico pelo login
    Cliente findByLogin(String login);
    
    //Verifica se existe um cliente com o login especificado
    boolean existsByLogin(String login);
    
    // Verifica se existe um cliente com o email especificado
    boolean existsByEmail(String email);

    boolean existsByCpf(String cpf);
}