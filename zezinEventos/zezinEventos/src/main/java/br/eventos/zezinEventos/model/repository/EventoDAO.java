package br.eventos.zezinEventos.model.repository;

import br.eventos.zezinEventos.model.Evento;
import br.eventos.zezinEventos.model.Organizador;
import br.eventos.zezinEventos.model.StatusEvento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EventoDAO extends JpaRepository<Evento, Long> {
    
    // Busca eventos por organizador
    List<Evento> findByOrganizador(Organizador organizador);
    
    // Busca eventos ativos
    List<Evento> findByAtivoTrue();
    
    // Busca eventos por status
    List<Evento> findByStatus(StatusEvento status);
      // Busca eventos abertos (para inscrições) - com vagas e futuros
    @Query("SELECT e FROM Evento e WHERE e.status = 'ABERTO' AND e.ativo = true AND e.dataEvento > CURRENT_TIMESTAMP AND e.vagasOcupadas < e.vagasTotais ORDER BY e.dataEvento ASC")
    List<Evento> findEventosAbertos();
    
    // Busca eventos futuros
    @Query("SELECT e FROM Evento e WHERE e.dataEvento > :agora AND e.ativo = true ORDER BY e.dataEvento")
    List<Evento> findEventosFuturos(@Param("agora") LocalDateTime agora);
    
    // Busca por nome (case insensitive)
    List<Evento> findByNomeContainingIgnoreCase(String nome);
}
