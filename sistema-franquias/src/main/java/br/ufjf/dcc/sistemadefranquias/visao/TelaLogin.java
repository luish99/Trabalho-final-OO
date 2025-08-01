// br/ufjf/dcc/sistemadefranquias/visao/TelaLogin.java
package br.ufjf.dcc.sistemadefranquias.visao;

import br.ufjf.dcc.sistemadefranquias.controle.Sistema;
import br.ufjf.dcc.sistemadefranquias.excecoes.ValidacaoException;
import br.ufjf.dcc.sistemadefranquias.modelo.Dono;
import br.ufjf.dcc.sistemadefranquias.modelo.Gerente;
import br.ufjf.dcc.sistemadefranquias.modelo.Usuario;
import br.ufjf.dcc.sistemadefranquias.modelo.Vendedor;

import javax.swing.*;
import java.awt.*;

public class TelaLogin extends JFrame {
    private Sistema sistema;
    private JTextField campoCpf;
    private JPasswordField campoSenha;

    public TelaLogin(Sistema sistema) {
        super("Login - Sistema de Franquias");
        this.sistema = sistema;

        // Adiciona um dono padrão se não houver usuários (primeira execução)
        if (sistema.getUsuarios().isEmpty()) {
            try {
                sistema.adicionarDono("Dono Padrão", "admin", "admin@franquia.com", "admin");
                JOptionPane.showMessageDialog(null, "Primeira execução detectada.\nUsuário 'Dono' criado:\nCPF: admin\nSenha: admin", "Boas-vindas", JOptionPane.INFORMATION_MESSAGE);
            } catch (ValidacaoException e) {
                // Não deve acontecer
            }
        }

        campoCpf = new JTextField(20);
        campoSenha = new JPasswordField(20);
        JButton botaoLogin = new JButton("Entrar");

        JPanel painel = new JPanel(new GridLayout(3, 2, 5, 5));
        painel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        painel.add(new JLabel("CPF:"));
        painel.add(campoCpf);
        painel.add(new JLabel("Senha:"));
        painel.add(campoSenha);
        painel.add(new JLabel(""));
        painel.add(botaoLogin);

        // Adiciona o painel ao frame
        add(painel);

        botaoLogin.addActionListener(e -> realizarLogin());
        // Permite login com a tecla Enter no campo de senha
        campoSenha.addActionListener(e -> realizarLogin());

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void realizarLogin() {
        try {
            String cpf = campoCpf.getText();
            String senha = new String(campoSenha.getPassword());
            Usuario usuario = sistema.autenticar(cpf, senha);

            // Polimorfismo na GUI: abre a tela correta para o tipo de usuário
            if (usuario instanceof Dono) {
                new TelaDono(sistema, (Dono) usuario);
            } else if (usuario instanceof Gerente) {
                if (((Gerente) usuario).getFranquiaGerenciada() == null) {
                    JOptionPane.showMessageDialog(this, "Você é um gerente, mas não está associado a nenhuma franquia.\nContate o dono do sistema.", "Acesso Negado", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                new TelaGerente(sistema, (Gerente) usuario);
            } else if (usuario instanceof Vendedor) {
                 new TelaVendedor(sistema, (Vendedor) usuario);
            }
            this.dispose(); // Fecha a tela de login
        } catch (ValidacaoException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro de Login", JOptionPane.ERROR_MESSAGE);
        }
    }
}