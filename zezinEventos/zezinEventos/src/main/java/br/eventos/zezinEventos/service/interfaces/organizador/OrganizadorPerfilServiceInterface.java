package br.eventos.zezinEventos.service.interfaces.organizador;

import br.eventos.zezinEventos.model.dto.organizador.OrganizadorPerfilDTO;

/**
 * Interface para serviços relacionados ao gerenciamento de perfil completo do organizador.
 * Define operações específicas para visualização e atualização de todos os dados do perfil.
 */
public interface OrganizadorPerfilServiceInterface {
    
    /**
     * Busca e prepara os dados completos do perfil do organizador para visualização.
     * 
     * @param login Login do organizador autenticado
     * @return DTO com dados do perfil ou null se não encontrado
     */
    OrganizadorPerfilDTO obterPerfilOrganizador(String login);
    
    /**
     * Atualiza o perfil completo do organizador com validações de negócio.
     * 
     * @param perfilAtualizado DTO com dados atualizados do perfil
     * @param loginAtual Login atual do organizador (para validações)
     */
    void atualizarPerfil(OrganizadorPerfilDTO perfilAtualizado, String loginAtual);
    
    /**
     * Valida se um email está disponível para uso.
     * 
     * @param novoEmail Email a ser validado
     * @param emailAtual Email atual do organizador (para excluir da validação)
     * @return true se o email está disponível, false caso contrário
     */
    boolean validarDisponibilidadeEmail(String novoEmail, String emailAtual);
    
    /**
     * Valida se um CNPJ está disponível para uso.
     * 
     * @param novoCnpj CNPJ a ser validado
     * @param cnpjAtual CNPJ atual do organizador (para excluir da validação)
     * @return true se o CNPJ está disponível, false caso contrário
     */
    boolean validarDisponibilidadeCnpj(String novoCnpj, String cnpjAtual);
    
    /**
     * Valida se um login está disponível para uso.
     * 
     * @param novoLogin Login a ser validado
     * @param loginAtual Login atual do organizador (para excluir da validação)
     * @return true se o login está disponível, false caso contrário
     */
    boolean validarDisponibilidadeLogin(String novoLogin, String loginAtual);
}
