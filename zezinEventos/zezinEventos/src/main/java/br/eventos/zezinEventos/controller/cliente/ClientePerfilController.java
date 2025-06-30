package br.eventos.zezinEventos.controller.cliente;

import br.eventos.zezinEventos.model.dto.cliente.ClientePerfilDTO;
import br.eventos.zezinEventos.service.interfaces.cliente.ClientePerfilServiceInterface;
import br.eventos.zezinEventos.service.interfaces.cliente.ClientePerfilServiceInterface.ResultadoOperacao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controller especializado para gerenciamento de perfil do cliente.
 */
@Controller
@RequestMapping("/cliente/perfil")
@PreAuthorize("hasRole('CLIENTE')")
public class ClientePerfilController {
    
    private final ClientePerfilServiceInterface perfilService;
    
    @Autowired
    public ClientePerfilController(ClientePerfilServiceInterface perfilService) {
        this.perfilService = perfilService;
    }
    
    /**
     * Exibe a página de perfil do cliente.
     * 
     * @param model Modelo para a view
     * @param authentication Dados de autenticação do usuário
     * @return Nome da view
     */
    @GetMapping
    public String exibirPerfil(Model model, Authentication authentication) {
        try {
            String login = authentication.getName();
            ClientePerfilDTO perfil = perfilService.obterPerfilPorLogin(login);
            
            if (perfil == null) {
                return "redirect:/cliente/home?erro=ClienteNaoEncontrado";
            }
            
            model.addAttribute("pageTitle", "Meu Perfil");
            model.addAttribute("cliente", perfil);
            return "cliente/perfil";
            
        } catch (Exception e) {
            return "redirect:/cliente/home?erro=ErroAoCarregarPerfil";
        }
    }
    
    /**
     * Processa a atualização do perfil do cliente.
     * 
     * @param perfilAtualizado DTO com dados atualizados do perfil
     * @param authentication Dados de autenticação do usuário
     * @param redirectAttributes Atributos para redirect
     * @return Redirecionamento para perfil
     */
    @PostMapping("/editar")
    public String atualizarPerfil(@ModelAttribute ClientePerfilDTO perfilAtualizado,
                                 Authentication authentication,
                                 RedirectAttributes redirectAttributes) {
        try {
            String loginAtual = authentication.getName();
            
            // Validação básica
            if (!validarDadosBasicos(perfilAtualizado, redirectAttributes)) {
                return "redirect:/cliente/perfil";
            }
            
            // Processar atualização via service
            ResultadoOperacao resultado = perfilService.atualizarPerfil(perfilAtualizado, loginAtual);
            
            // Tratar resultado
            if (resultado.isSucesso()) {
                redirectAttributes.addFlashAttribute("success", resultado.getMensagem());
            } else {
                redirectAttributes.addFlashAttribute("error", resultado.getMensagem());
            }
            
            return "redirect:/cliente/perfil";
            
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", 
                "Erro inesperado ao atualizar perfil: " + e.getMessage());
            return "redirect:/cliente/perfil";
        }
    }
    
    /**
     * Endpoint para validação AJAX de disponibilidade de email.
     * 
     * @param email Email a ser validado
     * @param authentication Dados de autenticação
     * @return Resposta JSON com disponibilidade
     */
    @GetMapping("/validar-email")
    @ResponseBody
    public boolean validarDisponibilidadeEmail(@RequestParam String email, 
                                              Authentication authentication) {
        try {
            // Obter email atual do perfil
            String loginAtual = authentication.getName();
            ClientePerfilDTO perfilAtual = perfilService.obterPerfilPorLogin(loginAtual);
            String emailAtual = (perfilAtual != null) ? perfilAtual.getEmail() : null;
            
            return perfilService.validarDisponibilidadeEmail(email, emailAtual);
        } catch (Exception e) {
            return false; // Em caso de erro, considerar indisponível
        }
    }
    
    /**
     * Endpoint para validação AJAX de disponibilidade de CPF.
     * 
     * @param cpf CPF a ser validado
     * @param authentication Dados de autenticação
     * @return Resposta JSON com disponibilidade
     */
    @GetMapping("/validar-cpf")
    @ResponseBody
    public boolean validarDisponibilidadeCpf(@RequestParam String cpf, 
                                            Authentication authentication) {
        try {
            // Obter CPF atual do perfil
            String loginAtual = authentication.getName();
            ClientePerfilDTO perfilAtual = perfilService.obterPerfilPorLogin(loginAtual);
            String cpfAtual = (perfilAtual != null) ? perfilAtual.getCpf() : null;
            
            return perfilService.validarDisponibilidadeCpf(cpf, cpfAtual);
        } catch (Exception e) {
            return false; // Em caso de erro, considerar indisponível
        }
    }
    
    // ==================== MÉTODOS PRIVADOS ====================
    
    /**
     * Valida os dados básicos do perfil antes de processar.
     * 
     * @param perfil DTO com dados do perfil
     * @param redirectAttributes Atributos para mensagens de erro
     * @return true se dados são válidos, false caso contrário
     */
    private boolean validarDadosBasicos(ClientePerfilDTO perfil, RedirectAttributes redirectAttributes) {
        if (perfil == null) {
            redirectAttributes.addFlashAttribute("error", "Dados do perfil não foram recebidos!");
            return false;
        }
        
        if (perfil.getNome() == null || perfil.getNome().trim().isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Nome é obrigatório!");
            return false;
        }
        
        // Validação de email (se fornecido)
        if (perfil.getEmail() != null && !perfil.getEmail().isEmpty()) {
            if (!isEmailValido(perfil.getEmail())) {
                redirectAttributes.addFlashAttribute("error", "Email possui formato inválido!");
                return false;
            }
        }
        
        return true;
    }
    
    /**
     * Validação simples de formato de email.
     * 
     * @param email Email a ser validado
     * @return true se formato é válido
     */
    private boolean isEmailValido(String email) {
        return email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    }
}
