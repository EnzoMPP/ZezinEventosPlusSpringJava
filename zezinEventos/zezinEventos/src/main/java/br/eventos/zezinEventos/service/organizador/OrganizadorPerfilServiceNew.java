package br.eventos.zezinEventos.service.organizador;

import br.eventos.zezinEventos.model.Organizador;
import br.eventos.zezinEventos.model.dto.organizador.OrganizadorPerfilDTO;
import br.eventos.zezinEventos.service.interfaces.organizador.OrganizadorPerfilServiceInterface;
import br.eventos.zezinEventos.service.shared.OrganizadorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implementação do serviço para gerenciamento de perfil/empresa do organizador.
 * 
 * Esta classe segue o Princípio da Responsabilidade Única (SRP) ao focar
 * exclusivamente nas operações de perfil do organizador.
 */
@Service
public class OrganizadorPerfilService implements OrganizadorPerfilServiceInterface {
    
    private final OrganizadorService organizadorService;
    
    @Autowired
    public OrganizadorPerfilService(OrganizadorService organizadorService) {
        this.organizadorService = organizadorService;
    }
    
    @Override
    public OrganizadorPerfilDTO obterPerfilOrganizador(String login) {
        if (login == null || login.trim().isEmpty()) {
            return null;
        }
        
        try {
            Organizador organizador = organizadorService.buscarPorLogin(login);
            if (organizador == null) {
                return null;
            }
            
            return new OrganizadorPerfilDTO(organizador);
            
        } catch (Exception e) {
            return null;
        }
    }
    
    @Override
    public void atualizarPerfil(OrganizadorPerfilDTO perfilAtualizado, String loginAtual) {
        try {
            // Validações básicas
            if (perfilAtualizado == null) {
                throw new RuntimeException("Dados do perfil são obrigatórios");
            }
            
            if (loginAtual == null || loginAtual.trim().isEmpty()) {
                throw new RuntimeException("Login atual é obrigatório");
            }
            
            // Buscar organizador atual
            Organizador organizadorAtual = organizadorService.buscarPorLogin(loginAtual);
            if (organizadorAtual == null) {
                throw new RuntimeException("Organizador não encontrado");
            }
            
            // Validações de duplicidade
            if (!organizadorAtual.getEmail().equals(perfilAtualizado.getEmail()) && 
                organizadorService.existeEmail(perfilAtualizado.getEmail())) {
                throw new RuntimeException("Este email já está sendo usado por outro organizador.");
            }
            
            if (!organizadorAtual.getCnpj().equals(perfilAtualizado.getCnpj()) && 
                organizadorService.existeCnpj(perfilAtualizado.getCnpj())) {
                throw new RuntimeException("Este CNPJ já está sendo usado por outro organizador.");
            }
            
            // Atualizar dados do organizador
            organizadorAtual.setNome(perfilAtualizado.getNome());
            organizadorAtual.setEmail(perfilAtualizado.getEmail());
            organizadorAtual.setTelefone(perfilAtualizado.getTelefone());
            organizadorAtual.setEmpresa(perfilAtualizado.getEmpresa());
            organizadorAtual.setCnpj(perfilAtualizado.getCnpj());
            
            // Salvar organizador
            organizadorService.salvar(organizadorAtual);
            
        } catch (Exception e) {
            throw new RuntimeException("Erro ao atualizar informações: " + e.getMessage());
        }
    }
    
    @Override
    public boolean validarDisponibilidadeEmail(String novoEmail, String emailAtual) {
        if (novoEmail == null || novoEmail.trim().isEmpty()) {
            return false;
        }
        
        // Se o email não mudou, está disponível
        if (novoEmail.equals(emailAtual)) {
            return true;
        }
        
        return !organizadorService.existeEmail(novoEmail);
    }
    
    @Override
    public boolean validarDisponibilidadeCnpj(String novoCnpj, String cnpjAtual) {
        if (novoCnpj == null || novoCnpj.trim().isEmpty()) {
            return false;
        }
        
        // Se o CNPJ não mudou, está disponível
        if (novoCnpj.equals(cnpjAtual)) {
            return true;
        }
        
        return !organizadorService.existeCnpj(novoCnpj);
    }
}
