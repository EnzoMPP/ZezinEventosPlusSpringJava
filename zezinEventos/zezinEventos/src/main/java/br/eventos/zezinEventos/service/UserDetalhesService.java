package br.eventos.zezinEventos.config.security.service;

import br.eventos.zezinEventos.model.Administrador;
import br.eventos.zezinEventos.model.Cliente;
import br.eventos.zezinEventos.model.Organizador;
import br.eventos.zezinEventos.model.repository.AdministradorDAO;
import br.eventos.zezinEventos.model.repository.ClienteDAO;
import br.eventos.zezinEventos.model.repository.OrganizadorDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserDetalhesService implements UserDetailsService {

    @Autowired
    private ClienteDAO clienteDAO;
    
    @Autowired
    private OrganizadorDAO organizadorDAO;
    
    @Autowired
    private AdministradorDAO administradorDAO;
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("=== CARREGANDO USUARIO: " + username + " ===");
        
        // Tenta encontrar como administrador
        Administrador admin = administradorDAO.findByLogin(username);
        if (admin != null && admin.getAtivo()) {
            System.out.println("Usuario encontrado como ADMIN: " + admin.getNome());
            return buildUserDetails(admin.getLogin(), admin.getSenha(), "ROLE_ADMIN");
        }
        
        // Tenta encontrar como organizador
        Organizador organizador = organizadorDAO.findByLogin(username);
        if (organizador != null && organizador.getAtivo()) {
            System.out.println("Usuario encontrado como ORGANIZADOR: " + organizador.getNome());
            return buildUserDetails(organizador.getLogin(), organizador.getSenha(), "ROLE_ORGANIZADOR");
        }
        
        // Tenta encontrar como cliente
        Cliente cliente = clienteDAO.findByLogin(username);
        if (cliente != null && cliente.getAtivo()) {
            System.out.println("Usuario encontrado como CLIENTE: " + cliente.getNome());
            return buildUserDetails(cliente.getLogin(), cliente.getSenha(), "ROLE_CLIENTE");
        }
        
        System.out.println("USUARIO NAO ENCONTRADO: " + username);
        throw new UsernameNotFoundException("Usuário não encontrado: " + username);
    }
    
    private UserDetails buildUserDetails(String username, String password, String role) {
        System.out.println("Criando UserDetails com role: " + role);
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(role));
        
        return new User(username, password, authorities);
    }
}