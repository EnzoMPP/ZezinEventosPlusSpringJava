package br.eventos.zezinEventos.service.cliente;

import br.eventos.zezinEventos.model.Cliente;
import br.eventos.zezinEventos.model.Evento;
import br.eventos.zezinEventos.model.Inscricao;
import br.eventos.zezinEventos.model.dto.cliente.ClienteInscricoesDTO;
import br.eventos.zezinEventos.service.interfaces.cliente.ClienteInscricaoServiceInterface;
import br.eventos.zezinEventos.service.shared.ClienteService;
import br.eventos.zezinEventos.service.shared.EventoService;
import br.eventos.zezinEventos.service.shared.InscricaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Implementação do serviço para inscrições do cliente.
 * 
 * Esta classe segue o Princípio da Responsabilidade Única (SRP) ao focar
 * exclusivamente nas operações de inscrição do cliente.
 */
@Service
public class ClienteInscricaoService implements ClienteInscricaoServiceInterface {
    
    private final ClienteService clienteService;
    private final EventoService eventoService;
    private final InscricaoService inscricaoService;
    
    @Autowired
    public ClienteInscricaoService(ClienteService clienteService,
                                  EventoService eventoService,
                                  InscricaoService inscricaoService) {
        this.clienteService = clienteService;
        this.eventoService = eventoService;
        this.inscricaoService = inscricaoService;
    }
    
    @Override
    public ClienteInscricoesDTO obterInscricoesAtivas(String loginCliente) {
        if (loginCliente == null || loginCliente.trim().isEmpty()) {
            return new ClienteInscricoesDTO();
        }
        
        try {
            Cliente cliente = clienteService.buscarPorLogin(loginCliente);
            if (cliente == null) {
                return new ClienteInscricoesDTO();
            }
            
            List<Inscricao> inscricoesAtivas = inscricaoService.listarEventosProximos(cliente);
            List<Inscricao> historicoCompleto = inscricaoService.listarInscricoesPorCliente(cliente);
            
            return new ClienteInscricoesDTO(inscricoesAtivas, historicoCompleto);
            
        } catch (Exception e) {
            // Log do erro seria implementado aqui
            return new ClienteInscricoesDTO();
        }
    }
    
    @Override
    public ClienteInscricoesDTO obterHistoricoCompleto(String loginCliente) {
        if (loginCliente == null || loginCliente.trim().isEmpty()) {
            return new ClienteInscricoesDTO();
        }
        
        try {
            Cliente cliente = clienteService.buscarPorLogin(loginCliente);
            if (cliente == null) {
                return new ClienteInscricoesDTO();
            }
            
            List<Inscricao> historicoCompleto = inscricaoService.listarInscricoesPorCliente(cliente);
            List<Inscricao> inscricoesAtivas = inscricaoService.listarEventosProximos(cliente);
            
            return new ClienteInscricoesDTO(inscricoesAtivas, historicoCompleto);
            
        } catch (Exception e) {
            // Log do erro seria implementado aqui
            return new ClienteInscricoesDTO();
        }
    }
    
    @Override
    public ResultadoInscricao inscreverEmEvento(String loginCliente, Long eventoId) {
        if (loginCliente == null || eventoId == null) {
            return ResultadoInscricao.erro("Dados inválidos para inscrição.");
        }
        
        try {
            Cliente cliente = clienteService.buscarPorLogin(loginCliente);
            if (cliente == null) {
                return ResultadoInscricao.erro("Cliente não encontrado!");
            }
            
            Evento evento = eventoService.buscarPorId(eventoId);
            if (evento == null) {
                return ResultadoInscricao.erro("Evento não encontrado!");
            }
            
            // Verificar se pode se inscrever
            if (!podeSeInscrever(loginCliente, eventoId)) {
                return ResultadoInscricao.aviso("Não é possível se inscrever neste evento no momento.");
            }
            
            // Realizar inscrição
            inscricaoService.inscrever(cliente, evento);
            
            return ResultadoInscricao.sucesso("Inscrição realizada com sucesso!");
            
        } catch (Exception e) {
            return ResultadoInscricao.erro("Erro ao realizar inscrição: " + e.getMessage());
        }
    }
    
    @Override
    public ResultadoInscricao cancelarInscricao(String loginCliente, Long eventoId) {
        if (loginCliente == null || eventoId == null) {
            return ResultadoInscricao.erro("Dados inválidos para cancelamento.");
        }
        
        try {
            Cliente cliente = clienteService.buscarPorLogin(loginCliente);
            if (cliente == null) {
                return ResultadoInscricao.erro("Cliente não encontrado!");
            }
            
            Evento evento = eventoService.buscarPorId(eventoId);
            if (evento == null) {
                return ResultadoInscricao.erro("Evento não encontrado!");
            }
            
            // Realizar cancelamento
            inscricaoService.cancelarInscricao(cliente, evento);
            
            return ResultadoInscricao.sucesso("Inscrição cancelada com sucesso!");
            
        } catch (Exception e) {
            return ResultadoInscricao.erro("Erro ao cancelar inscrição: " + e.getMessage());
        }
    }
    
    @Override
    public boolean podeSeInscrever(String loginCliente, Long eventoId) {
        if (loginCliente == null || eventoId == null) {
            return false;
        }
        
        try {
            Cliente cliente = clienteService.buscarPorLogin(loginCliente);
            if (cliente == null) {
                return false;
            }
            
            Evento evento = eventoService.buscarPorId(eventoId);
            if (evento == null) {
                return false;
            }
            
            // Verificar se já está inscrito
            List<Inscricao> inscricoesCliente = inscricaoService.listarInscricoesPorCliente(cliente);
            boolean jaInscrito = inscricoesCliente.stream()
                    .anyMatch(inscricao -> inscricao.getEvento().getId().equals(eventoId));
            
            if (jaInscrito) {
                return false;
            }
            
            // Verificar se o evento está aberto para inscrições
            // (Esta lógica pode ser expandida conforme as regras de negócio)
            return evento.getStatus() != null; // Simplificado - pode ser mais complexo
            
        } catch (Exception e) {
            return false;
        }
    }
}
