package br.eventos.zezinEventos.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "inscricoes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Inscricao {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "data_inscricao")
    private LocalDateTime dataInscricao = LocalDateTime.now();
    
    @Column(name = "presente")
    private Boolean presente = false;
    
    @Column(name = "data_confirmacao_presenca")
    private LocalDateTime dataConfirmacaoPresenca;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "evento_id", nullable = false)
    private Evento evento;
    
    @OneToOne(mappedBy = "inscricao", cascade = CascadeType.ALL)
    private Ingresso ingresso;

    public void confirmarPresenca() {
        this.presente = true;
        this.dataConfirmacaoPresenca = LocalDateTime.now();
    }
}