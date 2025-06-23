package br.eventos.zezinEventos.service;

import br.eventos.zezinEventos.model.Administrador;
import br.eventos.zezinEventos.model.repository.AdministradorDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdministradorService {

    @Autowired
    private AdministradorDAO repo;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    // Lista todos os administradores
    public List<Administrador> listarTodos() {
        return repo.findAll();
    }

    // Busca administrador por ID
    public Administrador buscarPorId(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Administrador não encontrado"));
    }

    // Busca administrador por login
    public Administrador buscarPorLogin(String login) {
        return repo.findByLogin(login);
    }

    // Salva ou atualiza um administrador
    public Administrador salvar(Administrador administrador) {
        if (administrador.getId() == null) {
            administrador.setSenha(passwordEncoder.encode(administrador.getSenha()));
        }
        return repo.save(administrador);
    }

    // Exclui um administrador
    public void excluir(Long id) {
        repo.deleteById(id);
    }

    // Conta total de administradores
    public long contarTodos() {
        return repo.count();
    }

    // Verifica se login já existe
    public boolean existeLogin(String login) {
        return repo.existsByLogin(login);
    }
    
    // Verifica se email já existe
    public boolean existeEmail(String email) {
        return repo.existsByEmail(email);
    }
}
