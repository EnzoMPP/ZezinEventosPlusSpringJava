package br.eventos.zezinEventos.model.repository;

import br.eventos.zezinEventos.model.Organizador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrganizadorDAO extends JpaRepository<Organizador, Long> {
    Organizador findByLogin(String login);
    boolean existsByLogin(String login);
    boolean existsByEmail(String email);
    boolean existsByCnpj(String cnpj);
}