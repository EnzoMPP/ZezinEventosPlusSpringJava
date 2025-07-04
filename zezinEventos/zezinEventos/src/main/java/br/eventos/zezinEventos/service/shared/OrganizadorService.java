package br.eventos.zezinEventos.service.shared;

import br.eventos.zezinEventos.model.Organizador;
import br.eventos.zezinEventos.model.repository.OrganizadorDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

// Service para encapsular lógicas de negócio relacionadas a Organizadores
@Service
public class OrganizadorService {

    @Autowired
    private OrganizadorDAO repo;
    
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    // Salva ou atualiza um organizador
    public Organizador salvar(Organizador organizador) {
    if (organizador.getId() == null) {
        organizador.setSenha(passwordEncoder.encode(organizador.getSenha()));
    }
    return repo.save(organizador);
}

    // Lista todos os organizadores
    public List<Organizador> listarTodos() {
        return repo.findAll();
    }

    // Busca organizador por ID
    public Organizador buscarPorId(Long id) {
        return repo.findById(id).orElse(null);
    }

    // Exclui um organizador
    public void deletar(Long id) {
        repo.deleteById(id);
    }

    // Busca organizador por login
    public Organizador buscarPorLogin(String login) {
        return repo.findByLogin(login);
    }

    // Verifica se um organizador pode ser autenticado
    public boolean autenticar(String login, String senha) {
        Organizador organizador = repo.findByLogin(login);
        if (organizador != null && organizador.getAtivo()) {
            return passwordEncoder.matches(senha, organizador.getSenha());
        }
        return false;
    }
    
    // Verifica se login já existe
    public boolean existeLogin(String login) {
        return repo.existsByLogin(login);
    }
    
    // Verifica se email já existe
    public boolean existeEmail(String email) {
        return repo.existsByEmail(email);
    }
    
    // Verifica se CNPJ já existe
    public boolean existeCnpj(String cnpj) {
        return repo.existsByCnpj(cnpj);
    }
    
    // Conta total de organizadores
    public long contarTodos() {
        return repo.count();
    }
    
    // Busca organizadores por nome
    public List<Organizador> buscarPorNome(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            return listarTodos();
        }
        return repo.findByNomeContainingIgnoreCase(nome);
    }
    
    // Exclui um organizador
    public void excluir(Long id) {
        repo.deleteById(id);
    }
    
    // Verifica se existe organizador com o email informado
    public boolean existePorEmail(String email) {
        return repo.existsByEmail(email);
    }
    
    // Verifica se existe organizador com o CNPJ informado
    public boolean existePorCnpj(String cnpj) {
        return repo.existsByCnpj(cnpj);
    }
}