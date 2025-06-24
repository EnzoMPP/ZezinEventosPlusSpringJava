package br.eventos.zezinEventos.model.repository;

import br.eventos.zezinEventos.model.ListaEspera;
import br.eventos.zezinEventos.model.Evento;
import br.eventos.zezinEventos.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository para gerenciamento da lista de espera.
 * Implementa operações específicas para controle da fila FIFO.
 */
@Repository
public interface ListaEsperaDAO extends JpaRepository<ListaEspera, Long> {
    
    /**
     * Busca todos os itens ativos da lista de espera de um evento,
     * ordenados por posição (FIFO).
     */
    @Query("SELECT le FROM ListaEspera le WHERE le.evento = :evento AND le.ativo = true ORDER BY le.posicao ASC")
    List<ListaEspera> findByEventoAndAtivoTrueOrderByPosicaoAsc(@Param("evento") Evento evento);
    
    /**
     * Busca a próxima pessoa da fila (menor posição).
     */
    @Query("SELECT le FROM ListaEspera le WHERE le.evento = :evento AND le.ativo = true ORDER BY le.posicao ASC LIMIT 1")
    Optional<ListaEspera> findProximoDaFila(@Param("evento") Evento evento);
    
    /**
     * Conta quantas pessoas estão na lista de espera de um evento.
     */
    @Query("SELECT COUNT(le) FROM ListaEspera le WHERE le.evento = :evento AND le.ativo = true")
    Long contarPessoasNaFila(@Param("evento") Evento evento);
    
    /**
     * Verifica se um cliente já está na lista de espera de um evento.
     */
    @Query("SELECT le FROM ListaEspera le WHERE le.evento = :evento AND le.cliente = :cliente AND le.ativo = true")
    Optional<ListaEspera> findByEventoAndClienteAndAtivoTrue(@Param("evento") Evento evento, @Param("cliente") Cliente cliente);
    
    /**
     * Busca a posição de um cliente na lista de espera.
     */
    @Query("SELECT le.posicao FROM ListaEspera le WHERE le.evento = :evento AND le.cliente = :cliente AND le.ativo = true")
    Optional<Integer> findPosicaoNaFila(@Param("evento") Evento evento, @Param("cliente") Cliente cliente);
    
    /**
     * Busca a maior posição atual para calcular a próxima.
     */
    @Query("SELECT MAX(le.posicao) FROM ListaEspera le WHERE le.evento = :evento AND le.ativo = true")
    Optional<Integer> findMaiorPosicao(@Param("evento") Evento evento);
    
    /**
     * Lista de espera de um cliente específico.
     */
    @Query("SELECT le FROM ListaEspera le WHERE le.cliente = :cliente AND le.ativo = true ORDER BY le.dataEntrada DESC")
    List<ListaEspera> findByClienteAndAtivoTrueOrderByDataEntradaDesc(@Param("cliente") Cliente cliente);
}
