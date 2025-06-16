package br.eventos.zezinEventos.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;

@Entity
@Table(name = "administradores")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class Administrador extends Usuario {
    
    @Column(length = 15)
    private String telefone;

     @Column(length = 100)
    private String cargo;

}
