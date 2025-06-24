package br.eventos.zezinEventos.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller do cliente principal - REFATORADO.
 * 
 * Este controller foi completamente refatorado seguindo os princípios SOLID:
 * 
 * - Dashboard foi movido para ClienteDashboardController
 * - Gerenciamento de perfil foi movido para ClientePerfilController
 * - Visualização de eventos foi movido para ClienteEventoController
 * - Gerenciamento de inscrições foi movido para ClienteInscricaoController
 * 
 * Cada controller agora segue o Princípio da Responsabilidade Única (SRP),
 * tendo uma única razão para mudar e focando em uma funcionalidade específica.
 * 
 * Esta refatoração elimina o "God Class" antipattern e promove:
 * - Melhor testabilidade (classes menores e focadas)
 * - Maior manutenibilidade (mudanças isoladas)
 * - Melhor organização do código (responsabilidades claras)
 * - Facilita extensibilidade (novos recursos em controllers específicos)
 */
@Controller
@RequestMapping("/cliente")
@PreAuthorize("hasRole('CLIENTE')")
public class ClienteController {
    
    /**
     * Esta classe serve agora apenas como um ponto de entrada/marcação
     * para o módulo do cliente. Todas as funcionalidades foram
     * distribuídas entre controllers especializados:
     * 
     * - ClienteDashboardController: Dashboard e estatísticas do cliente
     * - ClientePerfilController: Gerenciamento do perfil do cliente
     * - ClienteEventoController: Visualização de eventos disponíveis
     * - ClienteInscricaoController: Gerenciamento de inscrições
     * 
     * Cada controller possui seu próprio service especializado e DTOs apropriados,
     * seguindo uma arquitetura limpa e orientada a objetos.
     */
}