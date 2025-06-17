package br.eventos.zezinEventos.model.repository;

import br.eventos.zezinEventos.model.Administrador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdministradorDAO extends JpaRepository<Administrador, Long> {
    Administrador findByLogin(String login);
    
    boolean existsByLogin(String login);
    boolean existsByEmail(String email);
}