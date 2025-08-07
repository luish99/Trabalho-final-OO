package br.ufjf.dcc.sistemadefranquias.visao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import br.ufjf.dcc.sistemadefranquias.controle.Sistema;
import br.ufjf.dcc.sistemadefranquias.modelo.Dono;

/**
 * Teste unitário para a classe TelaLogin
 * Arrange para criar uma instância de TelaDono com parâmetros válidos
 * Act para verificar se a tela é criada corretamente
 * Assert para validar as propriedades da tela, como título, dimensões e componentes
 */
public class TelaLoginTest {
    
    private Sistema sistema;
    private TelaLogin telaLogin;
    
    @Test
    void testCriacaoTelaLoginComSistemaValido() {
        // Arrange
        sistema = new Sistema();
        
        // Act
        telaLogin = new TelaLogin(sistema);
        
        // Assert
        assertNotNull(telaLogin);
        assertEquals("Login - Sistema de Franquias", telaLogin.getTitle());
        assertFalse(sistema.getUsuarios().isEmpty()); // Deve ter criado um dono padrão
    }
    
    @Test
    void testSistemaCriaDonoAutomaticamente() {
        // Arrange
        sistema = new Sistema();
        assertTrue(sistema.getUsuarios().isEmpty());
        
        // Act
        telaLogin = new TelaLogin(sistema);
        
        // Assert
        assertFalse(sistema.getUsuarios().isEmpty());
        assertEquals(1, sistema.getUsuarios().size());
        assertTrue(sistema.getUsuarios().containsKey("admin"));
        assertTrue(sistema.getUsuarios().get("admin") instanceof Dono);
        
        Dono dono = (Dono) sistema.getUsuarios().get("admin");
        assertEquals("Dono Padrão", dono.getNome());
        assertEquals("admin", dono.getCpf());
        assertEquals("admin@franquia.com", dono.getEmail());
    }
    
    @Test
    void testNaoCriaDonoSeJaExisteUsuarios() throws Exception {
        // Arrange
        sistema = new Sistema();
        sistema.adicionarDono("Dono Existente", "123.456.789-00", "dono@test.com", "senha123");
        int usuariosAntes = sistema.getUsuarios().size();
        
        // Act
        telaLogin = new TelaLogin(sistema);
        
        // Assert
        assertEquals(usuariosAntes, sistema.getUsuarios().size()); // Não deve criar outro usuário
    }
    
    @Test
    void testTelaLoginTemCamposNecessarios() {
        // Arrange
        sistema = new Sistema();
        
        // Act
        telaLogin = new TelaLogin(sistema);
        
        // Assert
        assertNotNull(telaLogin.getContentPane());
        assertEquals("Login - Sistema de Franquias", telaLogin.getTitle());
    }
    
    @Test
    void testConfiguracaoInicialDaTela() {
        // Arrange
        sistema = new Sistema();
        
        // Act
        telaLogin = new TelaLogin(sistema);
        
        // Assert
        assertEquals("Login - Sistema de Franquias", telaLogin.getTitle());
        assertEquals(javax.swing.JFrame.EXIT_ON_CLOSE, telaLogin.getDefaultCloseOperation());
        assertTrue(telaLogin.isVisible());
    }
    
    @Test
    void testTelaLoginComSistemaNull() {
        // Este teste verifica se a tela lida adequadamente com sistema que nao foi inicializado
        
        // Arrange e Act
        try {
            telaLogin = new TelaLogin(null);
            assertNotNull(telaLogin);
        } catch (Exception e) {
            assertTrue(e instanceof NullPointerException || 
                      e instanceof IllegalArgumentException);
        }
    }
}
