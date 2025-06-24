package br.eventos.zezinEventos.service.admin;

import br.eventos.zezinEventos.model.Administrador;
import br.eventos.zezinEventos.model.Cliente;
import br.eventos.zezinEventos.model.Evento;
import br.eventos.zezinEventos.model.Organizador;
import br.eventos.zezinEventos.model.dto.EstatisticasGeraisDTO;
import br.eventos.zezinEventos.model.dto.RelatorioCompletoDTO;
import br.eventos.zezinEventos.service.interfaces.admin.RelatorioServiceInterface;
import br.eventos.zezinEventos.service.shared.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Service para geração de relatórios administrativos complexos
 * Implementa princípios SOLID: SRP, OCP, DIP
 * Organizado na estrutura admin/ para melhor navegabilidade
 */
@Service
public class RelatorioService implements RelatorioServiceInterface {

    private final EventoService eventoService;
    private final ClienteService clienteService;
    private final OrganizadorService organizadorService;
    private final AdministradorService administradorService;
    private final InscricaoService inscricaoService;

    @Autowired
    public RelatorioService(EventoService eventoService,
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
    public RelatorioCompletoDTO gerarRelatorioCompleto() {
        // Buscar dados base
        List<Evento> todosEventos = eventoService.listarTodos();
        List<Cliente> todosClientes = clienteService.listarTodos();
        List<Organizador> todosOrganizadores = organizadorService.listarTodos();
        List<Administrador> todosAdministradores = administradorService.listarTodos();

        // Gerar estatísticas
        EstatisticasGeraisDTO estatisticas = gerarEstatisticasGerais(
            todosEventos, todosClientes, todosOrganizadores, todosAdministradores
        );

        // Gerar rankings e listas
        List<Map<String, Object>> top5EventosPopulares = gerarTop5EventosPopulares(todosEventos);
        List<Map<String, Object>> top5OrganizadoresAtivos = gerarTop5OrganizadoresAtivos(todosEventos);
        Map<String, Long> eventosPorTipo = gerarDistribuicaoPorTipo(todosEventos);
        List<Evento> eventosRecentes = gerarEventosRecentes(todosEventos);

        return new RelatorioCompletoDTO(
            estatisticas, top5EventosPopulares, top5OrganizadoresAtivos,
            eventosPorTipo, eventosRecentes
        );
    }

    @Override
    public RelatorioCompletoDTO gerarRelatorioEventos() {
        List<Evento> todosEventos = eventoService.listarTodos();
        
        // Estatísticas focadas em eventos
        EstatisticasGeraisDTO estatisticas = new EstatisticasGeraisDTO();
        preencherEstatisticasEventos(estatisticas, todosEventos);
        
        return new RelatorioCompletoDTO(
            estatisticas,
            gerarTop5EventosPopulares(todosEventos),
            gerarTop5OrganizadoresAtivos(todosEventos),
            gerarDistribuicaoPorTipo(todosEventos),
            gerarEventosRecentes(todosEventos)
        );
    }

    @Override
    public RelatorioCompletoDTO gerarRelatorioUsuarios() {
        List<Cliente> todosClientes = clienteService.listarTodos();
        List<Organizador> todosOrganizadores = organizadorService.listarTodos();
        List<Administrador> todosAdministradores = administradorService.listarTodos();
        
        // Estatísticas focadas em usuários
        EstatisticasGeraisDTO estatisticas = new EstatisticasGeraisDTO();
        preencherEstatisticasUsuarios(estatisticas, todosClientes, todosOrganizadores, todosAdministradores);
        
        return new RelatorioCompletoDTO(estatisticas, null, null, null, null);
    }

    @Override
    public RelatorioCompletoDTO gerarRelatorioFinanceiro() {
        List<Evento> todosEventos = eventoService.listarTodos();
        
        // Estatísticas focadas em financeiro
        EstatisticasGeraisDTO estatisticas = new EstatisticasGeraisDTO();
        preencherEstatisticasFinanceiras(estatisticas, todosEventos);
        
        return new RelatorioCompletoDTO(estatisticas, null, null, null, null);
    }

    // === MÉTODOS PRIVADOS PARA ENCAPSULAR LÓGICA ===

    private EstatisticasGeraisDTO gerarEstatisticasGerais(List<Evento> eventos, List<Cliente> clientes,
                                                         List<Organizador> organizadores, List<Administrador> admins) {
        // Totais gerais
        long totalEventos = eventos.size();
        long totalClientes = clientes.size();
        long totalOrganizadores = organizadores.size();
        long totalAdministradores = admins.size();
        long totalUsuarios = totalClientes + totalOrganizadores + totalAdministradores;

        // Eventos
        long eventosAtivos = eventos.stream().filter(Evento::getAtivo).count();
        long eventosInativos = totalEventos - eventosAtivos;
        long eventosFuturos = eventos.stream()
                .filter(e -> e.getDataEvento().isAfter(LocalDateTime.now()))
                .count();
        long eventosPassados = totalEventos - eventosFuturos;
        long eventosGratuitos = eventos.stream()
                .filter(e -> e.getPrecoIngresso() == 0.0)
                .count();
        long eventosPagos = totalEventos - eventosGratuitos;

        // Usuários
        long clientesAtivos = clientes.stream().filter(Cliente::getAtivo).count();
        long clientesInativos = totalClientes - clientesAtivos;
        long organizadoresAtivos = organizadores.stream().filter(Organizador::getAtivo).count();
        long organizadoresInativos = totalOrganizadores - organizadoresAtivos;
        long adminsAtivos = admins.stream().filter(Administrador::getAtivo).count();
        long adminsInativos = totalAdministradores - adminsAtivos;

        // Vagas e inscrições
        long totalVagas = eventos.stream().mapToLong(Evento::getVagasTotais).sum();
        long totalInscricoes = eventos.stream()
                .mapToLong(evento -> inscricaoService.contarInscricoesPorEvento(evento))
                .sum();
        long vagasDisponiveis = totalVagas - totalInscricoes;
        double taxaOcupacaoGeral = totalVagas > 0 ? (totalInscricoes * 100.0) / totalVagas : 0;

        // Receita
        double receitaTotal = calcularReceitaTotal(eventos);

        return new EstatisticasGeraisDTO(
            totalEventos, totalUsuarios, totalClientes, totalOrganizadores, totalAdministradores,
            eventosAtivos, eventosInativos, eventosFuturos, eventosPassados, eventosGratuitos, eventosPagos,
            clientesAtivos, clientesInativos, organizadoresAtivos, organizadoresInativos,
            adminsAtivos, adminsInativos, totalVagas, totalInscricoes, vagasDisponiveis,
            taxaOcupacaoGeral, receitaTotal
        );
    }

    private void preencherEstatisticasEventos(EstatisticasGeraisDTO estatisticas, List<Evento> eventos) {
        estatisticas.setTotalEventos((long) eventos.size());
        estatisticas.setEventosAtivos(eventos.stream().filter(Evento::getAtivo).count());
        estatisticas.setEventosInativos(estatisticas.getTotalEventos() - estatisticas.getEventosAtivos());
        // ... outros campos específicos de eventos
    }

    private void preencherEstatisticasUsuarios(EstatisticasGeraisDTO estatisticas, List<Cliente> clientes,
                                              List<Organizador> organizadores, List<Administrador> admins) {
        estatisticas.setTotalClientes((long) clientes.size());
        estatisticas.setTotalOrganizadores((long) organizadores.size());
        estatisticas.setTotalAdministradores((long) admins.size());
        estatisticas.setTotalUsuarios(estatisticas.getTotalClientes() + 
                                     estatisticas.getTotalOrganizadores() + 
                                     estatisticas.getTotalAdministradores());
        // ... outros campos específicos de usuários
    }

    private void preencherEstatisticasFinanceiras(EstatisticasGeraisDTO estatisticas, List<Evento> eventos) {
        estatisticas.setReceitaTotal(calcularReceitaTotal(eventos));
        estatisticas.setEventosGratuitos(eventos.stream()
                .filter(e -> e.getPrecoIngresso() == 0.0).count());
        estatisticas.setEventosPagos((long) eventos.size() - estatisticas.getEventosGratuitos());
    }

    private List<Map<String, Object>> gerarTop5EventosPopulares(List<Evento> eventos) {
        return eventos.stream()
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
    }

    private List<Map<String, Object>> gerarTop5OrganizadoresAtivos(List<Evento> eventos) {
        Map<Organizador, Long> eventosPorOrganizador = new HashMap<>();
        for (Evento evento : eventos) {
            Organizador org = evento.getOrganizador();
            eventosPorOrganizador.put(org, eventosPorOrganizador.getOrDefault(org, 0L) + 1);
        }

        return eventosPorOrganizador.entrySet().stream()
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
    }

    private Map<String, Long> gerarDistribuicaoPorTipo(List<Evento> eventos) {
        Map<String, Long> eventosPorTipo = new HashMap<>();
        for (Evento evento : eventos) {
            if (evento.getTipo() != null) {
                String tipo = evento.getTipo().toString();
                eventosPorTipo.put(tipo, eventosPorTipo.getOrDefault(tipo, 0L) + 1);
            }
        }
        return eventosPorTipo;
    }

    private List<Evento> gerarEventosRecentes(List<Evento> eventos) {
        return eventos.stream()
                .filter(e -> e.getDataCriacao() != null)
                .sorted((e1, e2) -> e2.getDataCriacao().compareTo(e1.getDataCriacao()))
                .limit(10)
                .toList();
    }

    private double calcularReceitaTotal(List<Evento> eventos) {
        return eventos.stream()
                .filter(e -> e.getPrecoIngresso() > 0)
                .mapToDouble(evento -> {
                    long inscricoesEvento = inscricaoService.contarInscricoesPorEvento(evento);
                    return inscricoesEvento * evento.getPrecoIngresso();
                })
                .sum();
    }
}
