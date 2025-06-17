package br.eventos.zezinEventos;

import br.eventos.zezinEventos.model.Administrador;
import br.eventos.zezinEventos.model.Cliente;
import br.eventos.zezinEventos.model.repository.AdministradorDAO;
import br.eventos.zezinEventos.model.repository.ClienteDAO;
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
                admin.setCargo("Administrador Geral");
                admin.setTelefone("(11) 99999-9999");
                
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
        };
    }
}