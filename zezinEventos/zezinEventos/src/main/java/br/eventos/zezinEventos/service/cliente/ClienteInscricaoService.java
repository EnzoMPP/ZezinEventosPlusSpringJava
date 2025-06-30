package br.eventos.zezinEventos.service.cliente;

import br.eventos.zezinEventos.model.Cliente;
import br.eventos.zezinEventos.model.Evento;
import br.eventos.zezinEventos.model.Inscricao;
import br.eventos.zezinEventos.model.dto.cliente.ClienteInscricoesDTO;
import br.eventos.zezinEventos.service.interfaces.cliente.ClienteInscricaoServiceInterface;
import br.eventos.zezinEventos.service.shared.ClienteService;
import br.eventos.zezinEventos.service.shared.EventoService;
import br.eventos.zezinEventos.service.shared.InscricaoService;
import br.eventos.zezinEventos.service.shared.ListaEsperaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Implementação do serviço para inscrições do cliente.
 */
@Service
public class ClienteInscricaoService implements ClienteInscricaoServiceInterface {
    
    private final ClienteService clienteService;
    private final EventoService eventoService;
    private final InscricaoService inscricaoService;
    private final ListaEsperaService listaEsperaService;
    
    @Autowired
    public ClienteInscricaoService(ClienteService clienteService,
                                  EventoService eventoService,
                                  InscricaoService inscricaoService,
                                  ListaEsperaService listaEsperaService) {
        this.clienteService = clienteService;
        this.eventoService = eventoService;
        this.inscricaoService = inscricaoService;
        this.listaEsperaService = listaEsperaService;
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
            
            // Verificar se já está inscrito
            List<Inscricao> inscricoesCliente = inscricaoService.listarInscricoesPorCliente(cliente);
            boolean jaInscrito = inscricoesCliente.stream()
                    .anyMatch(inscricao -> inscricao.getEvento().getId().equals(eventoId));
            
            if (jaInscrito) {
                return ResultadoInscricao.aviso("Você já está inscrito neste evento!");
            }
              // Verificar se já está na lista de espera
            if (listaEsperaService.estaNaListaEspera(cliente, evento)) {
                int posicao = listaEsperaService.obterPosicaoNaFila(cliente, evento);
                return ResultadoInscricao.aviso("Você já está na lista de espera na posição " + posicao + "!");
            }
            
            // Verificar se há vagas disponíveis
            if (evento.temVagasDisponiveis()) {
                // Realizar inscrição direta
                inscricaoService.inscrever(cliente, evento);
                return ResultadoInscricao.sucesso("Inscrição realizada com sucesso!");
            } else {
                // Adicionar à lista de espera
                listaEsperaService.adicionarNaFila(cliente, evento);
                int posicao = listaEsperaService.obterPosicaoNaFila(cliente, evento);
                return ResultadoInscricao.aviso("Evento lotado! Você foi adicionado à lista de espera na posição " + posicao + ".");
            }
            
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
            return evento.getStatus() != null && evento.getAtivo();
            
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Remove o cliente da lista de espera de um evento.
     */
    public ResultadoInscricao sairDaListaEspera(String loginCliente, Long eventoId) {
        if (loginCliente == null || eventoId == null) {
            return ResultadoInscricao.erro("Dados inválidos.");
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
              if (!listaEsperaService.estaNaListaEspera(cliente, evento)) {
                return ResultadoInscricao.aviso("Você não está na lista de espera deste evento.");
            }
            
            listaEsperaService.removerDaFila(cliente, evento);
            return ResultadoInscricao.sucesso("Você foi removido da lista de espera!");
            
        } catch (Exception e) {
            return ResultadoInscricao.erro("Erro ao sair da lista de espera: " + e.getMessage());
        }
    }
    
    /**
     * Consulta a posição do cliente na lista de espera.
     */
    public int obterPosicaoNaListaEspera(String loginCliente, Long eventoId) {
        if (loginCliente == null || eventoId == null) {
            return -1;
        }
        
        try {
            Cliente cliente = clienteService.buscarPorLogin(loginCliente);
            Evento evento = eventoService.buscarPorId(eventoId);
            
            if (cliente == null || evento == null) {
                return -1;
            }
            
            return listaEsperaService.obterPosicaoNaFila(cliente, evento);
            
        } catch (Exception e) {
            return -1;
        }
    }
    
    /**
     * Verifica se o cliente está na lista de espera.
     */
    public boolean clienteEstaNaListaEspera(String loginCliente, Long eventoId) {
        if (loginCliente == null || eventoId == null) {
            return false;
        }
        
        try {
            Cliente cliente = clienteService.buscarPorLogin(loginCliente);
            Evento evento = eventoService.buscarPorId(eventoId);
            
            if (cliente == null || evento == null) {
                return false;
            }
            
            return listaEsperaService.estaNaListaEspera(cliente, evento);
            
        } catch (Exception e) {
            return false;
        }
    }
}
