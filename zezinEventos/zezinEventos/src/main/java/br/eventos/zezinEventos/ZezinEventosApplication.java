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

    @Bean
    public CommandLineRunner criarUsuariosIniciais(
            AdministradorDAO adminDAO,
            ClienteDAO clienteDAO,
            OrganizadorDAO organizadorDAO,
            PasswordEncoder passwordEncoder) {
        return args -> {

            // Criar admin inicial se não existir
            if (adminDAO.count() == 0) {
                Administrador admin = new Administrador();
                admin.setNome("Administrador");
                admin.setEmail("admin@sigei.com");
                admin.setLogin("admin");
                admin.setSenha(passwordEncoder.encode("admin123"));
                admin.setAtivo(true);
                admin.setTelefone("(11) 99999-9999");
                admin.setCargo("Administrador Geral");

                adminDAO.save(admin);
                System.out.println("Admin criado - Login: admin | Senha: admin123");
            }

            // Criar cliente teste se não existir
            if (clienteDAO.count() == 0) {
                Cliente cliente = new Cliente();
                cliente.setNome("Cliente Teste");
                cliente.setEmail("cliente@teste.com");
                cliente.setLogin("cliente");
                cliente.setSenha(passwordEncoder.encode("123456"));
                cliente.setAtivo(true);
                cliente.setCpf("123.456.789-00");
                cliente.setTelefone("(11) 88888-8888");

                clienteDAO.save(cliente);
                System.out.println("Cliente criado - Login: cliente | Senha: 123456");
            }
            
            // Criar organizador teste se não existir
            if (!organizadorDAO.existsByLogin("organizador")) {
                Organizador organizador = new Organizador();
                organizador.setNome("João Silva");
                organizador.setEmail("organizador@empresa.com");
                organizador.setLogin("organizador");
                organizador.setSenha(passwordEncoder.encode("org123"));
                organizador.setAtivo(true);
                organizador.setTelefone("(11) 95555-5555");
                organizador.setCnpj("12.345.678/0001-90");
                organizador.setEmpresa("Eventos Silva LTDA");  // ← CAMPO OBRIGATÓRIO
                
                organizadorDAO.save(organizador);
                System.out.println("Organizador criado - Login: organizador | Senha: org123");
            }
        };
    }
}