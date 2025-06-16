package br.eventos.zezinEventos;

import br.eventos.zezinEventos.model.Cliente;
import br.eventos.zezinEventos.model.repository.ClienteDAO;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
//import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class ZezinEventosApplication {

	public static void main(String[] args) {
		SpringApplication.run(ZezinEventosApplication.class, args);
	}
/*
    @Bean
    public CommandLineRunner c
	riarUsuarioInicial(ClienteDAO clienteDAO, PasswordEncoder passwordEncoder) {
        return args -> {
            if (clienteDAO.count() == 0) {
                Cliente cliente = new Cliente();
                cliente.setNome("Usuário Teste");
                cliente.setEmail("teste@example.com");
                cliente.setLogin("teste");
                cliente.setSenha(passwordEncoder.encode("123456"));
                cliente.setCpf("123.456.789-00");
                cliente.setAtivo(true);
                
                clienteDAO.save(cliente);
                
                System.out.println("Usuário teste criado com sucesso!");
            }
        };
	}
	*/
}
