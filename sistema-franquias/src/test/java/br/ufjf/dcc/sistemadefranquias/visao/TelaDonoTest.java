package br.ufjf.dcc.sistemadefranquias.visao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import br.ufjf.dcc.sistemadefranquias.controle.Sistema;
import br.ufjf.dcc.sistemadefranquias.modelo.Dono;

/**
 * Teste unitário para a classe TelaDono
 * Arrange para criar uma instância de TelaDono com parâmetros válidos
 * Act para verificar se a tela é criada corretamente
 * Assert para validar as propriedades da tela, como título, dimensões e componentes
 */
public class TelaDonoTest {
    
    private Sistema sistema;
    private Dono dono;
    private TelaDono telaDono;
    
    @Test
    void testCriacaoTelaDonoComParametrosValidos() {
        // Arrange
        sistema = new Sistema();
        dono = new Dono("João Silva", "123.456.789-00", "joao@email.com", "senha123");
        
        // Act
        telaDono = new TelaDono(sistema, dono);
        
        // Assert
        assertNotNull(telaDono);
        assertEquals("Painel do Dono: João Silva", telaDono.getTitle());
        assertEquals(650, telaDono.getWidth());
        assertEquals(500, telaDono.getHeight());
    }
    
    @Test
    void testConfiguracaoInicialDaTela() {
        // Arrange
        sistema = new Sistema();
        dono = new Dono("Maria Santos", "987.654.321-00", "maria@email.com", "senha456");
        
        // Act
        telaDono = new TelaDono(sistema, dono);
        
        // Assert
        assertEquals("Painel do Dono: Maria Santos", telaDono.getTitle());
        assertEquals(javax.swing.JFrame.EXIT_ON_CLOSE, telaDono.getDefaultCloseOperation());
        assertNotNull(telaDono.getContentPane());
    }
    
    @Test
    void testTelaDonoComNomeDonoVazio() {
        // Arrange
        sistema = new Sistema();
        dono = new Dono("", "111.222.333-44", "test@email.com", "senha");
        
        // Act
        telaDono = new TelaDono(sistema, dono);
        
        // Assert
        assertEquals("Painel do Dono: ", telaDono.getTitle());
        assertNotNull(telaDono);
    }
    
    @Test
    void testTelaDonoComNomeDonoNull() {
        // Arrange
        sistema = new Sistema();
        dono = new Dono(null, "111.222.333-44", "test@email.com", "senha");
        
        // Act
        telaDono = new TelaDono(sistema, dono);
        
        // Assert
        assertEquals("Painel do Dono: null", telaDono.getTitle());
        assertNotNull(telaDono);
    }
    
    @Test
    void testDimensoesDaTela() {
        // Arrange
        sistema = new Sistema();
        dono = new Dono("Admin", "admin", "admin@test.com", "admin");
        
        // Act
        telaDono = new TelaDono(sistema, dono);
        
        // Assert
        assertEquals(650, telaDono.getWidth());
        assertEquals(500, telaDono.getHeight());
    }
    
    @Test
    void testTelaDonoComSistemaNull() {
        // Arrange
        dono = new Dono("Test Dono", "123.456.789-00", "test@email.com", "senha");
        
        // Act e Assert
        try {
            telaDono = new TelaDono(null, dono);
            // Se chegou até aqui, pelo menos não houve exceção na criação
            assertNotNull(telaDono);
        } catch (Exception e) {
            // É aceitável que lance exceção com sistema que nao foi inicializado
            assertTrue(e instanceof NullPointerException || 
                      e instanceof IllegalArgumentException);
        }
    }
    
    @Test
    void testTelaDonoComDonoNull() {
        // Arrange
        sistema = new Sistema();
        
        // Act e Assert
        try {
            telaDono = new TelaDono(sistema, null);
            assertNotNull(telaDono);
        } catch (Exception e) {
            assertTrue(e instanceof NullPointerException || 
                      e instanceof IllegalArgumentException);
        }
    }
    
    @Test
    void testTelaDonoTemComponentesBasicos() {
        // Arrange
        sistema = new Sistema();
        dono = new Dono("Teste", "123", "test@test.com", "123");
        
        // Act
        telaDono = new TelaDono(sistema, dono);
        
        // Assert
        assertNotNull(telaDono.getContentPane());
        assertTrue(telaDono.getContentPane().getComponentCount() > 0);
    }
}
