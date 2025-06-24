package br.eventos.zezinEventos.controller.admin;

import br.eventos.zezinEventos.model.dto.admin.PerfilDTO;
import br.eventos.zezinEventos.service.interfaces.admin.PerfilServiceInterface;
import br.eventos.zezinEventos.service.interfaces.admin.PerfilServiceInterface.ResultadoOperacao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controller especializado para gerenciamento de perfil do administrador.
 * 
 * Este controller segue o Princípio da Responsabilidade Única (SRP)
 * ao focar exclusivamente nas operações de perfil administrativo.
 * 
 * Também aplica o Princípio da Inversão de Dependência (DIP) ao depender
 * da interface PerfilServiceInterface ao invés da implementação concreta.
 */
@Controller
@RequestMapping("/admin/perfil")
@PreAuthorize("hasRole('ADMIN')")
public class AdminPerfilController {
    
    private final PerfilServiceInterface perfilService;
    
    @Autowired
    public AdminPerfilController(PerfilServiceInterface perfilService) {
        this.perfilService = perfilService;
    }
    
    /**
     * Exibe a página de perfil do administrador.
     * 
     * @param model Modelo para a view
     * @param authentication Dados de autenticação do usuário
     * @return Nome da view ou redirecionamento em caso de erro
     */
    @GetMapping
    public String exibirPerfil(Model model, Authentication authentication) {
        try {
            String login = authentication.getName();
            PerfilDTO perfil = perfilService.obterPerfilPorLogin(login);
            
            if (perfil == null) {
                return "redirect:/admin/home?erro=AdminNaoEncontrado";
            }
            
            model.addAttribute("admin", perfil);
            return "admin/perfil";
            
        } catch (Exception e) {
            return "redirect:/admin/home?erro=ErroAoCarregarPerfil";
        }
    }
    
    /**
     * Processa a atualização do perfil do administrador.
     * 
     * @param perfilAtualizado DTO com dados atualizados do perfil
     * @param authentication Dados de autenticação do usuário
     * @param redirectAttributes Atributos para redirect
     * @return Redirecionamento para perfil ou logout
     */
    @PostMapping("/editar")
    public String atualizarPerfil(@ModelAttribute PerfilDTO perfilAtualizado,
                                 Authentication authentication,
                                 RedirectAttributes redirectAttributes) {
        try {
            String loginAtual = authentication.getName();
            
            // Validação básica
            if (!validarDadosBasicos(perfilAtualizado, redirectAttributes)) {
                return "redirect:/admin/perfil";
            }
            
            // Processar atualização via service
            ResultadoOperacao resultado = perfilService.atualizarPerfil(perfilAtualizado, loginAtual);
            
            // Tratar resultado
            return processarResultadoAtualizacao(resultado, redirectAttributes);
            
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erro", 
                "Erro inesperado ao atualizar perfil: " + e.getMessage());
            return "redirect:/admin/perfil";
        }
    }
    
    /**
     * Endpoint para validação AJAX de disponibilidade de login.
     * 
     * @param login Login a ser validado
     * @param authentication Dados de autenticação
     * @return Resposta JSON com disponibilidade
     */
    @GetMapping("/validar-login")
    @ResponseBody
    public boolean validarDisponibilidadeLogin(@RequestParam String login, 
                                              Authentication authentication) {
        try {
            String loginAtual = authentication.getName();
            return perfilService.validarDisponibilidadeLogin(login, loginAtual);
        } catch (Exception e) {
            return false; // Em caso de erro, considerar indisponível
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
            PerfilDTO perfilAtual = perfilService.obterPerfilPorLogin(loginAtual);
            String emailAtual = (perfilAtual != null) ? perfilAtual.getEmail() : null;
            
            return perfilService.validarDisponibilidadeEmail(email, emailAtual);
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
    private boolean validarDadosBasicos(PerfilDTO perfil, RedirectAttributes redirectAttributes) {
        if (perfil == null) {
            redirectAttributes.addFlashAttribute("erro", "Dados do perfil não foram recebidos!");
            return false;
        }
        
        if (perfil.getNome() == null || perfil.getNome().trim().isEmpty()) {
            redirectAttributes.addFlashAttribute("erro", "Nome é obrigatório!");
            return false;
        }
        
        if (perfil.getLogin() == null || perfil.getLogin().trim().isEmpty()) {
            redirectAttributes.addFlashAttribute("erro", "Login é obrigatório!");
            return false;
        }
        
        // Validação de email (se fornecido)
        if (perfil.getEmail() != null && !perfil.getEmail().isEmpty()) {
            if (!isEmailValido(perfil.getEmail())) {
                redirectAttributes.addFlashAttribute("erro", "Email possui formato inválido!");
                return false;
            }
        }
        
        return true;
    }
    
    /**
     * Processa o resultado da operação de atualização.
     * 
     * @param resultado Resultado da operação
     * @param redirectAttributes Atributos para mensagens
     * @return Redirecionamento apropriado
     */
    private String processarResultadoAtualizacao(ResultadoOperacao resultado, 
                                                RedirectAttributes redirectAttributes) {
        if (resultado.isSucesso()) {
            if (resultado.isRequerLogout()) {
                redirectAttributes.addFlashAttribute("aviso", resultado.getMensagem());
                return "redirect:/logout";
            } else {
                redirectAttributes.addFlashAttribute("sucesso", resultado.getMensagem());
                return "redirect:/admin/perfil";
            }
        } else {
            redirectAttributes.addFlashAttribute("erro", resultado.getMensagem());
            return "redirect:/admin/perfil";
        }
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
