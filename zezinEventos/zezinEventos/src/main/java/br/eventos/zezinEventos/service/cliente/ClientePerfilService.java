package br.eventos.zezinEventos.service.cliente;

import br.eventos.zezinEventos.model.Cliente;
import br.eventos.zezinEventos.model.dto.cliente.ClientePerfilDTO;
import br.eventos.zezinEventos.service.interfaces.cliente.ClientePerfilServiceInterface;
import br.eventos.zezinEventos.service.shared.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implementação do serviço para gerenciamento de perfil do cliente.
 */
@Service
public class ClientePerfilService implements ClientePerfilServiceInterface {
    
    private final ClienteService clienteService;
    
    @Autowired
    public ClientePerfilService(ClienteService clienteService) {
        this.clienteService = clienteService;
    }
    
    @Override
    public ClientePerfilDTO obterPerfilPorLogin(String login) {
        if (login == null || login.trim().isEmpty()) {
            return null;
        }
        
        try {
            Cliente cliente = clienteService.buscarPorLogin(login);
            return cliente != null ? new ClientePerfilDTO(cliente) : null;
        } catch (Exception e) {
            // Log do erro seria implementado aqui
            return null;
        }
    }
    
    @Override
    public ResultadoOperacao atualizarPerfil(ClientePerfilDTO perfilAtualizado, String loginAtual) {
        if (perfilAtualizado == null || loginAtual == null) {
            return ResultadoOperacao.erro("Dados inválidos para atualização do perfil.");
        }
        
        try {
            // Buscar cliente existente
            Cliente clienteExistente = clienteService.buscarPorLogin(loginAtual);
            if (clienteExistente == null) {
                return ResultadoOperacao.erro("Cliente não encontrado!");
            }
            
            // Validar alteração de email
            if (perfilAtualizado.getEmail() != null && !perfilAtualizado.getEmail().isEmpty()) {
                if (!validarDisponibilidadeEmail(perfilAtualizado.getEmail(), clienteExistente.getEmail())) {
                    return ResultadoOperacao.erro("Este email já está em uso!");
                }
            }
            
            // Validar alteração de CPF
            if (perfilAtualizado.getCpf() != null && !perfilAtualizado.getCpf().isEmpty()) {
                if (!validarDisponibilidadeCpf(perfilAtualizado.getCpf(), clienteExistente.getCpf())) {
                    return ResultadoOperacao.erro("Este CPF já está em uso!");
                }
            }
            
            // Atualizar dados do cliente
            atualizarDadosCliente(clienteExistente, perfilAtualizado);
            
            // Salvar no banco de dados
            clienteService.salvar(clienteExistente);
            
            return ResultadoOperacao.sucesso("Perfil atualizado com sucesso!");
            
        } catch (Exception e) {
            return ResultadoOperacao.erro("Erro ao atualizar perfil: " + e.getMessage());
        }
    }
    
    @Override
    public boolean validarDisponibilidadeEmail(String novoEmail, String emailAtual) {
        if (novoEmail == null || novoEmail.trim().isEmpty()) {
            return true; // Email vazio é permitido
        }
        
        // Se o email não foi alterado, está disponível
        if (novoEmail.equals(emailAtual)) {
            return true;
        }
        
        try {
            return !clienteService.existePorEmail(novoEmail);
        } catch (Exception e) {
            return false; // Em caso de erro, considerar indisponível por segurança
        }
    }
    
    @Override
    public boolean validarDisponibilidadeCpf(String novoCpf, String cpfAtual) {
        if (novoCpf == null || novoCpf.trim().isEmpty()) {
            return true; // CPF vazio pode ser permitido dependendo das regras de negócio
        }
        
        // Se o CPF não foi alterado, está disponível
        if (novoCpf.equals(cpfAtual)) {
            return true;
        }
        
        try {
            return !clienteService.existePorCpf(novoCpf);
        } catch (Exception e) {
            return false; // Em caso de erro, considerar indisponível por segurança
        }
    }
    
    /**
     * Atualiza os dados do cliente com as informações do DTO.
     * 
     * @param clienteExistente Cliente a ser atualizado
     * @param perfilAtualizado DTO com novos dados
     */
    private void atualizarDadosCliente(Cliente clienteExistente, ClientePerfilDTO perfilAtualizado) {
        clienteExistente.setNome(perfilAtualizado.getNome());
        clienteExistente.setEmail(perfilAtualizado.getEmail());
        clienteExistente.setTelefone(perfilAtualizado.getTelefone());
        clienteExistente.setCpf(perfilAtualizado.getCpf());
        // Login e status ativo não são alterados via perfil
    }
}
