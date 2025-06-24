package br.eventos.zezinEventos.service.admin;

import br.eventos.zezinEventos.model.Administrador;
import br.eventos.zezinEventos.model.dto.admin.PerfilDTO;
import br.eventos.zezinEventos.service.interfaces.admin.PerfilServiceInterface;
import br.eventos.zezinEventos.service.shared.AdministradorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Implementação do serviço para gerenciamento de perfil do administrador.
 * 
 * Esta classe segue o Princípio da Responsabilidade Única (SRP) ao focar
 * exclusivamente nas operações de perfil do administrador, delegando
 * operações básicas de CRUD para o AdministradorService.
 * 
 * Também aplica o Princípio da Inversão de Dependência (DIP) ao depender
 * de abstrações (interfaces) ao invés de implementações concretas.
 */
@Service
public class PerfilService implements PerfilServiceInterface {
    
    private final AdministradorService administradorService;
    private final PasswordEncoder passwordEncoder;
    
    @Autowired
    public PerfilService(AdministradorService administradorService, 
                        PasswordEncoder passwordEncoder) {
        this.administradorService = administradorService;
        this.passwordEncoder = passwordEncoder;
    }
    
    @Override
    public PerfilDTO obterPerfilPorLogin(String login) {
        if (login == null || login.trim().isEmpty()) {
            return null;
        }
        
        try {
            Administrador admin = administradorService.buscarPorLogin(login);
            return admin != null ? new PerfilDTO(admin) : null;
        } catch (Exception e) {
            // Log do erro seria implementado aqui
            return null;
        }
    }
    
    @Override
    public ResultadoOperacao atualizarPerfil(PerfilDTO perfilAtualizado, String loginAtual) {
        if (perfilAtualizado == null || loginAtual == null) {
            return ResultadoOperacao.erro("Dados inválidos para atualização do perfil.");
        }
        
        try {
            // Buscar administrador existente
            Administrador adminExistente = administradorService.buscarPorLogin(loginAtual);
            if (adminExistente == null) {
                return ResultadoOperacao.erro("Administrador não encontrado!");
            }
            
            // Validar alteração de login
            if (!adminExistente.getLogin().equals(perfilAtualizado.getLogin())) {
                if (!validarDisponibilidadeLogin(perfilAtualizado.getLogin(), loginAtual)) {
                    return ResultadoOperacao.erro("Este login já está em uso por outro administrador!");
                }
            }
            
            // Validar alteração de email
            if (perfilAtualizado.getEmail() != null && !perfilAtualizado.getEmail().isEmpty()) {
                if (!validarDisponibilidadeEmail(perfilAtualizado.getEmail(), adminExistente.getEmail())) {
                    return ResultadoOperacao.erro("Este email já está em uso por outro administrador!");
                }
            }
            
            // Preparar dados para atualização
            Administrador adminParaAtualizar = prepararAdministradorParaAtualizacao(
                adminExistente, perfilAtualizado);
                
            // Atualizar no banco de dados
            administradorService.atualizar(adminParaAtualizar);
            
            // Verificar se precisa de logout (login foi alterado)
            boolean loginAlterado = !adminExistente.getLogin().equals(perfilAtualizado.getLogin());
            
            if (loginAlterado) {
                return ResultadoOperacao.sucessoComLogout(
                    "Perfil atualizado com sucesso! Login alterado - faça login novamente com o novo login.");
            } else {
                return ResultadoOperacao.sucesso("Perfil atualizado com sucesso!");
            }
            
        } catch (Exception e) {
            return ResultadoOperacao.erro("Erro ao atualizar perfil: " + e.getMessage());
        }
    }
    
    @Override
    public boolean validarDisponibilidadeLogin(String novoLogin, String loginAtual) {
        if (novoLogin == null || novoLogin.trim().isEmpty()) {
            return false;
        }
        
        // Se o login não foi alterado, está disponível
        if (novoLogin.equals(loginAtual)) {
            return true;
        }
        
        try {
            Administrador adminComLogin = administradorService.buscarPorLogin(novoLogin);
            return adminComLogin == null; // Disponível se não encontrar ninguém usando
        } catch (Exception e) {
            return false; // Em caso de erro, considerar indisponível por segurança
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
            return !administradorService.existePorEmail(novoEmail);
        } catch (Exception e) {
            return false; // Em caso de erro, considerar indisponível por segurança
        }
    }
    
    /**
     * Prepara o administrador existente com os novos dados do perfil.
     * Método privado que encapsula a lógica de preparação da entidade.
     * 
     * @param adminExistente Administrador atual no banco
     * @param perfilAtualizado DTO com novos dados
     * @return Administrador preparado para atualização
     */
    private Administrador prepararAdministradorParaAtualizacao(
            Administrador adminExistente, PerfilDTO perfilAtualizado) {
        
        // Preservar ID e status
        perfilAtualizado.setId(adminExistente.getId());
        perfilAtualizado.setAtivo(adminExistente.getAtivo());
        
        // Tratar senha
        if (!perfilAtualizado.temSenhaParaAtualizar()) {
            // Manter senha atual se não foi fornecida nova senha
            perfilAtualizado.setSenha(adminExistente.getSenha());
        } else {
            // Criptografar nova senha
            String senhaEncriptada = passwordEncoder.encode(perfilAtualizado.getSenha());
            perfilAtualizado.setSenha(senhaEncriptada);
        }
        
        // Converter DTO para entidade
        return perfilAtualizado.paraEntidade();
    }
}
