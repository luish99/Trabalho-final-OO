package br.ufjf.dcc.sistemadefranquias.controle;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import br.ufjf.dcc.sistemadefranquias.excecoes.ValidacaoException;
import br.ufjf.dcc.sistemadefranquias.modelo.Endereco;
import br.ufjf.dcc.sistemadefranquias.modelo.Franquia;
import br.ufjf.dcc.sistemadefranquias.modelo.Gerente;
import br.ufjf.dcc.sistemadefranquias.modelo.Usuario;

/**
 * Teste unitário para a classe Sistema (controle)
 *  Arrange para criar uma instância de TelaDono com parâmetros válidos
 * Act para verificar se a tela é criada corretamente
 * Assert para validar as propriedades da tela, como título, dimensões e componentes
 */
public class SistemaTest {
    
    private Sistema sistema;
    private Endereco endereco;
    
    @BeforeEach
    void setUp() {
        sistema = new Sistema();
        endereco = new Endereco("Rua A", "123", "Apto 1", "Centro", "Juiz de Fora", "MG", "36000-000");
    }
    
    @Test
    void testAdicionarGerenteComSucesso() throws ValidacaoException {
        // Arrange
        String nome = "Carlos Gerente";
        String cpf = "111.222.333-44";
        String email = "carlos@empresa.com";
        String senha = "senha123";
        Franquia franquia = new Franquia(1, "Franquia Teste", endereco);
        
        // Act
        sistema.adicionarGerente(nome, cpf, email, senha, franquia);
        
        // Assert
        assertTrue(sistema.getUsuarios().containsKey(cpf));
        Usuario usuario = sistema.getUsuarios().get(cpf);
        assertTrue(usuario instanceof Gerente);
        assertEquals(nome, usuario.getNome());
        assertEquals(cpf, usuario.getCpf());
        assertEquals(email, usuario.getEmail());
        assertEquals(franquia, ((Gerente) usuario).getFranquiaGerenciada());
    }
    
    @Test
    void testAdicionarGerenteComCpfDuplicado() throws ValidacaoException {
        // Arrange
        String cpf = "111.222.333-44";
        Franquia franquia1 = new Franquia(1, "Franquia 1", endereco);
        Franquia franquia2 = new Franquia(2, "Franquia 2", endereco);
        sistema.adicionarGerente("Primeiro Gerente", cpf, "primeiro@email.com", "senha1", franquia1);
        
        // Act e Assert
        ValidacaoException exception = assertThrows(ValidacaoException.class, () -> {
            sistema.adicionarGerente("Segundo Gerente", cpf, "segundo@email.com", "senha2", franquia2);
        });
        
        assertEquals("CPF já cadastrado.", exception.getMessage());
    }
    
    @Test
    void testAdicionarFranquiaComSucesso() throws ValidacaoException {
        // Arrange
        String nomeFranquia = "Franquia Teste";
        Gerente gerente = new Gerente("Gerente Teste", "123.456.789-00", "gerente@teste.com", "senha", null);
        
        // Act
        sistema.adicionarFranquia(nomeFranquia, endereco, gerente);
        
        // Assert
        assertFalse(sistema.getFranquias().isEmpty());
        Franquia franquia = sistema.getFranquias().values().iterator().next();
        assertEquals(nomeFranquia, franquia.getNome());
        assertEquals(endereco, franquia.getEndereco());
        assertEquals(gerente, franquia.getGerente());
    }
    
    @Test
    void testAdicionarFranquiaComNomeVazio() {
        // Arrange
        String nomeFranquia = "";
        Gerente gerente = new Gerente("Gerente", "123.456.789-00", "gerente@teste.com", "senha", null);
        
        // Act e Assert
        ValidacaoException exception = assertThrows(ValidacaoException.class, () -> {
            sistema.adicionarFranquia(nomeFranquia, endereco, gerente);
        });
        
        assertEquals("O nome da franquia é obrigatório.", exception.getMessage());
    }
    
    @Test
    void testAutenticarUsuarioComSucesso() throws ValidacaoException {
        // Arrange
        String cpf = "111.222.333-44";
        String senha = "senha123";
        Franquia franquia = new Franquia(1, "Franquia Teste", endereco);
        sistema.adicionarGerente("Gerente Teste", cpf, "gerente@teste.com", senha, franquia);
        
        // Act
        Usuario usuarioAutenticado = sistema.autenticar(cpf, senha);
        
        // Assert
        assertNotNull(usuarioAutenticado);
        assertEquals(cpf, usuarioAutenticado.getCpf());
        assertEquals("Gerente Teste", usuarioAutenticado.getNome());
    }
    
    @Test
    void testAutenticarUsuarioInexistente() {
        // Act e Assert
        ValidacaoException exception = assertThrows(ValidacaoException.class, () -> {
            sistema.autenticar("999.999.999-99", "senhaqualquer");
        });
        
        assertEquals("Usuário não encontrado.", exception.getMessage());
    }
    
    @Test
    void testAutenticarComSenhaIncorreta() throws ValidacaoException {
        // Arrange
        String cpf = "111.222.333-44";
        String senhaCorreta = "senha123";
        String senhaIncorreta = "senhaerrada";
        Franquia franquia = new Franquia(1, "Franquia Teste", endereco);
        sistema.adicionarGerente("Gerente Teste", cpf, "gerente@teste.com", senhaCorreta, franquia);
        
        // Act e Assert
        ValidacaoException exception = assertThrows(ValidacaoException.class, () -> {
            sistema.autenticar(cpf, senhaIncorreta);
        });
        
        assertEquals("Senha incorreta.", exception.getMessage());
    }
    
    @Test
    void testRemoverUsuarioComSucesso() throws ValidacaoException {
        // Arrange
        String cpf = "111.222.333-44";
        Franquia franquia = new Franquia(1, "Franquia Teste", endereco);
        sistema.adicionarGerente("Gerente Teste", cpf, "gerente@teste.com", "senha123", franquia);
        assertTrue(sistema.getUsuarios().containsKey(cpf));
        
        // Act
        sistema.removerUsuario(cpf);
        
        // Assert
        assertFalse(sistema.getUsuarios().containsKey(cpf));
    }
    
    @Test
    void testRemoverUsuarioInexistente() {
        // Act e Assert
        ValidacaoException exception = assertThrows(ValidacaoException.class, () -> {
            sistema.removerUsuario("999.999.999-99");
        });
        
        assertEquals("Usuário não encontrado.", exception.getMessage());
    }
}
