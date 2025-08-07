package br.ufjf.dcc.sistemadefranquias.visao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import br.ufjf.dcc.sistemadefranquias.controle.Sistema;
import br.ufjf.dcc.sistemadefranquias.modelo.Endereco;
import br.ufjf.dcc.sistemadefranquias.modelo.Franquia;
import br.ufjf.dcc.sistemadefranquias.modelo.Vendedor;

/**
 * Teste unitário para a classe TelaVendedor
 * Arrange para criar uma instância de TelaDono com parâmetros válidos
 * Act para verificar se a tela é criada corretamente
 * Assert para validar as propriedades da tela, como título, dimensões e componentes
 */
public class TelaVendedorTest {
    
    private Sistema sistema;
    private Vendedor vendedor;
    private Franquia franquia;
    private TelaVendedor telaVendedor;
    
    @Test
    void testCriacaoTelaVendedorComParametrosValidos() {
        // Arrange
        sistema = new Sistema();
        Endereco endereco = new Endereco("Rua A", "123", "Apt 1", "Centro", "Cidade", "Estado", "12345-678");
        franquia = new Franquia(1, "Franquia Teste", endereco);
        vendedor = new Vendedor("João Vendedor", "123.456.789-00", "joao@email.com", "senha123", franquia);
        
        // Act
        telaVendedor = new TelaVendedor(sistema, vendedor);
        
        // Assert
        assertNotNull(telaVendedor);
        assertEquals("Painel do Vendedor: João Vendedor | Franquia: Franquia Teste", telaVendedor.getTitle());
        assertEquals(400, telaVendedor.getWidth());
        assertEquals(400, telaVendedor.getHeight());
    }
    
    @Test
    void testConfiguracaoInicialDaTela() {
        // Arrange
        sistema = new Sistema();
        Endereco endereco = new Endereco("Rua B", "456", "", "Bairro", "Outra Cidade", "MG", "54321-876");
        franquia = new Franquia(2, "Segunda Franquia", endereco);
        vendedor = new Vendedor("Maria Vendedora", "987.654.321-00", "maria@email.com", "senha456", franquia);
        
        // Act
        telaVendedor = new TelaVendedor(sistema, vendedor);
        
        // Assert
        assertEquals("Painel do Vendedor: Maria Vendedora | Franquia: Segunda Franquia", telaVendedor.getTitle());
        assertEquals(javax.swing.JFrame.EXIT_ON_CLOSE, telaVendedor.getDefaultCloseOperation());
        assertNotNull(telaVendedor.getContentPane());
    }
    
    @Test
    void testTelaVendedorComNomesVazios() {
        // Arrange
        sistema = new Sistema();
        Endereco endereco = new Endereco("", "", "", "", "", "", "");
        franquia = new Franquia(3, "", endereco);
        vendedor = new Vendedor("", "111.222.333-44", "test@email.com", "senha", franquia);
        
        // Act
        telaVendedor = new TelaVendedor(sistema, vendedor);
        
        // Assert
        assertEquals("Painel do Vendedor:  | Franquia: ", telaVendedor.getTitle());
        assertNotNull(telaVendedor);
    }
    
    @Test
    void testTelaVendedorComNomesNull() {
        // Arrange
        sistema = new Sistema();
        Endereco endereco = new Endereco("Rua", "1", "", "Bairro", "Cidade", "Estado", "12345-678");
        franquia = new Franquia(4, null, endereco);
        vendedor = new Vendedor(null, "111.222.333-44", "test@email.com", "senha", franquia);
        
        // Act
        telaVendedor = new TelaVendedor(sistema, vendedor);
        
        // Assert
        assertEquals("Painel do Vendedor: null | Franquia: null", telaVendedor.getTitle());
        assertNotNull(telaVendedor);
    }
    
    @Test
    void testDimensoesDaTela() {
        // Arrange
        sistema = new Sistema();
        Endereco endereco = new Endereco("Rua Principal", "100", "Loja", "Centro", "Capital", "SP", "01000-000");
        franquia = new Franquia(5, "Franquia Principal", endereco);
        vendedor = new Vendedor("Vendedor Principal", "555.666.777-88", "vendedor@email.com", "senha", franquia);
        
        // Act
        telaVendedor = new TelaVendedor(sistema, vendedor);
        
        // Assert
        assertEquals(400, telaVendedor.getWidth());
        assertEquals(400, telaVendedor.getHeight());
    }
    
    @Test
    void testTelaVendedorComSistemaNull() {
        // Arrange
        Endereco endereco = new Endereco("Rua", "1", "", "Bairro", "Cidade", "Estado", "12345-678");
        franquia = new Franquia(6, "Teste Franquia", endereco);
        vendedor = new Vendedor("Test Vendedor", "123.456.789-00", "test@email.com", "senha", franquia);
        
        // Act e Assert
        try {
            telaVendedor = new TelaVendedor(null, vendedor);
            assertNotNull(telaVendedor);
        } catch (Exception e) {
            assertTrue(e instanceof NullPointerException || 
                      e instanceof IllegalArgumentException);
        }
    }
    
    @Test
    void testTelaVendedorComVendedorNull() {
        // Arrange
        sistema = new Sistema();
        
        // Act e Assert
        try {
            telaVendedor = new TelaVendedor(sistema, null);
            assertNotNull(telaVendedor);
        } catch (Exception e) {
            assertTrue(e instanceof NullPointerException || 
                      e instanceof IllegalArgumentException);
        }
    }
    
    @Test
    void testTelaVendedorComFranquiaNull() {
        // Arrange
        sistema = new Sistema();
        vendedor = new Vendedor("Vendedor Sem Franquia", "999.888.777-66", "sem@franquia.com", "senha", null);
        
        // Act e Assert
        try {
            telaVendedor = new TelaVendedor(sistema, vendedor);
            assertNotNull(telaVendedor);
        } catch (Exception e) {
            assertTrue(e instanceof NullPointerException || 
                      e instanceof IllegalArgumentException);
        }
    }
    
    @Test
    void testTelaVendedorTemComponentesBasicos() {
        // Arrange
        sistema = new Sistema();
        Endereco endereco = new Endereco("Rua Componentes", "999", "", "Teste", "Cidade Teste", "RJ", "99999-999");
        franquia = new Franquia(7, "Franquia Componentes", endereco);
        vendedor = new Vendedor("Teste Componentes", "111.111.111-11", "comp@test.com", "123", franquia);
        
        // Act
        telaVendedor = new TelaVendedor(sistema, vendedor);
        
        // Assert
        assertNotNull(telaVendedor.getContentPane());
        assertTrue(telaVendedor.getContentPane().getComponentCount() > 0);
    }
}
