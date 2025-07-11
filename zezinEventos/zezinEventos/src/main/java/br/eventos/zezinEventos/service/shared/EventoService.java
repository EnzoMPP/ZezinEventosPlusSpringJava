package br.eventos.zezinEventos.service.shared;

import br.eventos.zezinEventos.model.Evento;
import br.eventos.zezinEventos.model.Organizador;
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
    }    /**
     * Lista eventos abertos para inscrição (para clientes)
     */
    public List<Evento> listarEventosAbertos() {
        return eventoDAO.findEventosAbertos();
    }
    
    /**
     * Lista eventos com vagas disponíveis (não lotados)
     */
    public List<Evento> listarEventosComVagasDisponiveis() {
        return eventoDAO.findEventosComVagasDisponiveis();
    }

    // Lista eventos futuros
    public List<Evento> listarEventosFuturos() {
        return eventoDAO.findEventosFuturos(LocalDateTime.now());
    }    // Busca evento por ID
    public Evento buscarPorId(Long id) {
        return eventoDAO.findById(id)
                .orElseThrow(() -> new RuntimeException("Evento não encontrado"));
    }

    // Busca eventos por nome
    public List<Evento> buscarPorNome(String nome) {
        return eventoDAO.findByNomeContainingIgnoreCase(nome);
    }

    // Conta total de eventos
    public long contarTodos() {
        return eventoDAO.count();
    }    // Salva ou atualiza evento
    @Transactional
    public Evento salvar(Evento evento) {
        // Validações básicas
        validarEvento(evento);
        
        // Se for uma atualização, verificar se organizador não foi perdido
        if (evento.getId() != null && evento.getOrganizador() == null) {
            Evento eventoExistente = buscarPorId(evento.getId());
            evento.setOrganizador(eventoExistente.getOrganizador());
        }
        
        return eventoDAO.save(evento);
    }
    
    // Método específico para edições admin que preserva dados importantes
    @Transactional
    public Evento salvarEdicaoAdmin(Evento eventoEditado) {
        if (eventoEditado.getId() == null) {
            throw new RuntimeException("ID do evento é obrigatório para edição");
        }
        
        // Buscar evento original
        Evento eventoOriginal = buscarPorId(eventoEditado.getId());
        
        // Preservar campos que não devem ser alterados
        eventoEditado.setOrganizador(eventoOriginal.getOrganizador());
        eventoEditado.setDataCriacao(eventoOriginal.getDataCriacao());
        
        // Validar apenas campos editáveis
        if (eventoEditado.getNome() == null || eventoEditado.getNome().trim().isEmpty()) {
            throw new RuntimeException("Nome do evento é obrigatório");
        }
        
        if (eventoEditado.getDataEvento() == null) {
            throw new RuntimeException("Data do evento é obrigatória");
        }
        
        if (eventoEditado.getVagasTotais() == null || eventoEditado.getVagasTotais() <= 0) {
            throw new RuntimeException("Número de vagas deve ser maior que zero");
        }
        
        if (eventoEditado.getLocal() == null || eventoEditado.getLocal().trim().isEmpty()) {
            throw new RuntimeException("Local do evento é obrigatório");
        }
        
        return eventoDAO.save(eventoEditado);
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
    }    // Validações básicas
    private void validarEvento(Evento evento) {
        if (evento.getNome() == null || evento.getNome().trim().isEmpty()) {
            throw new RuntimeException("Nome do evento é obrigatório");
        }
        
        if (evento.getDataEvento() == null) {
            throw new RuntimeException("Data do evento é obrigatória");
        }
        
        // Para novos eventos, não permitir data no passado
        // Para edições, permitir (admin pode editar eventos passados)
        if (evento.getId() == null && evento.getDataEvento().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Data do evento não pode ser no passado");
        }
        
        if (evento.getVagasTotais() == null || evento.getVagasTotais() <= 0) {
            throw new RuntimeException("Número de vagas deve ser maior que zero");
        }
        
        if (evento.getLocal() == null || evento.getLocal().trim().isEmpty()) {
            throw new RuntimeException("Local do evento é obrigatório");
        }
        
        // Verificar se organizador existe (apenas para novos eventos)
        if (evento.getId() == null && evento.getOrganizador() == null) {
            throw new RuntimeException("Organizador é obrigatório");
        }
    }
}
