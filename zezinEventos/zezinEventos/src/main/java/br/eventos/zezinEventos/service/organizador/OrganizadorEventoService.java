package br.eventos.zezinEventos.service.organizador;

import br.eventos.zezinEventos.model.Evento;
import br.eventos.zezinEventos.model.Organizador;
import br.eventos.zezinEventos.model.dto.organizador.OrganizadorEventosDTO;
import br.eventos.zezinEventos.service.interfaces.organizador.OrganizadorEventoServiceInterface;
import br.eventos.zezinEventos.service.shared.EventoService;
import br.eventos.zezinEventos.service.shared.InscricaoService;
import br.eventos.zezinEventos.service.shared.OrganizadorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Implementação do serviço para gerenciamento de eventos do organizador.
 */
@Service
public class OrganizadorEventoService implements OrganizadorEventoServiceInterface {
    
    private final OrganizadorService organizadorService;
    private final EventoService eventoService;
    private final InscricaoService inscricaoService;
    
    @Autowired
    public OrganizadorEventoService(OrganizadorService organizadorService,
                                  EventoService eventoService,
                                  InscricaoService inscricaoService) {
        this.organizadorService = organizadorService;
        this.eventoService = eventoService;
        this.inscricaoService = inscricaoService;
    }
    
    @Override
    public OrganizadorEventosDTO obterEventosDoOrganizador(String loginOrganizador) {
        if (loginOrganizador == null || loginOrganizador.trim().isEmpty()) {
            return null;
        }
        
        try {
            Organizador organizador = organizadorService.buscarPorLogin(loginOrganizador);
            if (organizador == null) {
                return null;
            }
            
            List<Evento> eventos = eventoService.listarPorOrganizador(organizador);
            return new OrganizadorEventosDTO(eventos, null);
            
        } catch (Exception e) {
            return null;
        }
    }
    
    @Override
    public OrganizadorEventosDTO buscarEventosComFiltro(String loginOrganizador, String filtroNome) {
        if (loginOrganizador == null || loginOrganizador.trim().isEmpty()) {
            return null;
        }
        
        try {
            Organizador organizador = organizadorService.buscarPorLogin(loginOrganizador);
            if (organizador == null) {
                return null;
            }
            
            List<Evento> eventos = eventoService.listarPorOrganizador(organizador);
            
            // Filtrar por nome se fornecido
            if (filtroNome != null && !filtroNome.trim().isEmpty()) {
                eventos = eventos.stream()
                    .filter(e -> e.getNome().toLowerCase().contains(filtroNome.toLowerCase()))
                    .toList();
            }
            
            return new OrganizadorEventosDTO(eventos, filtroNome);
            
        } catch (Exception e) {
            return null;
        }
    }
    
    @Override
    public Evento prepararNovoEvento(String loginOrganizador) {
        try {
            Organizador organizador = organizadorService.buscarPorLogin(loginOrganizador);
            if (organizador == null) {
                throw new RuntimeException("Organizador não encontrado");
            }
            
            Evento novoEvento = new Evento();
            novoEvento.setOrganizador(organizador);
            
            return novoEvento;
            
        } catch (Exception e) {
            throw new RuntimeException("Erro ao preparar novo evento: " + e.getMessage());
        }
    }
    
    @Override
    public Evento salvarEvento(Evento evento, String loginOrganizador) {
        try {
            // Validações básicas
            if (evento == null) {
                throw new RuntimeException("Dados do evento são obrigatórios");
            }
            
            Organizador organizador = organizadorService.buscarPorLogin(loginOrganizador);
            if (organizador == null) {
                throw new RuntimeException("Organizador não encontrado");
            }
            
            // Verificar se é edição ou nova criação
            if (evento.getId() != null) {
                // Edição: verificar se o evento pertence ao organizador
                Evento eventoExistente = eventoService.buscarPorId(evento.getId());
                if (eventoExistente == null) {
                    throw new RuntimeException("Evento não encontrado");
                }
                
                if (!eventoExistente.getOrganizador().getId().equals(organizador.getId())) {
                    throw new RuntimeException("Você não tem permissão para editar este evento");
                }
            }
            
            // Garantir que o organizador está definido
            evento.setOrganizador(organizador);
            
            // Salvar evento
            return eventoService.salvar(evento);
            
        } catch (Exception e) {
            throw new RuntimeException("Erro ao salvar evento: " + e.getMessage());
        }
    }
    
    @Override
    public Evento buscarEventoParaEdicao(Long eventoId, String loginOrganizador) {
        try {
            if (eventoId == null) {
                throw new RuntimeException("ID do evento é obrigatório");
            }
            
            Organizador organizador = organizadorService.buscarPorLogin(loginOrganizador);
            if (organizador == null) {
                throw new RuntimeException("Organizador não encontrado");
            }
            
            Evento evento = eventoService.buscarPorId(eventoId);
            if (evento == null) {
                throw new RuntimeException("Evento não encontrado");
            }
            
            // Verificar se o evento pertence ao organizador
            if (!evento.getOrganizador().getId().equals(organizador.getId())) {
                throw new RuntimeException("Você não tem permissão para editar este evento");
            }
            
            return evento;
            
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar evento: " + e.getMessage());
        }
    }
    
    @Override
    public void excluirEvento(Long eventoId, String loginOrganizador) {
        try {
            if (eventoId == null) {
                throw new RuntimeException("ID do evento é obrigatório");
            }
            
            Organizador organizador = organizadorService.buscarPorLogin(loginOrganizador);
            if (organizador == null) {
                throw new RuntimeException("Organizador não encontrado");
            }
            
            Evento evento = eventoService.buscarPorId(eventoId);
            if (evento == null) {
                throw new RuntimeException("Evento não encontrado");
            }
            
            // Verificar se o evento pertence ao organizador
            if (!evento.getOrganizador().getId().equals(organizador.getId())) {
                throw new RuntimeException("Você não tem permissão para excluir este evento");
            }
            
            // Excluir evento
            eventoService.excluir(eventoId);
            
        } catch (Exception e) {
            throw new RuntimeException("Erro ao excluir evento: " + e.getMessage());
        }
    }
}
