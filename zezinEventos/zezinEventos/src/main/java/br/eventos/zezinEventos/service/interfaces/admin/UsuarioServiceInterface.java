package br.eventos.zezinEventos.service.interfaces.admin;

import br.eventos.zezinEventos.model.Administrador;
import br.eventos.zezinEventos.model.dto.admin.UsuariosListaDTO;

/**
 * Interface para service de gerenciamento de usuários administrativos
 * Seguindo o Princípio da Inversão de Dependência (DIP)
 * Organizada na estrutura admin/ para melhor navegabilidade
 */
public interface UsuarioServiceInterface {
    
    /**
     * Busca e consolida todos os usuários do sistema
     * @param busca termo de busca (opcional)
     * @return DTO com lista de usuários e estatísticas
     */
    UsuariosListaDTO listarUsuarios(String busca);
    
    /**
     * Desativa um usuário do sistema
     * @param tipo tipo do usuário (CLIENTE, ORGANIZADOR, ADMIN)
     * @param id ID do usuário
     * @param loginAtual login do administrador que está fazendo a operação
     * @throws IllegalArgumentException se tentar desativar própria conta
     */
    void desativarUsuario(String tipo, Long id, String loginAtual);
    
    /**
     * Busca um usuário por tipo e ID para edição
     * @param tipo tipo do usuário
     * @param id ID do usuário
     * @return objeto do usuário ou null se não encontrado
     */
    Object buscarUsuarioParaEdicao(String tipo, Long id);
    
    /**
     * Atualiza os dados de um cliente
     * @param id ID do cliente
     * @param cliente dados atualizados
     * @param novaSenha nova senha (opcional)
     * @param confirmarSenha confirmação da nova senha
     * @throws IllegalArgumentException se dados inválidos
     */
    void atualizarCliente(Long id, br.eventos.zezinEventos.model.Cliente cliente, 
                         String novaSenha, String confirmarSenha);
    
    /**
     * Atualiza os dados de um organizador
     * @param id ID do organizador
     * @param organizador dados atualizados
     * @param novaSenha nova senha (opcional)
     * @param confirmarSenha confirmação da nova senha
     * @throws IllegalArgumentException se dados inválidos
     */
    void atualizarOrganizador(Long id, br.eventos.zezinEventos.model.Organizador organizador,
                             String novaSenha, String confirmarSenha);
    
    /**
     * Atualiza os dados de um administrador
     * @param id ID do administrador
     * @param administrador dados atualizados
     * @param novaSenha nova senha (opcional)
     * @param confirmarSenha confirmação da nova senha
     * @throws IllegalArgumentException se dados inválidos
     */
    void atualizarAdministrador(Long id, Administrador administrador,
                               String novaSenha, String confirmarSenha);
    
    /**
     * Cria um novo administrador
     * @param administrador dados do novo administrador
     * @throws IllegalArgumentException se dados inválidos ou login/email já existem
     */
    void criarNovoAdministrador(Administrador administrador);
}
