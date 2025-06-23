package br.eventos.zezinEventos.model.repository;

import br.eventos.zezinEventos.model.Administrador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AdministradorDAO extends JpaRepository<Administrador, Long> {
    Administrador findByLogin(String login);
    
    List<Administrador> findByNomeContainingIgnoreCase(String nome);
    List<Administrador> findByAtivo(boolean ativo);
    
    boolean existsByLogin(String login);
    boolean existsByEmail(String email);
}