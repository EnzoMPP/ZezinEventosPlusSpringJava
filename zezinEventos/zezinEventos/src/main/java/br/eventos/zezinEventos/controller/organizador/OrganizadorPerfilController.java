package br.eventos.zezinEventos.controller.organizador;

import br.eventos.zezinEventos.model.dto.organizador.OrganizadorPerfilDTO;
import br.eventos.zezinEventos.service.interfaces.organizador.OrganizadorPerfilServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controller responsável pelo gerenciamento do perfil completo do organizador.
 * Gerencia tanto informações pessoais herdadas de usuário quanto dados específicos do organizador.
 */
@Controller
@RequestMapping("/organizador")
@PreAuthorize("hasRole('ORGANIZADOR')")
public class OrganizadorPerfilController {

    @Autowired
    private OrganizadorPerfilServiceInterface perfilService;    /**
     * Exibe a página de perfil completo do organizador.
     */
    @GetMapping("/perfil")
    public String perfil(Model model, Authentication authentication) {
        try {
            OrganizadorPerfilDTO perfilData = perfilService.obterPerfilOrganizador(authentication.getName());
            
            if (perfilData == null) {
                model.addAttribute("error", "Erro ao carregar dados do perfil");
                return "organizador/perfil";
            }
            
            model.addAttribute("pageTitle", "Meu Perfil");
            model.addAttribute("organizador", perfilData);
            
            return "organizador/perfil";
            
        } catch (Exception e) {
            model.addAttribute("error", "Erro ao carregar perfil: " + e.getMessage());
            return "organizador/perfil";
        }
    }    /**
     * Processa a atualização do perfil completo do organizador.
     */
    @PostMapping("/perfil/salvar")
    public String salvarPerfil(@ModelAttribute OrganizadorPerfilDTO organizadorDto,
                              Authentication authentication,
                              RedirectAttributes redirectAttributes,
                              Model model) {
        try {
            perfilService.atualizarPerfil(organizadorDto, authentication.getName());
            
            redirectAttributes.addFlashAttribute("success", "Perfil atualizado com sucesso!");
            return "redirect:/organizador/perfil";
            
        } catch (Exception e) {
            model.addAttribute("pageTitle", "Meu Perfil");
            model.addAttribute("organizador", organizadorDto);
            model.addAttribute("error", e.getMessage());
            return "organizador/perfil";
        }
    }

    // Redirect para compatibilidade com links antigos
    @GetMapping("/empresa")
    public String empresaRedirect() {
        return "redirect:/organizador/perfil";
    }
}
