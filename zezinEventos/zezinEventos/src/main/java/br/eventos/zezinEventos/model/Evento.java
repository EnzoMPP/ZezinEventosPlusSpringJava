package br.eventos.zezinEventos.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;
import java.util.List;
import java.time.LocalDateTime;

@Entity
@Table(name = "eventos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Evento {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 200)
    private String nome;
    
    @Column(columnDefinition = "TEXT")
    private String descricao;
    
    @Column(name = "preco_ingresso")
    private double precoIngresso;
    
    @Column(name = "data_evento", nullable = false)
    private LocalDateTime dataEvento;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoEvento tipo;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusEvento status = StatusEvento.ABERTO;
    
    @Column(nullable = false, length = 200)
    private String local;
    
    @Column(name = "vagas_totais", nullable = false)
    private Integer vagasTotais;
    
    @Column(name = "vagas_ocupadas")
    private Integer vagasOcupadas = 0;
      @Column(nullable = false)
    private Boolean ativo = true;
    
    @Column(columnDefinition = "TEXT")
    private String palestrantes;
    
    @CreationTimestamp
    @Column(name = "data_criacao")
    private LocalDateTime dataCriacao;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organizador_id", nullable = false)
    private Organizador organizador;
    
    @OneToMany(mappedBy = "evento", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Ingresso> ingressos;
    
    @OneToMany(mappedBy = "evento", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Inscricao> inscricoes;

     public Integer getVagasDisponiveis() {
        return vagasTotais - vagasOcupadas;
    }
    
    public Boolean temVagasDisponiveis() {
        return getVagasDisponiveis() > 0;
    }

}
