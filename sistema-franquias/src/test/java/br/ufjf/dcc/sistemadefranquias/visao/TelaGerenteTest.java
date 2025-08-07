package br.ufjf.dcc.sistemadefranquias.visao;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import br.ufjf.dcc.sistemadefranquias.controle.Sistema;
import br.ufjf.dcc.sistemadefranquias.modelo.Franquia;
import br.ufjf.dcc.sistemadefranquias.modelo.Gerente;
import br.ufjf.dcc.sistemadefranquias.modelo.Vendedor;

/**
 * Teste unitário para a classe TelaGerente (visão)
 * Testa principalmente a inicialização e configuração da interface
 * Arrange para criar uma instância de TelaDono com parâmetros válidos
 * Act para verificar se a tela é criada corretamente
 * Assert para validar as propriedades da tela, como título, dimensões e componentes
 * 
 * Mocks são usados para evitar dependências externas e focar na lógica da tela criando
 *  um falso objeto para que nao haja necessidade de inicializar o sistema completo
 *  e assim evitar exceções inesperadas durante os testes.
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class TelaGerenteTest {
    
    @Mock
    private Sistema sistema;
    
    @Mock
    private Gerente gerente;
    
    @Mock
    private Franquia franquia;
    
    private TelaGerente telaGerente;
    
    @BeforeEach
    void setUp() {
        // Setup dos mocks
        when(gerente.getNome()).thenReturn("Gerente Teste");
        when(gerente.getFranquiaGerenciada()).thenReturn(franquia);
        when(franquia.getNome()).thenReturn("Franquia Teste");
        
        // Mock para vendedores vazios
        Map<String, Vendedor> vendedoresVazios = new HashMap<>();
        when(franquia.getVendedores()).thenReturn(vendedoresVazios);
        
        // Evita exceções durante a criação da tela
        when(franquia.getEstoque()).thenReturn(new HashMap<>());
        when(franquia.getPedidos()).thenReturn(new HashMap<>());
        when(franquia.getSolicitacoesPendentes()).thenReturn(new HashMap<>());
    }
    
    @Test
    void testCriacaoTelaGerenteComSucesso() {
        // Act e Assert - não deve lançar exceção
        assertDoesNotThrow(() -> {
            telaGerente = new TelaGerente(sistema, gerente);
        });
        
        // Verifica se a tela foi criada
        assertNotNull(telaGerente);
    }
    
    @Test
    void testTituloTelaGerenteConfiguradoCorretamente() {
        // Arrange e Act
        telaGerente = new TelaGerente(sistema, gerente);
        
        // Assert
        String tituloEsperado = "Painel do Gerente: Gerente Teste | Franquia: Franquia Teste";
        assertEquals(tituloEsperado, telaGerente.getTitle());
    }
    
    @Test
    void testTelaGerenteTemTamanhoCorreto() {
        // Arrange e Act
        telaGerente = new TelaGerente(sistema, gerente);
        
        // Assert
        assertEquals(500, telaGerente.getWidth());
        assertEquals(450, telaGerente.getHeight());
    }
    
    @Test
    void testTelaGerenteComportamentoFecharJanela() {
        // Arrange e Act
        telaGerente = new TelaGerente(sistema, gerente);
        
        // Assert
        assertEquals(javax.swing.JFrame.EXIT_ON_CLOSE, telaGerente.getDefaultCloseOperation());
    }
    
    @Test
    void testTelaGerenteEstaVisivel() {
        // Arrange e Act
        telaGerente = new TelaGerente(sistema, gerente);
        
        // Assert
        assertTrue(telaGerente.isVisible());
    }
}
