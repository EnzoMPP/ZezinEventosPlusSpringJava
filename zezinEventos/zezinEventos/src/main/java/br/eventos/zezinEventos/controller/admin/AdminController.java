package br.eventos.zezinEventos.controller.admin;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller administrativo principal - REFATORADO.
 * 
 * Este controller foi completamente refatorado seguindo os princípios SOLID:
 * 
 * - Dashboard foi movido para AdminDashboardController
 * - Gerenciamento de usuários foi movido para AdminUsuarioController  
 * - Gerenciamento de eventos foi movido para AdminEventoController
 * - Relatórios foi movido para AdminRelatorioController
 * - Perfil foi movido para AdminPerfilController
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
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {
    
    /**
     * Esta classe serve agora apenas como um ponto de entrada/marcação
     * para o módulo administrativo. Todas as funcionalidades foram
     * distribuídas entre controllers especializados:
     * 
     * - AdminDashboardController: Dashboard e estatísticas gerais
     * - AdminUsuarioController: Gerenciamento de clientes e organizadores
     * - AdminEventoController: Gerenciamento de eventos
     * - AdminRelatorioController: Geração e exibição de relatórios
     * - AdminPerfilController: Gerenciamento do perfil do administrador
     * 
     * Cada controller possui seu próprio service especializado e DTOs apropriados,
     * seguindo uma arquitetura limpa e orientada a objetos.
     */
}