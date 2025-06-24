package br.eventos.zezinEventos.service.admin;

import br.eventos.zezinEventos.model.Administrador;
import br.eventos.zezinEventos.model.Cliente;
import br.eventos.zezinEventos.model.Organizador;
import br.eventos.zezinEventos.model.dto.UsuarioDTO;
import br.eventos.zezinEventos.model.dto.UsuariosListaDTO;
import br.eventos.zezinEventos.service.interfaces.admin.UsuarioServiceInterface;
import br.eventos.zezinEventos.service.shared.ClienteService;
import br.eventos.zezinEventos.service.shared.OrganizadorService;
import br.eventos.zezinEventos.service.shared.AdministradorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Service para gerenciamento de usuários administrativos
 * Implementa princípios SOLID: SRP, OCP, DIP
 * Organizado na estrutura admin/ para melhor navegabilidade
 */
@Service
public class UsuarioService implements UsuarioServiceInterface {

    @Autowired
    private ClienteService clienteService;
    
    @Autowired
    private OrganizadorService organizadorService;
    
    @Autowired
    private AdministradorService administradorService;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UsuariosListaDTO listarUsuarios(String busca) {
        // Buscar usuários por tipo
        List<Cliente> clientes = buscarClientes(busca);
        List<Organizador> organizadores = buscarOrganizadores(busca);
        List<Administrador> administradores = buscarAdministradores(busca);
        
        // Consolidar em DTOs
        List<UsuarioDTO> todosUsuarios = new ArrayList<>();
        todosUsuarios.addAll(converterClientesParaDTO(clientes));
        todosUsuarios.addAll(converterOrganizadoresParaDTO(organizadores));
        todosUsuarios.addAll(converterAdministradoresParaDTO(administradores));
        
        // Construir DTO de resposta
        return new UsuariosListaDTO(
            todosUsuarios,
            busca,
            todosUsuarios.size(),
            clientes.size(),
            organizadores.size(),
            administradores.size()
        );
    }

    @Override
    public void desativarUsuario(String tipo, Long id, String loginAtual) {
        switch (tipo.toUpperCase()) {
            case "CLIENTE":
                desativarCliente(id);
                break;
            case "ORGANIZADOR":
                desativarOrganizador(id);
                break;
            case "ADMIN":
                desativarAdministrador(id, loginAtual);
                break;
            default:
                throw new IllegalArgumentException("Tipo de usuário inválido: " + tipo);
        }
    }

    @Override
    public Object buscarUsuarioParaEdicao(String tipo, Long id) {
        switch (tipo.toUpperCase()) {
            case "CLIENTE":
                return clienteService.buscarPorId(id);
            case "ORGANIZADOR":
                return organizadorService.buscarPorId(id);
            case "ADMIN":
                return administradorService.buscarPorId(id);
            default:
                throw new IllegalArgumentException("Tipo de usuário inválido: " + tipo);
        }
    }

    @Override
    public void atualizarCliente(Long id, Cliente cliente, String novaSenha, String confirmarSenha) {
        Cliente clienteExistente = clienteService.buscarPorId(id);
        if (clienteExistente == null) {
            throw new IllegalArgumentException("Cliente não encontrado.");
        }
        
        validarESalvarSenha(cliente, clienteExistente, novaSenha, confirmarSenha);
        cliente.setId(id);
        clienteService.salvar(cliente);
    }

    @Override
    public void atualizarOrganizador(Long id, Organizador organizador, String novaSenha, String confirmarSenha) {
        Organizador organizadorExistente = organizadorService.buscarPorId(id);
        if (organizadorExistente == null) {
            throw new IllegalArgumentException("Organizador não encontrado.");
        }
        
        validarESalvarSenha(organizador, organizadorExistente, novaSenha, confirmarSenha);
        organizador.setId(id);
        organizadorService.salvar(organizador);
    }

    @Override
    public void atualizarAdministrador(Long id, Administrador administrador, String novaSenha, String confirmarSenha) {
        Administrador administradorExistente = administradorService.buscarPorId(id);
        if (administradorExistente == null) {
            throw new IllegalArgumentException("Administrador não encontrado.");
        }
        
        validarESalvarSenha(administrador, administradorExistente, novaSenha, confirmarSenha);
        administrador.setId(id);
        administradorService.salvar(administrador);
    }

    @Override
    public void criarNovoAdministrador(Administrador administrador) {
        validarDadosNovoAdministrador(administrador);
        
        // Criptografar senha
        administrador.setSenha(passwordEncoder.encode(administrador.getSenha()));
        administrador.setAtivo(true);
        
        administradorService.salvar(administrador);
    }

    // Métodos privados para encapsular lógica específica
    
    private List<Cliente> buscarClientes(String busca) {
        if (busca != null && !busca.trim().isEmpty()) {
            return clienteService.buscarPorNome(busca);
        }
        return clienteService.listarTodos();
    }

    private List<Organizador> buscarOrganizadores(String busca) {
        if (busca != null && !busca.trim().isEmpty()) {
            return organizadorService.buscarPorNome(busca);
        }
        return organizadorService.listarTodos();
    }

    private List<Administrador> buscarAdministradores(String busca) {
        if (busca != null && !busca.trim().isEmpty()) {
            return administradorService.buscarPorNome(busca);
        }
        return administradorService.listarTodos();
    }

