package br.eventos.zezinEventos.service.shared;

import br.eventos.zezinEventos.model.ListaEspera;
import br.eventos.zezinEventos.model.Evento;
import br.eventos.zezinEventos.model.Cliente;
import br.eventos.zezinEventos.model.repository.ListaEsperaDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service para gerenciamento da lista de espera automática.
 * 
 * Implementa a lógica FIFO (First In, First Out) para garantir que
 * quando uma vaga é liberada, o próximo cliente da fila seja automaticamente
 * promovido para participante efetivo do evento.
 */
@Service
public class ListaEsperaService {
    
    private final ListaEsperaDAO listaEsperaDAO;
    
    @Autowired
    public ListaEsperaService(ListaEsperaDAO listaEsperaDAO) {
        this.listaEsperaDAO = listaEsperaDAO;
    }
      /**
     * Adiciona um cliente à lista de espera de um evento.
     * 
     * @param cliente Cliente a ser adicionado
     * @param evento Evento desejado
     * @return ListaEspera criada
     * @throws RuntimeException se cliente já está na fila
     */
    @Transactional
    public ListaEspera adicionarNaFila(Cliente cliente, Evento evento) {
        // Verificar se cliente já está na lista de espera
        Optional<ListaEspera> jaExiste = listaEsperaDAO.findByEventoAndClienteAndAtivoTrue(evento, cliente);
        if (jaExiste.isPresent()) {
            throw new RuntimeException("Você já está na lista de espera deste evento!");
        }
        
        // Calcular próxima posição na fila
        Integer proximaPosicao = calcularProximaPosicao(evento);
        
        // Criar entrada na lista de espera
        ListaEspera novaEntrada = new ListaEspera(evento, cliente, proximaPosicao);
        return listaEsperaDAO.save(novaEntrada);
    }
    
    /**
     * Remove um cliente da lista de espera.
     * 
     * @param cliente Cliente a ser removido
     * @param evento Evento da lista
     * @return true se removido com sucesso
     */
    @Transactional
    public boolean removerDaFila(Cliente cliente, Evento evento) {
        Optional<ListaEspera> entrada = listaEsperaDAO.findByEventoAndClienteAndAtivoTrue(evento, cliente);
        if (entrada.isPresent()) {
            ListaEspera item = entrada.get();
            item.setAtivo(false);
            listaEsperaDAO.save(item);
            
            // Reorganizar posições da fila
            reorganizarFila(evento);
            return true;
        }
        return false;
    }
      /**
     * Obtém o próximo cliente da fila para ser promovido.
     * NÃO realiza a inscrição - apenas retorna o cliente e remove da fila.
     * 
     * @param evento Evento que teve vaga liberada
     * @return Cliente a ser promovido ou null se fila vazia
     */
    @Transactional
    public Cliente obterProximoDaFila(Evento evento) {
        Optional<ListaEspera> proximoOpt = listaEsperaDAO.findProximoDaFila(evento);
        
        if (proximoOpt.isPresent()) {
            ListaEspera proximo = proximoOpt.get();
            
            // Marcar como processado (inativo)
            proximo.setAtivo(false);
            proximo.setNotificado(true);
            listaEsperaDAO.save(proximo);
            
            // Reorganizar posições
            reorganizarFila(evento);
            
            return proximo.getCliente();
        }
        
        return null;
    }
    
    /**
     * Promove manualmente o próximo cliente da fila (usado pelo admin).
     * Este método mantém a dependência para uso manual do admin.
     * 
     * @param evento Evento que teve vaga liberada
     * @return Cliente promovido ou null se fila vazia
     */
    @Transactional
    public Cliente promoverProximoDaFila(Evento evento) {
        return obterProximoDaFila(evento);
    }
      /**
     * Verifica se um cliente está na lista de espera de um evento.
     * 
     * @param cliente Cliente a verificar
     * @param evento Evento a verificar
     * @return true se está na fila
     */
    public boolean estaNaListaEspera(Cliente cliente, Evento evento) {
        return listaEsperaDAO.findByEventoAndClienteAndAtivoTrue(evento, cliente).isPresent();
    }
    
    /**
     * Retorna a posição de um cliente na lista de espera.
     * 
     * @param cliente Cliente a verificar
     * @param evento Evento a verificar
     * @return Posição na fila (1 = próximo) ou null se não está na fila
     */
    public Integer obterPosicaoNaFila(Cliente cliente, Evento evento) {
        return listaEsperaDAO.findPosicaoNaFila(evento, cliente).orElse(null);
    }
    
    /**
     * Conta quantas pessoas estão na lista de espera.
     * 
     * @param evento Evento a verificar
     * @return Número de pessoas na fila
     */
    public Long contarPessoasNaFila(Evento evento) {
        return listaEsperaDAO.contarPessoasNaFila(evento);
    }
    
    /**
     * Lista todas as listas de espera de um cliente.
     * 
     * @param cliente Cliente a buscar
     * @return Lista de esperas ativas
     */
    public List<ListaEspera> listarFilasDoCliente(Cliente cliente) {
        return listaEsperaDAO.findByClienteAndAtivoTrueOrderByDataEntradaDesc(cliente);
    }
    
    /**
     * Lista completa da fila de um evento (para admin/organizador).
     * 
     * @param evento Evento a verificar
     * @return Lista ordenada por posição
     */
    public List<ListaEspera> listarFilaCompleta(Evento evento) {
        return listaEsperaDAO.findByEventoAndAtivoTrueOrderByPosicaoAsc(evento);
    }
    
    // === MÉTODOS PRIVADOS ===
    
    /**
     * Calcula a próxima posição disponível na fila.
     */
    private Integer calcularProximaPosicao(Evento evento) {
        Optional<Integer> maiorPosicao = listaEsperaDAO.findMaiorPosicao(evento);
        return maiorPosicao.orElse(0) + 1;
    }
    
    /**
     * Reorganiza as posições da fila após remoção.
     */
    @Transactional
    private void reorganizarFila(Evento evento) {
        List<ListaEspera> filaAtual = listaEsperaDAO.findByEventoAndAtivoTrueOrderByPosicaoAsc(evento);
        
        for (int i = 0; i < filaAtual.size(); i++) {
            ListaEspera item = filaAtual.get(i);
            item.setPosicao(i + 1);
            listaEsperaDAO.save(item);
        }
    }
}
