package br.eventos.zezinEventos.service.admin;

import br.eventos.zezinEventos.model.dto.admin.DashboardDTO;
import br.eventos.zezinEventos.model.Evento;
import br.eventos.zezinEventos.service.interfaces.admin.DashboardServiceInterface;
import br.eventos.zezinEventos.service.shared.EventoService;
import br.eventos.zezinEventos.service.shared.ClienteService;
import br.eventos.zezinEventos.service.shared.OrganizadorService;
import br.eventos.zezinEventos.service.shared.AdministradorService;
import br.eventos.zezinEventos.service.shared.InscricaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementação do serviço de Dashboard Administrativo
 * 
 * Responsável por:
 * - Compilar estatísticas do sistema
 * - Gerar alertas importantes
 * - Verificar saúde do sistema
 * 
 * Seguindo o princípio da Responsabilidade Única (SRP)
 * Organizado na estrutura admin/ para melhor navegabilidade
 */
@Service
public class DashboardService implements DashboardServiceInterface {
    
    private final EventoService eventoService;
    private final ClienteService clienteService;
    private final OrganizadorService organizadorService;
    private final AdministradorService administradorService;
    private final InscricaoService inscricaoService;
      /**
     * Construtor com injeção de dependência
     * Seguindo o princípio da Inversão de Dependência (DIP)
     */
    @Autowired
    public DashboardService(EventoService eventoService,
                           ClienteService clienteService,
                           OrganizadorService organizadorService,
                           AdministradorService administradorService,
                           InscricaoService inscricaoService) {
        this.eventoService = eventoService;
        this.clienteService = clienteService;
        this.organizadorService = organizadorService;
        this.administradorService = administradorService;
        this.inscricaoService = inscricaoService;
    }
    
    @Override
    public DashboardDTO obterEstatisticasDashboard() {
        List<Evento> todosEventos = eventoService.listarTodos();
        
        // Calcular estatísticas básicas
        long totalEventos = todosEventos.size();
        long eventosAtivos = calcularEventosAtivos(todosEventos);
        long eventosFinalizados = totalEventos - eventosAtivos;
        
        long totalClientes = clienteService.contarTodos();
        long totalOrganizadores = organizadorService.contarTodos();
        long totalAdministradores = administradorService.contarTodos();
        long totalUsuarios = totalClientes + totalOrganizadores + totalAdministradores;
        
        long totalInscricoes = calcularTotalInscricoes(todosEventos);
        long totalVagas = calcularTotalVagas(todosEventos);
        double taxaOcupacaoGlobal = calcularTaxaOcupacao(totalInscricoes, totalVagas);
        
        Evento eventoMaisPopular = encontrarEventoMaisPopular(todosEventos);
        List<Evento> eventosRecentes = obterEventosRecentes(todosEventos);
        List<String> alertas = gerarAlertas(todosEventos);
        
        return DashboardDTO.builder()
                .totalEventos(totalEventos)
                .totalUsuarios(totalUsuarios)
                .totalClientes(totalClientes)
                .totalOrganizadores(totalOrganizadores)
                .totalAdministradores(totalAdministradores)
                .eventosAtivos(eventosAtivos)
                .eventosFinalizados(eventosFinalizados)
                .totalInscricoes(totalInscricoes)
                .totalVagas(totalVagas)
                .taxaOcupacaoGlobal(taxaOcupacaoGlobal)
                .eventoMaisPopular(eventoMaisPopular)
                .eventosRecentes(eventosRecentes)
                .alertas(alertas)
                .build();
    }
    
    @Override
    public boolean verificarSaudeSistema() {
        try {
            // Verificar se os serviços estão respondendo
            clienteService.contarTodos();
            organizadorService.contarTodos();
            administradorService.contarTodos();
            eventoService.contarTodos();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    @Override
    public List<String> obterAlertas() {
        List<Evento> todosEventos = eventoService.listarTodos();
        return gerarAlertas(todosEventos);
    }
    
    // === MÉTODOS PRIVADOS PARA ENCAPSULAR LÓGICA ===
    
    private long calcularEventosAtivos(List<Evento> eventos) {
        return eventos.stream()
                .filter(e -> e.getDataEvento().isAfter(LocalDateTime.now()))
                .count();
    }
    
    private long calcularTotalInscricoes(List<Evento> eventos) {
        return eventos.stream()
                .mapToLong(evento -> inscricaoService.contarInscricoesPorEvento(evento))
                .sum();
    }
    
    private long calcularTotalVagas(List<Evento> eventos) {
        return eventos.stream()
                .mapToLong(Evento::getVagasTotais)
                .sum();
    }
    
    private double calcularTaxaOcupacao(long inscricoes, long vagas) {
        return vagas > 0 ? (inscricoes * 100.0) / vagas : 0;
    }
    
    private Evento encontrarEventoMaisPopular(List<Evento> eventos) {
        return eventos.stream()
                .max((e1, e2) -> Long.compare(
                        inscricaoService.contarInscricoesPorEvento(e1),
                        inscricaoService.contarInscricoesPorEvento(e2)
                ))
                .orElse(null);
    }
    
    private List<Evento> obterEventosRecentes(List<Evento> eventos) {
        return eventos.stream()
                .filter(e -> e.getDataCriacao() != null)
                .sorted((e1, e2) -> e2.getDataCriacao().compareTo(e1.getDataCriacao()))
                .limit(5)
                .toList();
    }
    
    private List<String> gerarAlertas(List<Evento> eventos) {
        List<String> alertas = new ArrayList<>();
        
        // Verificar eventos sem inscrições
        long eventosSemInscricoes = eventos.stream()
                .filter(e -> inscricaoService.contarInscricoesPorEvento(e) == 0)
                .count();
        
        if (eventosSemInscricoes > 0) {
            alertas.add(eventosSemInscricoes + " evento(s) sem inscrições");
        }
        
        // Verificar eventos próximos com baixa ocupação
        long eventosComBaixaOcupacao = eventos.stream()
                .filter(e -> e.getDataEvento().isAfter(LocalDateTime.now()) && 
                           e.getDataEvento().isBefore(LocalDateTime.now().plusDays(7)))
                .filter(e -> {
                    long inscricoes = inscricaoService.contarInscricoesPorEvento(e);
                    double ocupacao = e.getVagasTotais() > 0 ? (inscricoes * 100.0) / e.getVagasTotais() : 0;
                    return ocupacao < 50;
                })
                .count();
        
        if (eventosComBaixaOcupacao > 0) {
            alertas.add(eventosComBaixaOcupacao + " evento(s) próximo(s) com baixa ocupação");
        }
        
        return alertas;
    }
}
