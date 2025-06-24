package br.eventos.zezinEventos.service.interfaces.organizador;

import br.eventos.zezinEventos.model.dto.organizador.OrganizadorPerfilDTO;

/**
 * Interface para serviços relacionados ao gerenciamento de perfil/empresa do organizador.
 * Define operações específicas para visualização e atualização do perfil organizacional.
 */
public interface OrganizadorPerfilServiceInterface {
      /**
     * Busca e prepara os dados do perfil/empresa do organizador para visualização.
     * 
     * @param login Login do organizador autenticado
     * @return DTO com dados do perfil ou null se não encontrado
     */
    OrganizadorPerfilDTO obterPerfilOrganizador(String login);
      /**
     * Atualiza o perfil/empresa do organizador com validações de negócio.
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
}
