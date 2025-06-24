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
import org.springframework.security.crypto.password.PasswordEncoder;
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
    
    @Autowired
    private PasswordEncoder passwordEncoder;

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
            .filter(e -> e.getDataCriacao() != null)
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
    }    @PostMapping("/usuarios/editar/CLIENTE/{id}")
    public String salvarEdicaoCliente(@PathVariable Long id,
                                     @ModelAttribute("usuario") Cliente cliente,
                                     @RequestParam(value = "senha", required = false) String novaSenha,
                                     @RequestParam(value = "confirmarSenha", required = false) String confirmarSenha,
                                     RedirectAttributes redirectAttributes) {
        try {
            // Buscar cliente existente para preservar senha
            Cliente clienteExistente = clienteService.buscarPorId(id);
            if (clienteExistente == null) {
                redirectAttributes.addFlashAttribute("error", "Cliente não encontrado.");
                return "redirect:/admin/usuarios";
            }
            
            // Validar confirmação de senha se uma nova senha foi fornecida
            if (novaSenha != null && !novaSenha.trim().isEmpty()) {
                if (confirmarSenha == null || !novaSenha.equals(confirmarSenha)) {
                    redirectAttributes.addFlashAttribute("error", "Senha e confirmação de senha não conferem.");
                    return "redirect:/admin/usuarios/editar/CLIENTE/" + id;
                }
                // Criptografar a nova senha
                cliente.setSenha(passwordEncoder.encode(novaSenha));
            } else {
                // Manter senha existente
                cliente.setSenha(clienteExistente.getSenha());
            }
            
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
                                         @RequestParam(required = false) String senha,
                                         @RequestParam(required = false) String confirmarSenha,
                                         RedirectAttributes redirectAttributes) {
        try {
            // Buscar organizador existente para preservar dados
            Organizador organizadorExistente = organizadorService.buscarPorId(id);
            if (organizadorExistente == null) {
                redirectAttributes.addFlashAttribute("error", "Organizador não encontrado.");
                return "redirect:/admin/usuarios";
            }
            
            // Validar alteração de senha se foi fornecida
            if (senha != null && !senha.trim().isEmpty()) {
                if (confirmarSenha == null || !senha.equals(confirmarSenha)) {
                    redirectAttributes.addFlashAttribute("error", "Senha e confirmação de senha não conferem.");
                    return "redirect:/admin/usuarios/editar/ORGANIZADOR/" + id;
                }
                // Criptografar a nova senha
                organizador.setSenha(passwordEncoder.encode(senha));
            } else {
                // Preservar senha existente se não foi alterada
                organizador.setSenha(organizadorExistente.getSenha());
            }
            
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
                                   @RequestParam(required = false) String senha,
                                   @RequestParam(required = false) String confirmarSenha,
                                   RedirectAttributes redirectAttributes) {
        try {
            // Buscar administrador existente para preservar dados
            Administrador administradorExistente = administradorService.buscarPorId(id);
            if (administradorExistente == null) {
                redirectAttributes.addFlashAttribute("error", "Administrador não encontrado.");
                return "redirect:/admin/usuarios";
            }
            
            // Validar alteração de senha se foi fornecida
            if (senha != null && !senha.trim().isEmpty()) {
                if (confirmarSenha == null || !senha.equals(confirmarSenha)) {
                    redirectAttributes.addFlashAttribute("error", "Senha e confirmação de senha não conferem.");
                    return "redirect:/admin/usuarios/editar/ADMIN/" + id;
                }
                // Criptografar a nova senha
                administrador.setSenha(passwordEncoder.encode(senha));
            } else {
                // Preservar senha existente se não foi alterada
                administrador.setSenha(administradorExistente.getSenha());
            }
            
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
    }    @GetMapping("/eventos")
    public String eventos(Model model, @RequestParam(required = false) String busca) {
        // Buscar todos os eventos ou filtrar por busca
        List<Evento> eventos = busca != null && !busca.trim().isEmpty() 
            ? eventoService.buscarPorNome(busca)
            : eventoService.listarTodos();
        
        // Estatísticas dos eventos
        long totalEventos = eventoService.contarTodos();
        long eventosAtivos = eventos.stream().filter(Evento::getAtivo).count();
        long eventosInativos = totalEventos - eventosAtivos;
        long eventosFuturos = eventos.stream()
            .filter(e -> e.getDataEvento().isAfter(LocalDateTime.now()))
            .count();
        long eventosPassados = totalEventos - eventosFuturos;
        
        // Calcular ocupação total
        int totalVagas = eventos.stream().mapToInt(Evento::getVagasTotais).sum();
        int totalOcupadas = eventos.stream().mapToInt(Evento::getVagasOcupadas).sum();
        double ocupacaoMedia = totalVagas > 0 ? (totalOcupadas * 100.0) / totalVagas : 0;
        
        model.addAttribute("pageTitle", "Gerenciar Eventos");
        model.addAttribute("eventos", eventos);
        model.addAttribute("busca", busca);
        model.addAttribute("totalEventos", totalEventos);
        model.addAttribute("eventosAtivos", eventosAtivos);
        model.addAttribute("eventosInativos", eventosInativos);
        model.addAttribute("eventosFuturos", eventosFuturos);
        model.addAttribute("eventosPassados", eventosPassados);
        model.addAttribute("totalVagas", totalVagas);
        model.addAttribute("totalOcupadas", totalOcupadas);
        model.addAttribute("ocupacaoMedia", ocupacaoMedia);
        
        return "admin/eventos";
    }
    
    @PostMapping("/eventos/ativar/{id}")
    public String ativarEvento(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            Evento evento = eventoService.buscarPorId(id);
            evento.setAtivo(true);
            eventoService.salvar(evento);
            redirectAttributes.addFlashAttribute("success", "Evento ativado com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erro ao ativar evento: " + e.getMessage());
        }
        return "redirect:/admin/eventos";
    }
    
    @PostMapping("/eventos/desativar/{id}")
    public String desativarEvento(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            Evento evento = eventoService.buscarPorId(id);
            evento.setAtivo(false);
            eventoService.salvar(evento);
            redirectAttributes.addFlashAttribute("success", "Evento excluído com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erro ao excluir evento: " + e.getMessage());
        }
        return "redirect:/admin/eventos";
    }
    
    @GetMapping("/eventos/editar/{id}")
    public String editarEvento(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            Evento evento = eventoService.buscarPorId(id);
            model.addAttribute("evento", evento);
            model.addAttribute("pageTitle", "Editar Evento");
            return "admin/editar-evento";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erro ao carregar evento: " + e.getMessage());
            return "redirect:/admin/eventos";
        }
    }    @PostMapping("/eventos/editar/{id}")
    public String salvarEdicaoEvento(@PathVariable Long id,
                                    @ModelAttribute("evento") Evento evento,
                                    RedirectAttributes redirectAttributes) {
        try {
            evento.setId(id);
            eventoService.salvarEdicaoAdmin(evento);
            redirectAttributes.addFlashAttribute("success", "Evento editado com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erro ao editar evento: " + e.getMessage());
        }        return "redirect:/admin/eventos";
    }
    
    @GetMapping("/relatorios")
    public String relatorios(Model model) {
        // Dados gerais do sistema
        List<Evento> todosEventos = eventoService.listarTodos();
        List<Cliente> todosClientes = clienteService.listarTodos();
        List<Organizador> todosOrganizadores = organizadorService.listarTodos();
        List<Administrador> todosAdministradores = administradorService.listarTodos();
        
        // === ESTATÍSTICAS GERAIS ===
        long totalEventos = todosEventos.size();
        long totalClientes = todosClientes.size();
        long totalOrganizadores = todosOrganizadores.size();
        long totalAdministradores = todosAdministradores.size();
        long totalUsuarios = totalClientes + totalOrganizadores + totalAdministradores;
        
        // === ESTATÍSTICAS DE EVENTOS ===
        long eventosAtivos = todosEventos.stream().filter(Evento::getAtivo).count();
        long eventosInativos = totalEventos - eventosAtivos;
        long eventosFuturos = todosEventos.stream()
            .filter(e -> e.getDataEvento().isAfter(LocalDateTime.now()))
            .count();
        long eventosPassados = totalEventos - eventosFuturos;        long eventosGratuitos = todosEventos.stream()
            .filter(e -> e.getPrecoIngresso() == 0.0)
            .count();
        long eventosPagos = totalEventos - eventosGratuitos;
        
        // === ESTATÍSTICAS DE USUÁRIOS ===
        long clientesAtivos = todosClientes.stream().filter(Cliente::getAtivo).count();
        long clientesInativos = totalClientes - clientesAtivos;
        long organizadoresAtivos = todosOrganizadores.stream().filter(Organizador::getAtivo).count();
        long organizadoresInativos = totalOrganizadores - organizadoresAtivos;
        long adminsAtivos = todosAdministradores.stream().filter(Administrador::getAtivo).count();
        long adminsInativos = totalAdministradores - adminsAtivos;
        
        // === ESTATÍSTICAS DE VAGAS E INSCRIÇÕES ===
        long totalVagas = todosEventos.stream().mapToLong(Evento::getVagasTotais).sum();
        long totalInscricoes = todosEventos.stream()
            .mapToLong(evento -> inscricaoService.contarInscricoesPorEvento(evento))
            .sum();
        long vagasDisponiveis = totalVagas - totalInscricoes;
        double taxaOcupacaoGeral = totalVagas > 0 ? (totalInscricoes * 100.0) / totalVagas : 0;        // === RECEITA TOTAL ===
        double receitaTotal = todosEventos.stream()
            .filter(e -> e.getPrecoIngresso() > 0)
            .mapToDouble(evento -> {
                long inscricoesEvento = inscricaoService.contarInscricoesPorEvento(evento);
                return inscricoesEvento * evento.getPrecoIngresso();
            })
            .sum();
        
        // === TOP 5 EVENTOS MAIS POPULARES ===
        List<Map<String, Object>> top5EventosPopulares = todosEventos.stream()
            .map(evento -> {
                Map<String, Object> eventoInfo = new HashMap<>();
                long inscricoes = inscricaoService.contarInscricoesPorEvento(evento);
                eventoInfo.put("nome", evento.getNome());
                eventoInfo.put("inscricoes", inscricoes);
                eventoInfo.put("organizador", evento.getOrganizador().getNome());
                eventoInfo.put("dataEvento", evento.getDataEvento());
                eventoInfo.put("ocupacao", evento.getVagasTotais() > 0 ? 
                    (inscricoes * 100.0) / evento.getVagasTotais() : 0);
                return eventoInfo;
            })
            .sorted((e1, e2) -> Long.compare((Long) e2.get("inscricoes"), (Long) e1.get("inscricoes")))
            .limit(5)
            .toList();
        
        // === TOP 5 ORGANIZADORES MAIS ATIVOS ===
        Map<Organizador, Long> eventosPorOrganizador = new HashMap<>();
        for (Evento evento : todosEventos) {
            Organizador org = evento.getOrganizador();
            eventosPorOrganizador.put(org, eventosPorOrganizador.getOrDefault(org, 0L) + 1);
        }
        
        List<Map<String, Object>> top5OrganizadoresAtivos = eventosPorOrganizador.entrySet().stream()
            .map(entry -> {
                Map<String, Object> orgInfo = new HashMap<>();
                orgInfo.put("nome", entry.getKey().getNome());
                orgInfo.put("empresa", entry.getKey().getEmpresa());
                orgInfo.put("totalEventos", entry.getValue());
                return orgInfo;
            })
            .sorted((o1, o2) -> Long.compare((Long) o2.get("totalEventos"), (Long) o1.get("totalEventos")))
            .limit(5)
            .toList();
          // === DISTRIBUIÇÃO POR TIPO DE EVENTO ===
        Map<String, Long> eventosPorTipo = new HashMap<>();
        for (Evento evento : todosEventos) {
            if (evento.getTipo() != null) {
                String tipo = evento.getTipo().toString();
                eventosPorTipo.put(tipo, eventosPorTipo.getOrDefault(tipo, 0L) + 1);
            }
        }
          // === EVENTOS RECENTES (ÚLTIMOS 10) ===
        List<Evento> eventosRecentes = todosEventos.stream()
            .filter(e -> e.getDataCriacao() != null)
            .sorted((e1, e2) -> e2.getDataCriacao().compareTo(e1.getDataCriacao()))
            .limit(10)
            .toList();
        
        // Adicionar todos os dados ao modelo
        model.addAttribute("pageTitle", "Relatórios do Sistema");
        
        // Estatísticas gerais
        model.addAttribute("totalEventos", totalEventos);
        model.addAttribute("totalUsuarios", totalUsuarios);
        model.addAttribute("totalClientes", totalClientes);
        model.addAttribute("totalOrganizadores", totalOrganizadores);
        model.addAttribute("totalAdministradores", totalAdministradores);
        
        // Estatísticas de eventos
        model.addAttribute("eventosAtivos", eventosAtivos);
        model.addAttribute("eventosInativos", eventosInativos);
        model.addAttribute("eventosFuturos", eventosFuturos);
        model.addAttribute("eventosPassados", eventosPassados);
        model.addAttribute("eventosGratuitos", eventosGratuitos);
        model.addAttribute("eventosPagos", eventosPagos);
        
        // Estatísticas de usuários
        model.addAttribute("clientesAtivos", clientesAtivos);
        model.addAttribute("clientesInativos", clientesInativos);
        model.addAttribute("organizadoresAtivos", organizadoresAtivos);
        model.addAttribute("organizadoresInativos", organizadoresInativos);
        model.addAttribute("adminsAtivos", adminsAtivos);
        model.addAttribute("adminsInativos", adminsInativos);
        
        // Estatísticas de vagas
        model.addAttribute("totalVagas", totalVagas);
        model.addAttribute("totalInscricoes", totalInscricoes);
        model.addAttribute("vagasDisponiveis", vagasDisponiveis);
        model.addAttribute("taxaOcupacaoGeral", taxaOcupacaoGeral);
          // Financeiro
        model.addAttribute("receitaTotal", receitaTotal);
        
        // Listas e rankings
        model.addAttribute("top5EventosPopulares", top5EventosPopulares);
        model.addAttribute("top5OrganizadoresAtivos", top5OrganizadoresAtivos);
        model.addAttribute("eventosPorTipo", eventosPorTipo);
        model.addAttribute("eventosRecentes", eventosRecentes);
        
        return "admin/relatorios";
    }
    
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