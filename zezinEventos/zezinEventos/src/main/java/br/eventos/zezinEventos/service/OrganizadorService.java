package br.eventos.zezinEventos.service;

import br.eventos.zezinEventos.model.Organizador;
import br.eventos.zezinEventos.model.repository.OrganizadorDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrganizadorService {

    @Autowired
    private OrganizadorDAO repo;
    
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public Organizador salvar(Organizador organizador) {
        return repo.save(organizador);
    }

    public List<Organizador> listarTodos() {
        return repo.findAll();
    }

    public Organizador buscarPorId(Long id) {
        return repo.findById(id).orElse(null);
    }

    public void deletar(Long id) {
        repo.deleteById(id);
    }

    public Organizador buscarPorLogin(String login) {
        return repo.findByLogin(login);
    }

    public boolean autenticar(String login, String senha) {
        Organizador organizador = repo.findByLogin(login);
        if (organizador != null && organizador.getAtivo()) {
            return passwordEncoder.matches(senha, organizador.getSenha());
        }
        return false;
    }
    
    public boolean existeLogin(String login) {
        return repo.existsByLogin(login);
    }
    
    public boolean existeEmail(String email) {
        return repo.existsByEmail(email);
    }
    
    public boolean existeCnpj(String cnpj) {
        return repo.existsByCnpj(cnpj);
    }
}