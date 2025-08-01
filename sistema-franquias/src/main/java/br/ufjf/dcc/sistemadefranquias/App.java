// br/ufjf/dcc/sistemadefranquias/App.java
package br.ufjf.dcc.sistemadefranquias;

import java.io.IOException;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import br.ufjf.dcc.sistemadefranquias.controle.Sistema;
import br.ufjf.dcc.sistemadefranquias.persistencia.Persistencia;
import br.ufjf.dcc.sistemadefranquias.visao.TelaLogin;

public class App {
    public static void main(String[] args) {
        Sistema sistema = carregarOuCriarSistema();

        // Garante que a GUI seja executada na thread de despacho de eventos
        SwingUtilities.invokeLater(() -> new TelaLogin(sistema));

        // Adiciona um gancho de desligamento para salvar os dados ao fechar
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
        try {
            System.out.println("Carregando dados existentes do arquivo de texto...");
            return Persistencia.carregar();
        } catch (IOException e) { // Apenas IOException é necessária agora
            JOptionPane.showMessageDialog(null, "Erro ao ler arquivo de dados. Um novo sistema será criado.", "Erro de Leitura", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            // Retorna um sistema novo em caso de erro de leitura
            Sistema novoSistema = new Sistema();
            // ... (criação do dono padrão)
            return novoSistema;
        }
    }
    }
