package br.eventos.zezinEventos.controller.organizador;

import br.eventos.zezinEventos.model.Evento;
import br.eventos.zezinEventos.model.Organizador;
import br.eventos.zezinEventos.model.enums.TipoEvento;
import br.eventos.zezinEventos.service.shared.EventoService;
import br.eventos.zezinEventos.service.shared.InscricaoService;
import br.eventos.zezinEventos.service.shared.OrganizadorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/organizador")
@PreAuthorize("hasRole('ORGANIZADOR')")
public class OrganizadorController {    @Autowired
    private OrganizadorService organizadorService;
    
    @Autowired
    private EventoService eventoService;
    
    @Autowired
    private InscricaoService inscricaoService;    @GetMapping("/home")
    public String organizadorHome(Model model, Authentication authentication) {
        // Busca dados do organizador logado
        Organizador organizador = organizadorService.buscarPorLogin(authentication.getName());
        List<Evento> eventosDoOrganizador = eventoService.listarPorOrganizador(organizador);
        
        // Calcular estatísticas reais
        long totalEventos = eventosDoOrganizador.size();
        long eventosAtivos = eventosDoOrganizador.stream()
            .filter(e -> e.getDataEvento().isAfter(java.time.LocalDateTime.now()))
            .count();
        long eventosFinalizados = totalEventos - eventosAtivos;
        
        long totalInscricoes = eventosDoOrganizador.stream()
            .mapToLong(evento -> inscricaoService.contarInscricoesPorEvento(evento))
            .sum();
        
        model.addAttribute("pageTitle", "Dashboard do Organizador");
        model.addAttribute("organizador", organizador);
        model.addAttribute("totalEventos", totalEventos); 
        model.addAttribute("eventosAtivos", eventosAtivos);
        model.addAttribute("eventosFinalizados", eventosFinalizados);
        model.addAttribute("totalInscricoes", totalInscricoes);
        
        return "organizador/home";
    }
      @GetMapping("/eventos")
    public String meusEventos(Model model, Authentication authentication) {
        Organizador organizador = organizadorService.buscarPorLogin(authentication.getName());
        List<Evento> eventos = eventoService.listarPorOrganizador(organizador);
        
        model.addAttribute("pageTitle", "Meus Eventos");
        model.addAttribute("organizador", organizador);
        model.addAttribute("eventos", eventos);
        
        return "organizador/eventos";
    }
    
    @GetMapping("/criar-evento")
    public String criarEvento(Model model, Authentication authentication) {
        Organizador organizador = organizadorService.buscarPorLogin(authentication.getName());
        
        model.addAttribute("pageTitle", "Criar Evento");
        model.addAttribute("organizador", organizador);
        model.addAttribute("evento", new Evento());
        model.addAttribute("tiposEvento", TipoEvento.values());
        
        return "organizador/criar-evento";
    }    @PostMapping("/eventos/salvar")
    public String salvarEvento(@ModelAttribute Evento evento, 
                              Authentication authentication,
                              RedirectAttributes redirectAttributes,
                              Model model) {
        try {
            Organizador organizador = organizadorService.buscarPorLogin(authentication.getName());
            evento.setOrganizador(organizador);
            
            eventoService.salvar(evento);
            
            if (evento.getId() != null) {
                redirectAttributes.addFlashAttribute("success", "Evento atualizado com sucesso!");
            } else {
                redirectAttributes.addFlashAttribute("success", "Evento criado com sucesso!");
            }
            
        } catch (Exception e) {
            // Em caso de erro, prepara os dados para o formulário
            Organizador organizador = organizadorService.buscarPorLogin(authentication.getName());
            model.addAttribute("organizador", organizador);
            model.addAttribute("evento", evento);
            model.addAttribute("tiposEvento", TipoEvento.values());
            model.addAttribute("error", e.getMessage());
            
            // Retorna o template correto baseado se é criação ou edição
            if (evento.getId() != null) {
                model.addAttribute("pageTitle", "Editar Evento");
                return "organizador/editar-evento";
            } else {
                model.addAttribute("pageTitle", "Criar Evento");
                return "organizador/criar-evento";
            }
        }
        
        return "redirect:/organizador/eventos";
    }
    
    @GetMapping("/eventos/editar/{id}")
    public String editarEvento(@PathVariable Long id, Model model, Authentication authentication) {
        try {
            Evento evento = eventoService.buscarPorId(id);
            Organizador organizador = organizadorService.buscarPorLogin(authentication.getName());
            
            // Verifica se o evento pertence ao organizador
            if (!evento.getOrganizador().getId().equals(organizador.getId())) {
                throw new RuntimeException("Você não tem permissão para editar este evento");
            }
            
            model.addAttribute("pageTitle", "Editar Evento");
            model.addAttribute("organizador", organizador);
            model.addAttribute("evento", evento);
            model.addAttribute("tiposEvento", TipoEvento.values());
            
            return "organizador/editar-evento";
            
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "redirect:/organizador/eventos";
        }
    }
    
