package br.ufjf.dcc.sistemadefranquias.modelo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Teste unitário para a classe Cliente
 * Arrange para criar uma instância de TelaDono com parâmetros válidos
 * Act para verificar se a tela é criada corretamente
 * Assert para validar as propriedades da tela, como título, dimensões e componentes
 */
public class ClienteTest {
    
    private Cliente cliente;
    
    @BeforeEach
    void setUp() {
        cliente = new Cliente(1, "João Silva", "123.456.789-00", "joao@email.com");
    }
    
    @Test
    void testCriacaoClienteComParametrosValidos() {
        // Arrange e Act (já feito no setUp)
        
        // Assert
        assertEquals(1, cliente.getId());
        assertEquals("João Silva", cliente.getNome());
        assertEquals("123.456.789-00", cliente.getCpf());
        assertEquals("joao@email.com", cliente.getEmail());
    }
    
    @Test
    void testSettersModificamPropriedadesCorretamente() {
        // Arrange
        int novoId = 2;
        String novoNome = "Maria Santos";
        String novoCpf = "987.654.321-00";
        String novoEmail = "maria@email.com";
        
        // Act
        cliente.setId(novoId);
        cliente.setNome(novoNome);
        cliente.setCpf(novoCpf);
        cliente.setEmail(novoEmail);
        
        // Assert
        assertEquals(novoId, cliente.getId());
        assertEquals(novoNome, cliente.getNome());
        assertEquals(novoCpf, cliente.getCpf());
        assertEquals(novoEmail, cliente.getEmail());
    }
    
    @Test
    void testGettersRetornamValoresCorretos() {
        // Act e Assert
        assertEquals(1, cliente.getId());
        assertEquals("João Silva", cliente.getNome());
        assertEquals("123.456.789-00", cliente.getCpf());
        assertEquals("joao@email.com", cliente.getEmail());
    }
    
    @Test
    void testClienteComParametrosVazios() {
        // Arrange e Act
        Cliente clienteVazio = new Cliente(0, "", "", "");
        
        // Assert
        assertEquals(0, clienteVazio.getId());
        assertEquals("", clienteVazio.getNome());
        assertEquals("", clienteVazio.getCpf());
        assertEquals("", clienteVazio.getEmail());
    }
    
    @Test
    void testClienteComParametrosNull() {
        // Arrange e Act
        Cliente clienteNulo = new Cliente(0, null, null, null);
        
        // Assert
        assertEquals(0, clienteNulo.getId());
        assertNull(clienteNulo.getNome());
        assertNull(clienteNulo.getCpf());
        assertNull(clienteNulo.getEmail());
    }
}
