package br.eventos.zezinEventos.service.organizador;

import br.eventos.zezinEventos.model.Evento;
import br.eventos.zezinEventos.model.Organizador;
import br.eventos.zezinEventos.model.dto.organizador.OrganizadorDashboardDTO;
import br.eventos.zezinEventos.service.interfaces.organizador.OrganizadorDashboardServiceInterface;
import br.eventos.zezinEventos.service.shared.EventoService;
import br.eventos.zezinEventos.service.shared.InscricaoService;
import br.eventos.zezinEventos.service.shared.OrganizadorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Implementação do serviço para dashboard do organizador.
 * 
 * Esta classe segue o Princípio da Responsabilidade Única (SRP) ao focar
 * exclusivamente nas operações de dashboard do organizador.
 */
@Service
public class OrganizadorDashboardService implements OrganizadorDashboardServiceInterface {
    
    private final OrganizadorService organizadorService;
    private final EventoService eventoService;
    private final InscricaoService inscricaoService;
    
    @Autowired
    public OrganizadorDashboardService(OrganizadorService organizadorService,
                                     EventoService eventoService,
                                     InscricaoService inscricaoService) {
        this.organizadorService = organizadorService;
        this.eventoService = eventoService;
        this.inscricaoService = inscricaoService;
    }
    
    @Override
    public OrganizadorDashboardDTO obterDashboard(String loginOrganizador) {
        if (loginOrganizador == null || loginOrganizador.trim().isEmpty()) {
            return null;
        }
        
        try {
            Organizador organizador = organizadorService.buscarPorLogin(loginOrganizador);
            if (organizador == null) {
                return null;
            }
            
            return calcularEstatisticasDashboard(organizador);
            
        } catch (Exception e) {
            // Log do erro seria implementado aqui
            return null;
        }
    }
    
    @Override
    public OrganizadorDashboardDTO atualizarEstatisticas(String loginOrganizador) {
        // Implementação idêntica ao obterDashboard para garantir dados atualizados
        return obterDashboard(loginOrganizador);
    }
    
    /**
     * Calcula as estatísticas do dashboard para um organizador específico.
     * 
     * @param organizador Organizador para calcular estatísticas
     * @return DTO com estatísticas calculadas
     */
    private OrganizadorDashboardDTO calcularEstatisticasDashboard(Organizador organizador) {
        try {
            // Buscar eventos do organizador
            List<Evento> eventosDoOrganizador = eventoService.listarPorOrganizador(organizador);
            
            // Calcular estatísticas básicas
            long totalEventos = eventosDoOrganizador.size();
            long eventosAtivos = eventosDoOrganizador.stream()
                .filter(e -> e.getDataEvento() != null && 
                            e.getDataEvento().isAfter(java.time.LocalDateTime.now()))
                .count();
            long eventosFinalizados = totalEventos - eventosAtivos;
            
            // Calcular total de inscrições
            long totalInscricoes = eventosDoOrganizador.stream()
                .mapToLong(evento -> inscricaoService.contarInscricoesPorEvento(evento))
                .sum();
            
            return new OrganizadorDashboardDTO(organizador, totalEventos, 
                                             eventosAtivos, eventosFinalizados, totalInscricoes);
            
        } catch (Exception e) {
            // Em caso de erro, retornar DTO com dados básicos do organizador
            return new OrganizadorDashboardDTO(organizador, 0L, 0L, 0L, 0L);
        }
    }
}
