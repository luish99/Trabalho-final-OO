// br/ufjf/dcc/sistemadefranquias/App.java
package br.ufjf.dcc.sistemadefranquias;

import br.ufjf.dcc.sistemadefranquias.controle.CargaDeDados; 
import br.ufjf.dcc.sistemadefranquias.controle.Sistema;
import br.ufjf.dcc.sistemadefranquias.persistencia.Persistencia;
import br.ufjf.dcc.sistemadefranquias.visao.TelaLogin;
import java.io.File; 
import java.io.IOException;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

public class App {
    public static void main(String[] args) {
        Sistema sistema = carregarOuCriarSistema();

        SwingUtilities.invokeLater(() -> new TelaLogin(sistema));
        //garante que quando fechado ira salvar os dados
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                Persistencia.salvar(sistema);
                System.out.println("Dados salvos com sucesso.");
            } catch (IOException e) {
                e.printStackTrace();
                System.err.println("Falha ao salvar os dados.");
            }
        }));
    }

    private static Sistema carregarOuCriarSistema() {
        File arquivoDeDados = new File(Persistencia.ARQUIVO_DADOS);

        if (arquivoDeDados.exists()) {
            // Se o arquivo existe, carrega os dados dele
            try {
                System.out.println("Arquivo de dados encontrado. Carregando sistema...");
                return Persistencia.carregar();
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Erro ao ler arquivo de dados. Um novo sistema será criado.", "Erro de Leitura", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
        
        // Se o arquivo NÃO existe (ou deu erro ao ler), cria um novo sistema e o popula
        System.out.println("Arquivo de dados não encontrado. Criando um novo sistema com dados de teste...");
        Sistema novoSistema = new Sistema();
        CargaDeDados.popularSistemaComDadosIniciais(novoSistema);
        return novoSistema;
    }
}