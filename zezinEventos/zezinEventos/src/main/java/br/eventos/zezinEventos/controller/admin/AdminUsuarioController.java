package br.eventos.zezinEventos.controller.admin;

import br.eventos.zezinEventos.model.Administrador;
import br.eventos.zezinEventos.model.Cliente;
import br.eventos.zezinEventos.model.Organizador;
import br.eventos.zezinEventos.model.dto.UsuariosListaDTO;
import br.eventos.zezinEventos.service.interfaces.admin.UsuarioServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controller responsável pelo gerenciamento de usuários administrativos
 * Seguindo o Princípio da Responsabilidade Única (SRP)
 * Usa injeção de dependência (DIP) para desacoplamento
 */
@Controller
@RequestMapping("/admin/usuarios")
@PreAuthorize("hasRole('ADMIN')")
public class AdminUsuarioController {

    private final UsuarioServiceInterface usuarioService;

    @Autowired
    public AdminUsuarioController(UsuarioServiceInterface usuarioService) {
        this.usuarioService = usuarioService;
    }

    /**
     * Lista todos os usuários do sistema com busca opcional
     */
    @GetMapping
    public String listarUsuarios(Model model, @RequestParam(required = false) String busca) {
        try {
            UsuariosListaDTO usuariosData = usuarioService.listarUsuarios(busca);
            
            model.addAttribute("pageTitle", "Gerenciar Usuários");
            model.addAttribute("usuarios", usuariosData.getUsuarios());
            model.addAttribute("busca", usuariosData.getBusca());
            model.addAttribute("totalUsuarios", usuariosData.getTotalUsuarios());
            model.addAttribute("totalClientes", usuariosData.getTotalClientes());
            model.addAttribute("totalOrganizadores", usuariosData.getTotalOrganizadores());
            model.addAttribute("totalAdministradores", usuariosData.getTotalAdministradores());
            
            return "admin/usuarios";
        } catch (Exception e) {
            model.addAttribute("error", "Erro ao carregar usuários: " + e.getMessage());
            return "admin/usuarios";
        }
    }

    /**
     * Desativa um usuário do sistema
     */
    @PostMapping("/desativar/{tipo}/{id}")
    public String desativarUsuario(@PathVariable String tipo, 
                                  @PathVariable Long id,
                                  Authentication authentication,
                                  RedirectAttributes redirectAttributes) {
        try {
            String loginAtual = authentication.getName();
            usuarioService.desativarUsuario(tipo, id, loginAtual);
            redirectAttributes.addFlashAttribute("success", "Usuário excluído com sucesso!");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erro ao excluir usuário: " + e.getMessage());
        }
        
        return "redirect:/admin/usuarios";
    }

    /**
     * Exibe formulário para editar usuário
     */
    @GetMapping("/editar/{tipo}/{id}")
    public String editarUsuario(@PathVariable String tipo, 
                               @PathVariable Long id,
                               Model model,
                               RedirectAttributes redirectAttributes) {
        try {
            Object usuario = usuarioService.buscarUsuarioParaEdicao(tipo, id);
            if (usuario == null) {
                redirectAttributes.addFlashAttribute("error", "Usuário não encontrado!");
                return "redirect:/admin/usuarios";
            }
            
            model.addAttribute("usuario", usuario);
            model.addAttribute("tipoUsuario", tipo.toUpperCase());
            model.addAttribute("pageTitle", "Editar " + tipo.toLowerCase());
            
            return "admin/editar-usuario";
            
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/admin/usuarios";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erro ao carregar usuário: " + e.getMessage());
            return "redirect:/admin/usuarios";
        }
    }

    /**
     * Salva edição de cliente
     */
    @PostMapping("/editar/CLIENTE/{id}")
    public String salvarEdicaoCliente(@PathVariable Long id,
                                     @ModelAttribute("usuario") Cliente cliente,
                                     @RequestParam(value = "senha", required = false) String novaSenha,
                                     @RequestParam(value = "confirmarSenha", required = false) String confirmarSenha,
                                     RedirectAttributes redirectAttributes) {
        try {
            usuarioService.atualizarCliente(id, cliente, novaSenha, confirmarSenha);
            redirectAttributes.addFlashAttribute("success", "Cliente editado com sucesso!");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/admin/usuarios/editar/CLIENTE/" + id;
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erro ao salvar cliente: " + e.getMessage());
        }
        
        return "redirect:/admin/usuarios";
    }

    /**
     * Salva edição de organizador
     */
    @PostMapping("/editar/ORGANIZADOR/{id}")
    public String salvarEdicaoOrganizador(@PathVariable Long id,
                                         @ModelAttribute("usuario") Organizador organizador,
                                         @RequestParam(required = false) String senha,
                                         @RequestParam(required = false) String confirmarSenha,
                                         RedirectAttributes redirectAttributes) {
        try {
            usuarioService.atualizarOrganizador(id, organizador, senha, confirmarSenha);
            redirectAttributes.addFlashAttribute("success", "Organizador editado com sucesso!");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/admin/usuarios/editar/ORGANIZADOR/" + id;
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erro ao salvar organizador: " + e.getMessage());
        }
        
        return "redirect:/admin/usuarios";
    }

    /**
     * Salva edição de administrador
     */
    @PostMapping("/editar/ADMIN/{id}")
    public String salvarEdicaoAdmin(@PathVariable Long id,
                                   @ModelAttribute("usuario") Administrador administrador,
                                   @RequestParam(required = false) String senha,
                                   @RequestParam(required = false) String confirmarSenha,
                                   RedirectAttributes redirectAttributes) {
        try {
            usuarioService.atualizarAdministrador(id, administrador, senha, confirmarSenha);
            redirectAttributes.addFlashAttribute("success", "Administrador editado com sucesso!");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/admin/usuarios/editar/ADMIN/" + id;
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erro ao salvar administrador: " + e.getMessage());
        }
        
        return "redirect:/admin/usuarios";
    }

    /**
     * Exibe formulário para criar novo administrador
     */
    @GetMapping("/novo-admin")
    public String novoAdmin(Model model) {
        model.addAttribute("administrador", new Administrador());
        model.addAttribute("pageTitle", "Cadastrar Novo Administrador");
        return "admin/novo-admin";
    }

    /**
     * Salva novo administrador
     */
    @PostMapping("/novo-admin")
    public String salvarNovoAdmin(@ModelAttribute("administrador") Administrador administrador,
                                 RedirectAttributes redirectAttributes) {
        try {
            usuarioService.criarNovoAdministrador(administrador);
            redirectAttributes.addFlashAttribute("success", "Administrador criado com sucesso!");
            return "redirect:/admin/usuarios";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/admin/usuarios/novo-admin";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erro ao criar administrador: " + e.getMessage());
            return "redirect:/admin/usuarios/novo-admin";
        }
    }
}
