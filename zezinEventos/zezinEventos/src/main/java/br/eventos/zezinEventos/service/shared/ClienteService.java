package br.eventos.zezinEventos.service.shared;

import br.eventos.zezinEventos.model.Cliente;
import br.eventos.zezinEventos.model.repository.ClienteDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

// Service para encapsular lógicas de negócio relacionadas a Clientes
@Service
public class ClienteService {

    @Autowired
    private ClienteDAO clienteDAO;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    //Lista todos os clientes
    public List<Cliente> listarTodos() {
        return clienteDAO.findAll();
    }
    
    //Busca clientes por nome
    public List<Cliente> buscarPorNome(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            return listarTodos();
        }
        return clienteDAO.findByNomeContainingIgnoreCase("%" + nome + "%");
    }
    
    //Busca cliente por ID
    public Cliente buscarPorId(Long id) {
        return clienteDAO.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));
    }
    
    // Busca cliente por CPF
    public Cliente buscarPorCpf(String cpf) {
        return clienteDAO.findByCpf(cpf);
    }
    
    // Busca cliente por login
    public Cliente buscarPorLogin(String login) {
        return clienteDAO.findByLogin(login);
    }
    
    //Salva ou atualiza um cliente
    @Transactional
    public Cliente salvar(Cliente cliente) {
        validarCliente(cliente);
        return clienteDAO.save(cliente);
    }
    
    // Exclui um cliente
    @Transactional
    public void excluir(Long id) {
        Cliente cliente = buscarPorId(id);
        clienteDAO.delete(cliente);
    }
    
    // Verifica se um cliente pode ser autenticado
    public boolean autenticar(String login, String senha) {
        Cliente cliente = clienteDAO.findByLogin(login);
        return cliente != null && passwordEncoder.matches(senha, cliente.getSenha());
    }
    
    // Valida os dados do cliente
    private void validarCliente(Cliente cliente) {
        if (cliente.getNome() == null || cliente.getNome().trim().isEmpty()) {
            throw new RuntimeException("Nome é obrigatório");
        }
        
        if (cliente.getEmail() == null || cliente.getEmail().trim().isEmpty()) {
            throw new RuntimeException("Email é obrigatório");
        }
        
        if (cliente.getLogin() == null || cliente.getLogin().trim().isEmpty()) {
            throw new RuntimeException("Login é obrigatório");
        }
        
        // Verificar duplicidade de login, email e CPF
        Cliente clienteExistente = null;
        if (cliente.getId() != null) {
            clienteExistente = clienteDAO.findById(cliente.getId()).orElse(null);
        }
        
        // Verifica login
        if ((clienteExistente == null || !cliente.getLogin().equals(clienteExistente.getLogin())) 
                && clienteDAO.existsByLogin(cliente.getLogin())) {
            throw new RuntimeException("Login já está em uso");
        }
        
        // Verifica email
        if ((clienteExistente == null || !cliente.getEmail().equals(clienteExistente.getEmail())) 
                && clienteDAO.existsByEmail(cliente.getEmail())) {
            throw new RuntimeException("Email já está cadastrado");
        }
        
        // Verifica CPF
        if (cliente.getCpf() != null && !cliente.getCpf().trim().isEmpty() &&
            (clienteExistente == null || !cliente.getCpf().equals(clienteExistente.getCpf())) && 
            clienteDAO.existsByCpf(cliente.getCpf())) {
            throw new RuntimeException("CPF já cadastrado");
        }
    }

    // Conta total de clientes
    public long contarTodos() {
        return clienteDAO.count();
    }
}