package br.eventos.zezinEventos.controller.admin;

import br.eventos.zezinEventos.model.Administrador;
import br.eventos.zezinEventos.model.Cliente;
import br.eventos.zezinEventos.model.Evento;
import br.eventos.zezinEventos.model.Organizador;
import br.eventos.zezinEventos.service.shared.AdministradorService;
import br.eventos.zezinEventos.service.shared.ClienteService;
import br.eventos.zezinEventos.service.shared.EventoService;
import br.eventos.zezinEventos.service.shared.InscricaoService;
import br.eventos.zezinEventos.service.shared.OrganizadorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {    @Autowired
    private EventoService eventoService;
    
    @Autowired
    private ClienteService clienteService;
    
    @Autowired
    private OrganizadorService organizadorService;
      @Autowired
    private AdministradorService administradorService;
    
    @Autowired
    private InscricaoService inscricaoService;
      @Autowired
    private PasswordEncoder passwordEncoder;    // Dashboard foi movido para AdminDashboardController
    // Usuários foi movido para AdminUsuarioController
    // Eventos foi movido para AdminEventoController
    // Relatórios foi movido para AdminRelatorioController
    // seguindo o princípio da Responsabilidade Única (SRP)
    
    // ==================== PERFIL DO ADMINISTRADOR ====================
      @GetMapping("/perfil")
    public String perfilAdmin(Model model, Authentication authentication) {
        String login = authentication.getName();
        Administrador admin = administradorService.buscarPorLogin(login);
        
        if (admin == null) {
            return "redirect:/admin/home?erro=AdminNaoEncontrado";
        }
        
        model.addAttribute("admin", admin);
        return "admin/perfil";
    }
    
    @PostMapping("/perfil/editar")
    public String atualizarPerfilAdmin(@ModelAttribute Administrador adminAtualizado, 
                                     Authentication authentication,
                                     RedirectAttributes redirectAttributes) {        try {
            String loginAtual = authentication.getName();
            Administrador adminExistente = administradorService.buscarPorLogin(loginAtual);
            
            if (adminExistente == null) {
                redirectAttributes.addFlashAttribute("erro", "Administrador não encontrado!");
                return "redirect:/admin/perfil";
            }            // Preservar campos que não devem ser alterados
            adminAtualizado.setId(adminExistente.getId());
            if (adminExistente.getAtivo() != null) {
                adminAtualizado.setAtivo(adminExistente.getAtivo());
            }
            
            // Se a senha estiver vazia, manter a senha atual
            if (adminAtualizado.getSenha() == null || adminAtualizado.getSenha().trim().isEmpty()) {
                adminAtualizado.setSenha(adminExistente.getSenha());
            }            
            // Validar se o novo login já existe (se foi alterado)
            if (!adminExistente.getLogin().equals(adminAtualizado.getLogin())) {
                if (administradorService.buscarPorLogin(adminAtualizado.getLogin()) != null) {
                    redirectAttributes.addFlashAttribute("erro", "Este login já está em uso por outro administrador!");
                    return "redirect:/admin/perfil";
                }
            }
            
            // Validar se o novo email já existe (se foi alterado)
            if (adminAtualizado.getEmail() != null && !adminAtualizado.getEmail().isEmpty()) {
                if (adminExistente.getEmail() != null && !adminExistente.getEmail().equals(adminAtualizado.getEmail())) {
                    if (administradorService.existePorEmail(adminAtualizado.getEmail())) {
                        redirectAttributes.addFlashAttribute("erro", "Este email já está em uso por outro administrador!");
                        return "redirect:/admin/perfil";
                    }
                }
            }
            
            // Atualizar o administrador
            administradorService.atualizar(adminAtualizado);
            
            redirectAttributes.addFlashAttribute("sucesso", "Perfil atualizado com sucesso!");
            
            // Se o login foi alterado, precisamos fazer logout e login novamente
            if (!adminExistente.getLogin().equals(adminAtualizado.getLogin())) {
                redirectAttributes.addFlashAttribute("aviso", "Login alterado! Faça login novamente com o novo login.");
                return "redirect:/logout";
            }
            
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erro", "Erro ao atualizar perfil: " + e.getMessage());
        }
        
        return "redirect:/admin/perfil";
    }
}