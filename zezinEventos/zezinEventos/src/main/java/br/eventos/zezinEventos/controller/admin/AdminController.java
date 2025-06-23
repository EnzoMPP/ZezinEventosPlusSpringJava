package br.eventos.zezinEventos.controller.admin;

import br.eventos.zezinEventos.model.Administrador;
import br.eventos.zezinEventos.model.Cliente;
import br.eventos.zezinEventos.model.Evento;
import br.eventos.zezinEventos.model.Organizador;
import br.eventos.zezinEventos.service.AdministradorService;
import br.eventos.zezinEventos.service.ClienteService;
import br.eventos.zezinEventos.service.EventoService;
import br.eventos.zezinEventos.service.InscricaoService;
import br.eventos.zezinEventos.service.OrganizadorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.ArrayList;
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

    @GetMapping("/home")
    public String adminHome(Model model) {
        // Buscar todas as estatísticas globais do sistema
        List<Evento> todosEventos = eventoService.listarTodos();
        
        // Calcular estatísticas
        long totalEventos = todosEventos.size();
        long eventosAtivos = todosEventos.stream()
            .filter(e -> e.getDataEvento().isAfter(LocalDateTime.now()))
            .count();
        long eventosFinalizados = totalEventos - eventosAtivos;
        
        long totalClientes = clienteService.contarTodos();
        long totalOrganizadores = organizadorService.contarTodos();
        long totalUsuarios = totalClientes + totalOrganizadores;
        
        long totalInscricoes = todosEventos.stream()
            .mapToLong(evento -> inscricaoService.contarInscricoesPorEvento(evento))
            .sum();
            
        long totalVagas = todosEventos.stream()
            .mapToLong(Evento::getVagasTotais)
            .sum();
            
        double taxaOcupacaoGlobal = totalVagas > 0 ? (totalInscricoes * 100.0) / totalVagas : 0;
        
        // Evento mais popular do sistema
        Evento eventoMaisPopular = todosEventos.stream()
            .max((e1, e2) -> Long.compare(
                inscricaoService.contarInscricoesPorEvento(e1),
                inscricaoService.contarInscricoesPorEvento(e2)
            ))
            .orElse(null);
        
        // Eventos recentes (últimos 5)
        List<Evento> eventosRecentes = todosEventos.stream()
            .sorted((e1, e2) -> e2.getDataCriacao().compareTo(e1.getDataCriacao()))
            .limit(5)
            .toList();
        
        model.addAttribute("pageTitle", "Dashboard Administrativo");
        model.addAttribute("totalEventos", totalEventos);
        model.addAttribute("eventosAtivos", eventosAtivos);
        model.addAttribute("eventosFinalizados", eventosFinalizados);
        model.addAttribute("totalClientes", totalClientes);
        model.addAttribute("totalOrganizadores", totalOrganizadores);
        model.addAttribute("totalUsuarios", totalUsuarios);
        model.addAttribute("totalInscricoes", totalInscricoes);
        model.addAttribute("totalVagas", totalVagas);
        model.addAttribute("taxaOcupacaoGlobal", taxaOcupacaoGlobal);
        model.addAttribute("eventoMaisPopular", eventoMaisPopular);
        model.addAttribute("eventosRecentes", eventosRecentes);
        
        return "admin/home";
    }    @GetMapping("/usuarios")
    public String usuarios(Model model, @RequestParam(required = false) String busca) {
        // Buscar todos os tipos de usuários
        List<Cliente> clientes = busca != null && !busca.trim().isEmpty() 
            ? clienteService.buscarPorNome(busca) 
            : clienteService.listarTodos();
            
        List<Organizador> organizadores = busca != null && !busca.trim().isEmpty()
            ? organizadorService.buscarPorNome(busca)
            : organizadorService.listarTodos();
            
        List<Administrador> administradores = busca != null && !busca.trim().isEmpty()
            ? administradorService.buscarPorNome(busca)
            : administradorService.listarTodos();
        
        // Criar lista combinada para exibição
        List<Map<String, Object>> todosUsuarios = new ArrayList<>();
          // Adicionar clientes (apenas ativos)
        for (Cliente cliente : clientes) {
            if (cliente.getAtivo()) { // Só mostra clientes ativos
                Map<String, Object> usuario = new HashMap<>();
                usuario.put("id", cliente.getId());
                usuario.put("nome", cliente.getNome());
                usuario.put("email", cliente.getEmail());
                usuario.put("login", cliente.getLogin());
                usuario.put("ativo", cliente.getAtivo());
                usuario.put("tipo", "CLIENTE");
                usuario.put("tipoTexto", "Cliente");
                usuario.put("tipoClasse", "bg-success");
                usuario.put("documento", cliente.getCpf());
                usuario.put("telefone", cliente.getTelefone());
                todosUsuarios.add(usuario);
            }
        }
        
        // Adicionar organizadores (apenas ativos)
        for (Organizador organizador : organizadores) {
            if (organizador.getAtivo()) { // Só mostra organizadores ativos
                Map<String, Object> usuario = new HashMap<>();
                usuario.put("id", organizador.getId());
                usuario.put("nome", organizador.getNome());
                usuario.put("email", organizador.getEmail());
                usuario.put("login", organizador.getLogin());
                usuario.put("ativo", organizador.getAtivo());
                usuario.put("tipo", "ORGANIZADOR");
                usuario.put("tipoTexto", "Organizador");
                usuario.put("tipoClasse", "bg-warning");
                usuario.put("documento", organizador.getCnpj());
                usuario.put("telefone", organizador.getTelefone());
                usuario.put("empresa", organizador.getEmpresa());
                todosUsuarios.add(usuario);
            }
        }
        
        // Adicionar administradores (apenas ativos)
        for (Administrador admin : administradores) {
            if (admin.getAtivo()) { // Só mostra administradores ativos
                Map<String, Object> usuario = new HashMap<>();
                usuario.put("id", admin.getId());
                usuario.put("nome", admin.getNome());
                usuario.put("email", admin.getEmail());
                usuario.put("login", admin.getLogin());            usuario.put("ativo", admin.getAtivo());
                usuario.put("tipo", "ADMIN");
                usuario.put("tipoTexto", "Administrador");
                usuario.put("tipoClasse", "bg-danger");
                usuario.put("documento", null); // Administradores não têm documento
                usuario.put("telefone", admin.getTelefone());
                usuario.put("cargo", admin.getCargo());
                todosUsuarios.add(usuario);
            }
        }
        
        model.addAttribute("pageTitle", "Gerenciar Usuários");
        model.addAttribute("usuarios", todosUsuarios);
        model.addAttribute("busca", busca);
        model.addAttribute("totalUsuarios", todosUsuarios.size());
        model.addAttribute("totalClientes", clientes.size());
        model.addAttribute("totalOrganizadores", organizadores.size());
        model.addAttribute("totalAdministradores", administradores.size());
        
        return "admin/usuarios";
    }    
    @PostMapping("/usuarios/desativar/{tipo}/{id}")
    public String desativarUsuario(@PathVariable String tipo, 
                                  @PathVariable Long id,
                                  Authentication authentication,
                                  RedirectAttributes redirectAttributes) {
        try {
            // Verificar se não é o próprio admin tentando se desativar
            String loginAtual = authentication.getName();
            
            switch (tipo.toUpperCase()) {
                case "CLIENTE":
                    Cliente cliente = clienteService.buscarPorId(id);
                    cliente.setAtivo(false);
                    clienteService.salvar(cliente);
                    break;
                case "ORGANIZADOR":
                    Organizador organizador = organizadorService.buscarPorId(id);
                    organizador.setAtivo(false);
                    organizadorService.salvar(organizador);
                    break;
                case "ADMIN":
                    Administrador admin = administradorService.buscarPorId(id);
                    if (admin.getLogin().equals(loginAtual)) {
                        redirectAttributes.addFlashAttribute("error", "Você não pode excluir sua própria conta!");
                        return "redirect:/admin/usuarios";
                    }
                    admin.setAtivo(false);
                    administradorService.salvar(admin);
                    break;
            }
            redirectAttributes.addFlashAttribute("success", "Usuário excluído com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erro ao excluir usuário: " + e.getMessage());
        }
          return "redirect:/admin/usuarios";
    }
    
    @GetMapping("/usuarios/editar/{tipo}/{id}")
    public String editarUsuario(@PathVariable String tipo, 
                               @PathVariable Long id,
                               Model model,
                               RedirectAttributes redirectAttributes) {
        try {
            switch (tipo.toUpperCase()) {
                case "CLIENTE":
                    Cliente cliente = clienteService.buscarPorId(id);
                    model.addAttribute("usuario", cliente);
                    model.addAttribute("tipoUsuario", "CLIENTE");
                    break;
                case "ORGANIZADOR":
                    Organizador organizador = organizadorService.buscarPorId(id);
                    model.addAttribute("usuario", organizador);
                    model.addAttribute("tipoUsuario", "ORGANIZADOR");
                    break;
                case "ADMIN":
                    Administrador admin = administradorService.buscarPorId(id);
                    model.addAttribute("usuario", admin);
                    model.addAttribute("tipoUsuario", "ADMIN");
                    break;
                default:
                    redirectAttributes.addFlashAttribute("error", "Tipo de usuário inválido!");
                    return "redirect:/admin/usuarios";
            }
            
            model.addAttribute("pageTitle", "Editar " + tipo.toLowerCase());
            return "admin/editar-usuario";
            
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erro ao carregar usuário: " + e.getMessage());
            return "redirect:/admin/usuarios";
        }
    }
      @PostMapping("/usuarios/editar/CLIENTE/{id}")
    public String salvarEdicaoCliente(@PathVariable Long id,
                                     @ModelAttribute("usuario") Cliente cliente,
                                     RedirectAttributes redirectAttributes) {
        try {
            cliente.setId(id);
            clienteService.salvar(cliente);
            redirectAttributes.addFlashAttribute("success", "Cliente editado com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erro ao salvar cliente: " + e.getMessage());
        }
        
        return "redirect:/admin/usuarios";
    }
    
    @PostMapping("/usuarios/editar/ORGANIZADOR/{id}")
    public String salvarEdicaoOrganizador(@PathVariable Long id,
                                         @ModelAttribute("usuario") Organizador organizador,
                                         RedirectAttributes redirectAttributes) {
        try {
            organizador.setId(id);
            organizadorService.salvar(organizador);
            redirectAttributes.addFlashAttribute("success", "Organizador editado com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erro ao salvar organizador: " + e.getMessage());
        }
        
        return "redirect:/admin/usuarios";
    }
    
    @PostMapping("/usuarios/editar/ADMIN/{id}")
    public String salvarEdicaoAdmin(@PathVariable Long id,
                                   @ModelAttribute("usuario") Administrador administrador,
                                   RedirectAttributes redirectAttributes) {
        try {
            administrador.setId(id);
            administradorService.salvar(administrador);
            redirectAttributes.addFlashAttribute("success", "Administrador editado com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erro ao salvar administrador: " + e.getMessage());
        }
        
        return "redirect:/admin/usuarios";
    }
    
    @GetMapping("/usuarios/novo-admin")
    public String novoAdmin(Model model) {
        model.addAttribute("administrador", new Administrador());
        model.addAttribute("pageTitle", "Cadastrar Novo Administrador");
        return "admin/novo-admin";
    }
    
    @PostMapping("/usuarios/novo-admin")
    public String salvarNovoAdmin(@ModelAttribute("administrador") Administrador administrador,
                                 RedirectAttributes redirectAttributes) {
        try {
            // Validações básicas
            if (administrador.getNome() == null || administrador.getNome().trim().isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "Nome é obrigatório!");
                return "redirect:/admin/usuarios/novo-admin";
            }
            
            if (administrador.getEmail() == null || administrador.getEmail().trim().isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "Email é obrigatório!");
                return "redirect:/admin/usuarios/novo-admin";
            }
            
            if (administrador.getLogin() == null || administrador.getLogin().trim().isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "Login é obrigatório!");
                return "redirect:/admin/usuarios/novo-admin";
            }
            
            if (administrador.getSenha() == null || administrador.getSenha().trim().isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "Senha é obrigatória!");
                return "redirect:/admin/usuarios/novo-admin";
            }
            
            // Verificar se login já existe
            if (administradorService.existePorLogin(administrador.getLogin())) {
                redirectAttributes.addFlashAttribute("error", "Login já existe! Escolha outro.");
                return "redirect:/admin/usuarios/novo-admin";
            }
            
            // Verificar se email já existe
            if (administradorService.existePorEmail(administrador.getEmail())) {
                redirectAttributes.addFlashAttribute("error", "Email já cadastrado! Use outro email.");
                return "redirect:/admin/usuarios/novo-admin";
            }
              // Definir valores padrão
            administrador.setAtivo(true);
            
            // Salvar
            administradorService.salvar(administrador);
            redirectAttributes.addFlashAttribute("success", "Administrador cadastrado com sucesso!");
            
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erro ao cadastrar administrador: " + e.getMessage());
            return "redirect:/admin/usuarios/novo-admin";
        }
        
        return "redirect:/admin/usuarios";
    }

    @GetMapping("/eventos")
    public String eventos(Model model) {
        model.addAttribute("pageTitle", "Gerenciar Eventos");
        return "admin/eventos";
    }
}