package br.eventos.zezinEventos.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "clientes")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class Cliente extends Usuario{

    @Column(unique = true, length = 14)
    private String cpf;

    @Column(name = "data_nascimento")
    private LocalDate dataNasc;

    @Column(length = 15)
    private String telefone;


    

}
