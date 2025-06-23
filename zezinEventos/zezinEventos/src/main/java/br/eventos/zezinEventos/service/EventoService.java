package br.eventos.zezinEventos.service;

import br.eventos.zezinEventos.model.Evento;
import br.eventos.zezinEventos.model.Organizador;
import br.eventos.zezinEventos.model.StatusEvento;
import br.eventos.zezinEventos.model.repository.EventoDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class EventoService {

    @Autowired
    private EventoDAO eventoDAO;

    // Lista todos os eventos
    public List<Evento> listarTodos() {
        return eventoDAO.findAll();
    }

    // Lista eventos por organizador
    public List<Evento> listarPorOrganizador(Organizador organizador) {
        return eventoDAO.findByOrganizador(organizador);
    }

    /**
     * Lista eventos abertos para inscrição (para clientes)
     */
    public List<Evento> listarEventosAbertos() {
        return eventoDAO.findEventosAbertos();
    }

    // Lista eventos futuros
    public List<Evento> listarEventosFuturos() {
        return eventoDAO.findEventosFuturos(LocalDateTime.now());
    }

    // Busca evento por ID
    public Evento buscarPorId(Long id) {
        return eventoDAO.findById(id)
                .orElseThrow(() -> new RuntimeException("Evento não encontrado"));
    }

    // Salva ou atualiza evento
    @Transactional
    public Evento salvar(Evento evento) {
        validarEvento(evento);
        return eventoDAO.save(evento);
    }

    // Exclui evento
    @Transactional
    public void excluir(Long id) {
        Evento evento = buscarPorId(id);
        eventoDAO.delete(evento);
    }

    // Verifica se tem vagas disponíveis
    public boolean temVagasDisponiveis(Long eventoId) {
        Evento evento = buscarPorId(eventoId);
        return evento.temVagasDisponiveis();
    }

    // Validações básicas
    private void validarEvento(Evento evento) {
        if (evento.getNome() == null || evento.getNome().trim().isEmpty()) {
            throw new RuntimeException("Nome do evento é obrigatório");
        }
        
        if (evento.getDataEvento() == null) {
            throw new RuntimeException("Data do evento é obrigatória");
        }
        
        if (evento.getDataEvento().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Data do evento não pode ser no passado");
        }
        
        if (evento.getVagasTotais() == null || evento.getVagasTotais() <= 0) {
            throw new RuntimeException("Número de vagas deve ser maior que zero");
        }
        
        if (evento.getLocal() == null || evento.getLocal().trim().isEmpty()) {
            throw new RuntimeException("Local do evento é obrigatório");
        }
    }
}
