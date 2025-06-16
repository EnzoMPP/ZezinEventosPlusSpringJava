package br.eventos.zezinEventos.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "ingressos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Ingresso {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "data_emissao")
    private LocalDateTime dataEmissao = LocalDateTime.now();
    
    @Column(name = "utilizado")
    private Boolean utilizado = false;
    
    @OneToOne
    @JoinColumn(name = "inscricao_id")
    private Inscricao inscricao;
    
    @ManyToOne
    @JoinColumn(name = "evento_id")
    private Evento evento;
}