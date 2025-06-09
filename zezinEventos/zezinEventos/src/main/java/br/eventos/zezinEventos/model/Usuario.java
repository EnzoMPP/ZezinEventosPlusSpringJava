package br.eventos.zezinEventos.model;


import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public abstract class Usuario {

    private int id;
    private String nome;
    private String email;
    private String login;
    private String senha;
}