    @PostMapping("/eventos/excluir/{id}")
    public String excluirEvento(@PathVariable Long id, 
                               Authentication authentication,
                               RedirectAttributes redirectAttributes) {
        try {
            Evento evento = eventoService.buscarPorId(id);
            Organizador organizador = organizadorService.buscarPorLogin(authentication.getName());
            
            // Verifica se o evento pertence ao organizador
            if (!evento.getOrganizador().getId().equals(organizador.getId())) {
                throw new RuntimeException("Você não tem permissão para excluir este evento");
            }
            
            eventoService.excluir(id);
            redirectAttributes.addFlashAttribute("success", "Evento excluído com sucesso!");
            
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        
        return "redirect:/organizador/eventos";
    }
      @GetMapping("/relatorios")
    public String relatorios(Model model, Authentication authentication) {
        Organizador organizador = organizadorService.buscarPorLogin(authentication.getName());
        List<Evento> eventosDoOrganizador = eventoService.listarPorOrganizador(organizador);
        
        // Calcular estatísticas
        long totalEventos = eventosDoOrganizador.size();
        long eventosAtivos = eventosDoOrganizador.stream()
            .filter(e -> e.getDataEvento().isAfter(java.time.LocalDateTime.now()))
            .count();
        long eventosFinalizados = totalEventos - eventosAtivos;
        
        long totalInscricoes = eventosDoOrganizador.stream()
            .mapToLong(evento -> inscricaoService.contarInscricoesPorEvento(evento))
            .sum();
            
        long totalVagas = eventosDoOrganizador.stream()
            .mapToLong(Evento::getVagasTotais)
            .sum();
            
        double taxaOcupacao = totalVagas > 0 ? (totalInscricoes * 100.0) / totalVagas : 0;
        
        // Evento com mais inscrições
        Evento eventoMaisPopular = eventosDoOrganizador.stream()
            .max((e1, e2) -> Long.compare(
                inscricaoService.contarInscricoesPorEvento(e1),
                inscricaoService.contarInscricoesPorEvento(e2)
            ))
            .orElse(null);
            
        model.addAttribute("pageTitle", "Relatórios");
        model.addAttribute("organizador", organizador);
        model.addAttribute("eventos", eventosDoOrganizador);
        model.addAttribute("totalEventos", totalEventos);
        model.addAttribute("eventosAtivos", eventosAtivos);
        model.addAttribute("eventosFinalizados", eventosFinalizados);
        model.addAttribute("totalInscricoes", totalInscricoes);
        model.addAttribute("totalVagas", totalVagas);
        model.addAttribute("taxaOcupacao", taxaOcupacao);
        model.addAttribute("eventoMaisPopular", eventoMaisPopular);
        
        return "organizador/relatorios";
    }
    
    @GetMapping("/empresa")
    public String empresa(Model model, Authentication authentication) {
        Organizador organizador = organizadorService.buscarPorLogin(authentication.getName());
        
        model.addAttribute("pageTitle", "Informações da Empresa");
        model.addAttribute("organizador", organizador);
        
        return "organizador/empresa";
    }
      @PostMapping("/empresa/salvar")
    public String salvarEmpresa(@ModelAttribute Organizador organizador,
                               Authentication authentication,
                               RedirectAttributes redirectAttributes,
                               Model model) {
        try {
            // Busca o organizador atual para manter dados importantes
            Organizador organizadorAtual = organizadorService.buscarPorLogin(authentication.getName());
            
            // Validações de duplicidade (apenas se os dados foram alterados)
            if (!organizadorAtual.getEmail().equals(organizador.getEmail()) && 
                organizadorService.existeEmail(organizador.getEmail())) {
                model.addAttribute("pageTitle", "Informações da Empresa");
                model.addAttribute("organizador", organizador);
                model.addAttribute("error", "Este email já está sendo usado por outro organizador.");
                return "organizador/empresa";
            }
            
            if (!organizadorAtual.getCnpj().equals(organizador.getCnpj()) && 
                organizadorService.existeCnpj(organizador.getCnpj())) {
                model.addAttribute("pageTitle", "Informações da Empresa");
                model.addAttribute("organizador", organizador);
                model.addAttribute("error", "Este CNPJ já está sendo usado por outro organizador.");
                return "organizador/empresa";
            }
              // Mantém dados que não devem ser alterados
            organizador.setId(organizadorAtual.getId());
            organizador.setLogin(organizadorAtual.getLogin());
            organizador.setSenha(organizadorAtual.getSenha());
            organizador.setAtivo(organizadorAtual.getAtivo());
            
            organizadorService.salvar(organizador);
            redirectAttributes.addFlashAttribute("success", "Informações da empresa atualizadas com sucesso!");
            
        } catch (Exception e) {
            model.addAttribute("pageTitle", "Informações da Empresa");
            model.addAttribute("organizador", organizador);
            model.addAttribute("error", "Erro ao atualizar informações: " + e.getMessage());
            return "organizador/empresa";
        }
        
        return "redirect:/organizador/empresa";
    }
}