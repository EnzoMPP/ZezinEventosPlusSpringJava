package br.eventos.zezinEventos.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

/**
 * Entidade que representa um item da lista de espera.
 * Mantém a ordem FIFO (First In, First Out) para gerenciar automaticamente
 * a atribuição de vagas quando algum participante cancela sua inscrição.
 */
@Entity
@Table(name = "lista_espera")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ListaEspera {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "evento_id", nullable = false)
    private Evento evento;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;
    
    @Column(name = "data_entrada", nullable = false)
    private LocalDateTime dataEntrada;
    
    @Column(name = "posicao", nullable = false)
    private Integer posicao;
    
    @Column(name = "ativo", nullable = false)
    private Boolean ativo = true;
    
    @Column(name = "notificado")
    private Boolean notificado = false;
    
    // Construtor personalizado
    public ListaEspera(Evento evento, Cliente cliente, Integer posicao) {
        this.evento = evento;
        this.cliente = cliente;
        this.posicao = posicao;
        this.dataEntrada = LocalDateTime.now();
        this.ativo = true;
        this.notificado = false;
    }
}
