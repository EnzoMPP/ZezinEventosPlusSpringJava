package br.eventos.zezinEventos;

import br.eventos.zezinEventos.model.Administrador;
import br.eventos.zezinEventos.model.Cliente;
import br.eventos.zezinEventos.model.Organizador;
import br.eventos.zezinEventos.model.repository.AdministradorDAO;
import br.eventos.zezinEventos.model.repository.ClienteDAO;
import br.eventos.zezinEventos.model.repository.OrganizadorDAO;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class ZezinEventosApplication {

    public static void main(String[] args) {
        SpringApplication.run(ZezinEventosApplication.class, args);
    }

}