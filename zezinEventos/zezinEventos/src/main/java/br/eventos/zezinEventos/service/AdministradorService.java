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
    private PasswordEncoder passwordEncoder;    // Lista todos os administradores
    public List<Administrador> listarTodos() {
        return repo.findAll();
    }

    // Lista apenas administradores ativos
    public List<Administrador> listarAtivos() {
        return repo.findByAtivo(true);
    }

    // Busca administrador por ID
    public Administrador buscarPorId(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Administrador não encontrado"));
    }    // Busca administrador por login
    public Administrador buscarPorLogin(String login) {
        return repo.findByLogin(login);
    }

    // Busca administradores por nome
    public List<Administrador> buscarPorNome(String nome) {
        return repo.findByNomeContainingIgnoreCase(nome);
    }    // Salva ou atualiza um administrador
    public Administrador salvar(Administrador administrador) {
        if (administrador.getId() == null) {
            // Novo administrador - criptografar senha
            administrador.setSenha(passwordEncoder.encode(administrador.getSenha()));
        } else {
            // Administrador existente - verificar se senha foi alterada
            Administrador existente = repo.findById(administrador.getId()).orElse(null);
            if (existente != null && !administrador.getSenha().equals(existente.getSenha())) {
                // Senha foi alterada, criptografar
                if (!administrador.getSenha().startsWith("$2a$")) { // Verifica se já não está criptografada
                    administrador.setSenha(passwordEncoder.encode(administrador.getSenha()));
                }
            } else if (existente != null) {
                // Manter senha existente se não foi alterada
                administrador.setSenha(existente.getSenha());
            }
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
    }    // Verifica se login já existe
    public boolean existeLogin(String login) {
        return repo.existsByLogin(login);
    }
    
    // Verifica se email já existe
    public boolean existeEmail(String email) {
        return repo.existsByEmail(email);
    }
    
    // Verifica se login já existe (método alternativo)
    public boolean existePorLogin(String login) {
        return repo.existsByLogin(login);
    }
      // Verifica se email já existe (método alternativo)
    public boolean existePorEmail(String email) {
        return repo.existsByEmail(email);
    }
}