    private List<UsuarioDTO> converterClientesParaDTO(List<Cliente> clientes) {
        List<UsuarioDTO> usuariosDTO = new ArrayList<>();
        for (Cliente cliente : clientes) {
            if (cliente.getAtivo()) {
                usuariosDTO.add(new UsuarioDTO(
                    cliente.getId(),
                    cliente.getNome(),
                    cliente.getEmail(),
                    cliente.getLogin(),
                    cliente.getAtivo(),
                    "CLIENTE",
                    "Cliente",
                    "bg-success",
                    cliente.getCpf(),
                    cliente.getTelefone(),
                    null,
                    null
                ));
            }
        }
        return usuariosDTO;
    }

    private List<UsuarioDTO> converterOrganizadoresParaDTO(List<Organizador> organizadores) {
        List<UsuarioDTO> usuariosDTO = new ArrayList<>();
        for (Organizador organizador : organizadores) {
            if (organizador.getAtivo()) {
                usuariosDTO.add(new UsuarioDTO(
                    organizador.getId(),
                    organizador.getNome(),
                    organizador.getEmail(),
                    organizador.getLogin(),
                    organizador.getAtivo(),
                    "ORGANIZADOR",
                    "Organizador",
                    "bg-warning",
                    organizador.getCnpj(),
                    organizador.getTelefone(),
                    organizador.getEmpresa(),
                    null
                ));
            }
        }
        return usuariosDTO;
    }

    private List<UsuarioDTO> converterAdministradoresParaDTO(List<Administrador> administradores) {
        List<UsuarioDTO> usuariosDTO = new ArrayList<>();
        for (Administrador admin : administradores) {
            if (admin.getAtivo()) {
                usuariosDTO.add(new UsuarioDTO(
                    admin.getId(),
                    admin.getNome(),
                    admin.getEmail(),
                    admin.getLogin(),
                    admin.getAtivo(),
                    "ADMIN",
                    "Administrador",
                    "bg-danger",
                    null,
                    admin.getTelefone(),
                    null,
                    admin.getCargo()
                ));
            }
        }
        return usuariosDTO;
    }

    private void desativarCliente(Long id) {
        Cliente cliente = clienteService.buscarPorId(id);
        cliente.setAtivo(false);
        clienteService.salvar(cliente);
    }

    private void desativarOrganizador(Long id) {
        Organizador organizador = organizadorService.buscarPorId(id);
        organizador.setAtivo(false);
        organizadorService.salvar(organizador);
    }

    private void desativarAdministrador(Long id, String loginAtual) {
        Administrador admin = administradorService.buscarPorId(id);
        if (admin.getLogin().equals(loginAtual)) {
            throw new IllegalArgumentException("Você não pode excluir sua própria conta!");
        }
        admin.setAtivo(false);
        administradorService.salvar(admin);
    }

    private void validarESalvarSenha(Object usuario, Object usuarioExistente, String novaSenha, String confirmarSenha) {
        if (novaSenha != null && !novaSenha.trim().isEmpty()) {
            if (confirmarSenha == null || !novaSenha.equals(confirmarSenha)) {
                throw new IllegalArgumentException("Senha e confirmação de senha não conferem.");
            }
            
            // Usar reflection ou instanceof para definir senha
            if (usuario instanceof Cliente) {
                ((Cliente) usuario).setSenha(passwordEncoder.encode(novaSenha));
            } else if (usuario instanceof Organizador) {
                ((Organizador) usuario).setSenha(passwordEncoder.encode(novaSenha));
            } else if (usuario instanceof Administrador) {
                ((Administrador) usuario).setSenha(passwordEncoder.encode(novaSenha));
            }
        } else {
            // Preservar senha existente
            if (usuario instanceof Cliente && usuarioExistente instanceof Cliente) {
                ((Cliente) usuario).setSenha(((Cliente) usuarioExistente).getSenha());
            } else if (usuario instanceof Organizador && usuarioExistente instanceof Organizador) {
                ((Organizador) usuario).setSenha(((Organizador) usuarioExistente).getSenha());
            } else if (usuario instanceof Administrador && usuarioExistente instanceof Administrador) {
                ((Administrador) usuario).setSenha(((Administrador) usuarioExistente).getSenha());
            }
        }
    }

    private void validarDadosNovoAdministrador(Administrador administrador) {
        if (administrador.getNome() == null || administrador.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("Nome é obrigatório!");
        }
        
        if (administrador.getEmail() == null || administrador.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("Email é obrigatório!");
        }
        
        if (administrador.getLogin() == null || administrador.getLogin().trim().isEmpty()) {
            throw new IllegalArgumentException("Login é obrigatório!");
        }
        
        if (administrador.getSenha() == null || administrador.getSenha().trim().isEmpty()) {
            throw new IllegalArgumentException("Senha é obrigatória!");
        }
        
        if (administradorService.existePorLogin(administrador.getLogin())) {
            throw new IllegalArgumentException("Login já existe! Escolha outro.");
        }
        
        if (administradorService.existePorEmail(administrador.getEmail())) {
            throw new IllegalArgumentException("Email já existe! Escolha outro.");
        }
    }
}
