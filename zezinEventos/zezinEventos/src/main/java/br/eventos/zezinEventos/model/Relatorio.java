package br.eventos.zezinEventos.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "relatorios")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Relatorio {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String titulo;
    
    @Column(columnDefinition = "TEXT")
    private String descricao;
    
    @Column(name = "data_geracao")
    private LocalDateTime dataGeracao = LocalDateTime.now();
    
    @Column(name = "total_inscritos")
    private Integer totalInscritos;
    
    @Column(name = "taxa_ocupacao")
    private Double taxaOcupacao;
    
    @ManyToOne
    @JoinColumn(name = "evento_id")
    private Evento evento;
    
}