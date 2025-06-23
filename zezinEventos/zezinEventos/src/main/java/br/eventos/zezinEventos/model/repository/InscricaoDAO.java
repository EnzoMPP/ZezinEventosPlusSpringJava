package br.eventos.zezinEventos.model.repository;

import br.eventos.zezinEventos.model.Cliente;
import br.eventos.zezinEventos.model.Evento;
import br.eventos.zezinEventos.model.Inscricao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InscricaoDAO extends JpaRepository<Inscricao, Long> {
    
    // Buscar inscrições de um cliente
    @Query("SELECT i FROM Inscricao i WHERE i.usuario = :cliente ORDER BY i.dataInscricao DESC")
    List<Inscricao> findByCliente(@Param("cliente") Cliente cliente);
    
    // Buscar inscrições de um evento
    @Query("SELECT i FROM Inscricao i WHERE i.evento = :evento ORDER BY i.dataInscricao ASC")
    List<Inscricao> findByEvento(@Param("evento") Evento evento);
    
    // Verificar se cliente já está inscrito em um evento
    @Query("SELECT i FROM Inscricao i WHERE i.usuario = :cliente AND i.evento = :evento")
    Optional<Inscricao> findByClienteAndEvento(@Param("cliente") Cliente cliente, @Param("evento") Evento evento);
    
    // Contar inscrições de um evento
    @Query("SELECT COUNT(i) FROM Inscricao i WHERE i.evento = :evento")
    Long countByEvento(@Param("evento") Evento evento);
    
    // Contar inscrições de um cliente
    @Query("SELECT COUNT(i) FROM Inscricao i WHERE i.usuario = :cliente")
    Long countByCliente(@Param("cliente") Cliente cliente);
    
    // Buscar eventos próximos de um cliente
    @Query("SELECT i FROM Inscricao i WHERE i.usuario = :cliente AND i.evento.dataEvento > CURRENT_TIMESTAMP ORDER BY i.evento.dataEvento ASC")
    List<Inscricao> findEventosProximosByCliente(@Param("cliente") Cliente cliente);
    
    // Buscar eventos finalizados de um cliente
    @Query("SELECT i FROM Inscricao i WHERE i.usuario = :cliente AND i.evento.dataEvento < CURRENT_TIMESTAMP ORDER BY i.evento.dataEvento DESC")
    List<Inscricao> findEventosFinalizadosByCliente(@Param("cliente") Cliente cliente);
    
    // Verificar se existe inscrição
    boolean existsByUsuarioAndEvento(Cliente cliente, Evento evento);
}
