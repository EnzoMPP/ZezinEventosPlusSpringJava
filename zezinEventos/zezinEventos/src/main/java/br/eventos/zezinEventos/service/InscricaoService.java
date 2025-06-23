package br.eventos.zezinEventos.service;

import br.eventos.zezinEventos.model.Cliente;
import br.eventos.zezinEventos.model.Evento;
import br.eventos.zezinEventos.model.Inscricao;
import br.eventos.zezinEventos.model.repository.InscricaoDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class InscricaoService {
    
    @Autowired
    private InscricaoDAO inscricaoDAO;
    
    @Autowired
    private EventoService eventoService;
    
    /**
     * Inscreve um cliente em um evento
     */
    @Transactional
    public Inscricao inscrever(Cliente cliente, Evento evento) {
        // Validações
        if (jaEstaInscrito(cliente, evento)) {
            throw new RuntimeException("Você já está inscrito neste evento!");
        }
        
        if (evento.getVagasOcupadas() >= evento.getVagasTotais()) {
            throw new RuntimeException("Evento lotado! Não há mais vagas disponíveis.");
        }
        
        if (evento.getDataEvento().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Não é possível se inscrever em eventos que já ocorreram.");
        }
        
        // Criar inscrição
        Inscricao inscricao = new Inscricao();
        inscricao.setUsuario(cliente);
        inscricao.setEvento(evento);
        inscricao.setDataInscricao(LocalDateTime.now());
        
        // Salvar inscrição
        inscricao = inscricaoDAO.save(inscricao);
        
        // Atualizar contador de vagas do evento
        evento.setVagasOcupadas(evento.getVagasOcupadas() + 1);
        eventoService.salvar(evento);
        
        return inscricao;
    }
    
    /**
     * Cancela inscrição de um cliente
     */
    @Transactional
    public void cancelarInscricao(Cliente cliente, Evento evento) {
        Optional<Inscricao> inscricaoOpt = inscricaoDAO.findByClienteAndEvento(cliente, evento);
        
        if (inscricaoOpt.isEmpty()) {
            throw new RuntimeException("Você não está inscrito neste evento!");
        }
        
        // Remover inscrição
        inscricaoDAO.delete(inscricaoOpt.get());
        
        // Atualizar contador de vagas do evento
        evento.setVagasOcupadas(evento.getVagasOcupadas() - 1);
        eventoService.salvar(evento);
    }
    
    /**
     * Verifica se cliente já está inscrito no evento
     */
    public boolean jaEstaInscrito(Cliente cliente, Evento evento) {
        return inscricaoDAO.existsByUsuarioAndEvento(cliente, evento);
    }
    
    /**
     * Lista todas as inscrições de um cliente
     */
    public List<Inscricao> listarInscricoesPorCliente(Cliente cliente) {
        return inscricaoDAO.findByCliente(cliente);
    }
    
    /**
     * Conta total de inscrições de um cliente
     */
    public Long contarInscricoesPorCliente(Cliente cliente) {
        return inscricaoDAO.countByCliente(cliente);
    }
    
    /**
     * Lista eventos próximos de um cliente
     */
    public List<Inscricao> listarEventosProximos(Cliente cliente) {
        return inscricaoDAO.findEventosProximosByCliente(cliente);
    }
    
    /**
     * Lista eventos finalizados de um cliente
     */
    public List<Inscricao> listarEventosFinalizados(Cliente cliente) {
        return inscricaoDAO.findEventosFinalizadosByCliente(cliente);
    }
}
