package br.eventos.zezinEventos.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class HomeController {

    @GetMapping({"","/home"})
    public String goHome(){
        return "home";
    }
    
    @GetMapping("login")
    public String goLogin(){
        return "login";
    }



}
