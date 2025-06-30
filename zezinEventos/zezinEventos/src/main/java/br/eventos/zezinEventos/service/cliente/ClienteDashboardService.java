package br.eventos.zezinEventos.service.cliente;

import br.eventos.zezinEventos.model.Cliente;
import br.eventos.zezinEventos.model.dto.cliente.ClienteDashboardDTO;
import br.eventos.zezinEventos.service.interfaces.cliente.ClienteDashboardServiceInterface;
import br.eventos.zezinEventos.service.shared.ClienteService;
import br.eventos.zezinEventos.service.shared.InscricaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implementação do serviço para dashboard do cliente.
 */
@Service
public class ClienteDashboardService implements ClienteDashboardServiceInterface {
    
    private final ClienteService clienteService;
    private final InscricaoService inscricaoService;
    
    @Autowired
    public ClienteDashboardService(ClienteService clienteService, InscricaoService inscricaoService) {
        this.clienteService = clienteService;
        this.inscricaoService = inscricaoService;
    }
    
    @Override
    public ClienteDashboardDTO obterDashboard(String loginCliente) {
        if (loginCliente == null || loginCliente.trim().isEmpty()) {
            return null;
        }
        
        try {
            Cliente cliente = clienteService.buscarPorLogin(loginCliente);
            if (cliente == null) {
                return null;
            }
            
            return calcularEstatisticasDashboard(cliente);
            
        } catch (Exception e) {
            // Log do erro seria implementado aqui
            return null;
        }
    }
    
    @Override
    public ClienteDashboardDTO atualizarEstatisticas(String loginCliente) {
        // Implementação idêntica ao obterDashboard para garantir dados atualizados
        return obterDashboard(loginCliente);
    }
    
    /**
     * Calcula as estatísticas do dashboard para um cliente específico.
     * 
     * @param cliente Cliente para calcular estatísticas
     * @return DTO com estatísticas calculadas
     */
    private ClienteDashboardDTO calcularEstatisticasDashboard(Cliente cliente) {
        try {
            // Obter estatísticas das inscrições
            Long totalInscritos = inscricaoService.contarInscricoesPorCliente(cliente);
            Long eventosProximos = (long) inscricaoService.listarEventosProximos(cliente).size();
            Long eventosFinalizados = (long) inscricaoService.listarEventosFinalizados(cliente).size();
            
            return new ClienteDashboardDTO(cliente, totalInscritos, eventosProximos, eventosFinalizados);
            
        } catch (Exception e) {
            // Em caso de erro, retornar DTO com dados básicos do cliente
            return new ClienteDashboardDTO(cliente, 0L, 0L, 0L);
        }
    }
}
