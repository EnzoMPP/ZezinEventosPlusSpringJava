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
 * Controller responsável pelo gerenciamento do perfil/empresa do organizador.
 * Gerencia informações pessoais e dados da empresa.
 */
@Controller
@RequestMapping("/organizador")
@PreAuthorize("hasRole('ORGANIZADOR')")
public class OrganizadorPerfilController {

    @Autowired
    private OrganizadorPerfilServiceInterface perfilService;

    /**
     * Exibe a página de informações da empresa/perfil.
     */
    @GetMapping("/empresa")
    public String empresa(Model model, Authentication authentication) {
        try {
            OrganizadorPerfilDTO perfilData = perfilService.obterPerfilOrganizador(authentication.getName());
            
            if (perfilData == null) {
                model.addAttribute("error", "Erro ao carregar dados do perfil");
                return "organizador/empresa";
            }
            
            model.addAttribute("pageTitle", "Informações da Empresa");
            model.addAttribute("organizador", perfilData);
            
            return "organizador/empresa";
            
        } catch (Exception e) {
            model.addAttribute("error", "Erro ao carregar perfil: " + e.getMessage());
            return "organizador/empresa";
        }
    }

    /**
     * Processa a atualização das informações da empresa/perfil.
     */
    @PostMapping("/empresa/salvar")
    public String salvarEmpresa(@ModelAttribute OrganizadorPerfilDTO organizadorDto,
                               Authentication authentication,
                               RedirectAttributes redirectAttributes,
                               Model model) {
        try {
            perfilService.atualizarPerfil(organizadorDto, authentication.getName());
            
            redirectAttributes.addFlashAttribute("success", "Informações da empresa atualizadas com sucesso!");
            return "redirect:/organizador/empresa";
            
        } catch (Exception e) {
            model.addAttribute("pageTitle", "Informações da Empresa");
            model.addAttribute("organizador", organizadorDto);
            model.addAttribute("error", e.getMessage());
            return "organizador/empresa";
        }
    }
}
