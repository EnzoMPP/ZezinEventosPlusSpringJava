package br.eventos.zezinEventos.service.cliente;

import br.eventos.zezinEventos.model.Cliente;
import br.eventos.zezinEventos.model.Evento;
import br.eventos.zezinEventos.model.Inscricao;
import br.eventos.zezinEventos.model.dto.cliente.ClienteEventosDTO;
import br.eventos.zezinEventos.service.interfaces.cliente.ClienteEventoServiceInterface;
import br.eventos.zezinEventos.service.shared.ClienteService;
import br.eventos.zezinEventos.service.shared.EventoService;
import br.eventos.zezinEventos.service.shared.InscricaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Implementação do serviço para eventos do cliente.
 * 
 * Esta classe segue o Princípio da Responsabilidade Única (SRP) ao focar
 * exclusivamente nas operações de visualização de eventos para o cliente.
 */
@Service
public class ClienteEventoService implements ClienteEventoServiceInterface {
    
    private final ClienteService clienteService;
    private final EventoService eventoService;
    private final InscricaoService inscricaoService;
    
    @Autowired
    public ClienteEventoService(ClienteService clienteService, 
                               EventoService eventoService,
                               InscricaoService inscricaoService) {
        this.clienteService = clienteService;
        this.eventoService = eventoService;
        this.inscricaoService = inscricaoService;
    }
    
    @Override
    public ClienteEventosDTO obterEventosDisponiveis(String loginCliente) {
        if (loginCliente == null || loginCliente.trim().isEmpty()) {
            return new ClienteEventosDTO();
        }
        
        try {
            Cliente cliente = clienteService.buscarPorLogin(loginCliente);
            if (cliente == null) {
                return new ClienteEventosDTO();
            }
            
            // Buscar eventos disponíveis
            List<Evento> eventosDisponiveis = eventoService.listarEventosAbertos();
            
            // Buscar inscrições do cliente
            List<Inscricao> inscricoesCliente = inscricaoService.listarInscricoesPorCliente(cliente);
            
            return new ClienteEventosDTO(eventosDisponiveis, inscricoesCliente);
            
        } catch (Exception e) {
            // Log do erro seria implementado aqui
            return new ClienteEventosDTO();
        }
    }
    
    @Override
    public ClienteEventosDTO buscarEventosComFiltro(String loginCliente, String filtroNome, String filtroTipo) {
        if (loginCliente == null || loginCliente.trim().isEmpty()) {
            return new ClienteEventosDTO();
        }
        
        try {
            Cliente cliente = clienteService.buscarPorLogin(loginCliente);
            if (cliente == null) {
                return new ClienteEventosDTO();
            }
            
            // Buscar eventos com filtros
            List<Evento> eventosDisponiveis = buscarEventosComFiltros(filtroNome, filtroTipo);
            
            // Buscar inscrições do cliente
            List<Inscricao> inscricoesCliente = inscricaoService.listarInscricoesPorCliente(cliente);
            
            return new ClienteEventosDTO(eventosDisponiveis, inscricoesCliente);
            
        } catch (Exception e) {
            // Log do erro seria implementado aqui
            return new ClienteEventosDTO();
        }
    }
    
    /**
     * Busca eventos aplicando filtros específicos.
     * 
     * @param filtroNome Filtro por nome (opcional)
     * @param filtroTipo Filtro por tipo (opcional)
     * @return Lista de eventos filtrados
     */
    private List<Evento> buscarEventosComFiltros(String filtroNome, String filtroTipo) {
        // Implementação básica - pode ser expandida conforme necessário
        List<Evento> eventos = eventoService.listarEventosAbertos();
        
        // Aplicar filtros se fornecidos
        if (filtroNome != null && !filtroNome.trim().isEmpty()) {
            eventos = eventos.stream()
                    .filter(evento -> evento.getNome().toLowerCase()
                            .contains(filtroNome.toLowerCase()))
                    .toList();
        }
        
        if (filtroTipo != null && !filtroTipo.trim().isEmpty()) {
            eventos = eventos.stream()
                    .filter(evento -> evento.getTipo() != null &&
                            evento.getTipo().toString().toLowerCase()
                            .contains(filtroTipo.toLowerCase()))
                    .toList();
        }
        
        return eventos;
    }
}
