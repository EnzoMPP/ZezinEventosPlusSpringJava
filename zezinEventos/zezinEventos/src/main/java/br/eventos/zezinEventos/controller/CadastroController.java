package br.eventos.zezinEventos.controller;

import br.eventos.zezinEventos.model.Cliente;
import br.eventos.zezinEventos.model.Organizador;
import br.eventos.zezinEventos.service.ClienteService;
import br.eventos.zezinEventos.service.OrganizadorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;

@Controller
public class CadastroController {

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private OrganizadorService organizadorService;

    @GetMapping("/cadastro")
    public String goCadastro(){
        return "cadastro";
    }

    @PostMapping("/cadastrar")
    public String processCadastro(@RequestParam String tipoUsuario,
                                 @RequestParam String nome,
                                 @RequestParam String email,
                                 @RequestParam(required = false) String telefone,
                                 @RequestParam String login,
                                 @RequestParam String senha,
                                 @RequestParam String confirmSenha,
                                 @RequestParam(required = false) String cpf,
                                 @RequestParam(required = false) String dataNasc,
                                 @RequestParam(required = false) String cnpj,
                                 @RequestParam(required = false) String empresa,
                                 RedirectAttributes redirectAttributes) {
        
        try {
            // Validações básicas
            if (!senha.equals(confirmSenha)) {
                redirectAttributes.addFlashAttribute("error", "As senhas não coincidem!");
                return "redirect:/cadastro";
            }
            
            if ("CLIENTE".equals(tipoUsuario)) {
                cadastrarCliente(nome, email, telefone, login, senha, cpf, dataNasc);
                redirectAttributes.addFlashAttribute("success", "Cliente cadastrado! Login: " + login);
            } 
            else if ("ORGANIZADOR".equals(tipoUsuario)) {
                cadastrarOrganizador(nome, email, telefone, login, senha, cnpj, empresa);
                redirectAttributes.addFlashAttribute("success", "Organizador cadastrado! Login: " + login);
            }
            
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erro: " + e.getMessage());
        }
        
        return "redirect:/cadastro";
    }
    
    private void cadastrarCliente(String nome, String email, String telefone, 
                                 String login, String senha, String cpf, String dataNasc) {
        
        Cliente cliente = new Cliente();
        cliente.setNome(nome.trim());
        cliente.setEmail(email.trim().toLowerCase());
        cliente.setTelefone(telefone != null ? telefone.trim() : "");
        cliente.setLogin(login.trim());
        cliente.setSenha(senha); 
        cliente.setAtivo(true);
        
        if (cpf != null && !cpf.trim().isEmpty()) {
            cliente.setCpf(cpf.trim());
        }
        
        if (dataNasc != null && !dataNasc.trim().isEmpty()) {
            try {
                cliente.setDataNasc(LocalDate.parse(dataNasc));
            } catch (Exception e) {
               
            }
        }
        
        clienteService.salvar(cliente); 
    }
    
    private void cadastrarOrganizador(String nome, String email, String telefone, 
                                    String login, String senha, String cnpj, String empresa) {
        
        Organizador organizador = new Organizador();
        organizador.setNome(nome.trim());
        organizador.setEmail(email.trim().toLowerCase());
        organizador.setTelefone(telefone != null ? telefone.trim() : "");
        organizador.setLogin(login.trim());
        organizador.setSenha(senha); 
        organizador.setAtivo(true);
        organizador.setEmpresa(empresa != null ? empresa.trim() : nome.trim());
        
        if (cnpj != null && !cnpj.trim().isEmpty()) {
            organizador.setCnpj(cnpj.trim());
        }
        
        organizadorService.salvar(organizador); 
    }
}