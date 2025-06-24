package br.eventos.zezinEventos.service.organizador;

import br.eventos.zezinEventos.model.Evento;
import br.eventos.zezinEventos.model.Organizador;
import br.eventos.zezinEventos.model.dto.organizador.OrganizadorRelatorioDTO;
import br.eventos.zezinEventos.service.interfaces.organizador.OrganizadorRelatorioServiceInterface;
import br.eventos.zezinEventos.service.shared.EventoService;
import br.eventos.zezinEventos.service.shared.InscricaoService;
import br.eventos.zezinEventos.service.shared.OrganizadorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Implementação do serviço para relatórios do organizador.
 * 
 * Esta classe segue o Princípio da Responsabilidade Única (SRP) ao focar
 * exclusivamente na geração de relatórios do organizador.
 */
@Service
public class OrganizadorRelatorioService implements OrganizadorRelatorioServiceInterface {
    
    private final OrganizadorService organizadorService;
    private final EventoService eventoService;
    private final InscricaoService inscricaoService;
    
    @Autowired
    public OrganizadorRelatorioService(OrganizadorService organizadorService,
                                     EventoService eventoService,
                                     InscricaoService inscricaoService) {
        this.organizadorService = organizadorService;
        this.eventoService = eventoService;
        this.inscricaoService = inscricaoService;
    }
    
    @Override
    public OrganizadorRelatorioDTO gerarRelatorioCompleto(String loginOrganizador) {
        if (loginOrganizador == null || loginOrganizador.trim().isEmpty()) {
            return null;
        }
        
        try {
            Organizador organizador = organizadorService.buscarPorLogin(loginOrganizador);
            if (organizador == null) {
                return null;
            }
            
            List<Evento> eventos = eventoService.listarPorOrganizador(organizador);
            
            return criarRelatorioCompleto(eventos);
            
        } catch (Exception e) {
            return null;
        }
    }
    
    @Override
    public OrganizadorRelatorioDTO gerarRelatorioPorPeriodo(String loginOrganizador, String dataInicio, String dataFim) {
        if (loginOrganizador == null || loginOrganizador.trim().isEmpty()) {
            return null;
        }
        
        try {
            Organizador organizador = organizadorService.buscarPorLogin(loginOrganizador);
            if (organizador == null) {
                return null;
            }
            
            List<Evento> todoEventos = eventoService.listarPorOrganizador(organizador);
            
            // Filtrar por período se fornecido
            List<Evento> eventosFiltrados = filtrarPorPeriodo(todoEventos, dataInicio, dataFim);
            
            return criarRelatorioCompleto(eventosFiltrados);
            
        } catch (Exception e) {
            return null;
        }
    }
    
    @Override
    public OrganizadorRelatorioDTO obterEstatisticasResumo(String loginOrganizador) {
        return gerarRelatorioCompleto(loginOrganizador);
    }
    
    private OrganizadorRelatorioDTO criarRelatorioCompleto(List<Evento> eventos) {
        if (eventos == null || eventos.isEmpty()) {
            return new OrganizadorRelatorioDTO(eventos, 0L);
        }
        
        // Calcular total de inscrições
        long totalInscricoes = eventos.stream()
            .mapToLong(evento -> inscricaoService.contarInscricoesPorEvento(evento))
            .sum();
        
        OrganizadorRelatorioDTO relatorio = new OrganizadorRelatorioDTO(eventos, totalInscricoes);
        
        // Calcular estatísticas adicionais
        long totalVagas = eventos.stream()
            .mapToLong(Evento::getVagasTotais)
            .sum();
        
        double taxaOcupacao = totalVagas > 0 ? (totalInscricoes * 100.0) / totalVagas : 0;
        relatorio.setTaxaOcupacaoMedia(taxaOcupacao);
        
        // Encontrar evento mais popular
        Evento eventoMaisPopular = eventos.stream()
            .max((e1, e2) -> Long.compare(
                inscricaoService.contarInscricoesPorEvento(e1),
                inscricaoService.contarInscricoesPorEvento(e2)
            ))
            .orElse(null);
        
        relatorio.setEventoMaisPopular(eventoMaisPopular);
        
        // Criar mapa de inscrições por evento
        Map<String, Long> inscricoesPorEvento = new HashMap<>();
        for (Evento evento : eventos) {
            long inscricoes = inscricaoService.contarInscricoesPorEvento(evento);
            inscricoesPorEvento.put(evento.getNome(), inscricoes);
        }
        relatorio.setInscricoesPorEvento(inscricoesPorEvento);
        
        // Criar mapa de eventos por período (por mês)
        Map<String, Long> eventosPorMes = criarEstatisticasPorMes(eventos);
        relatorio.setEventosPeríodo(eventosPorMes);
        
        return relatorio;
    }
    
    private List<Evento> filtrarPorPeriodo(List<Evento> eventos, String dataInicio, String dataFim) {
        if (dataInicio == null || dataFim == null || eventos == null) {
            return eventos;
        }
        
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDateTime inicio = LocalDateTime.parse(dataInicio + "T00:00:00");
            LocalDateTime fim = LocalDateTime.parse(dataFim + "T23:59:59");
            
            return eventos.stream()
                .filter(evento -> {
                    LocalDateTime dataEvento = evento.getDataEvento();
                    return dataEvento != null && 
                           !dataEvento.isBefore(inicio) && 
                           !dataEvento.isAfter(fim);
                })
                .toList();
                
        } catch (Exception e) {
            return eventos; // Em caso de erro no formato da data, retorna todos os eventos
        }
    }
    
    private Map<String, Long> criarEstatisticasPorMes(List<Evento> eventos) {
        Map<String, Long> eventosPorMes = new HashMap<>();
        
        for (Evento evento : eventos) {
            if (evento.getDataEvento() != null) {
                String mesAno = evento.getDataEvento().format(DateTimeFormatter.ofPattern("MM/yyyy"));
                eventosPorMes.put(mesAno, eventosPorMes.getOrDefault(mesAno, 0L) + 1);
            }
        }
        
        return eventosPorMes;
    }
}
