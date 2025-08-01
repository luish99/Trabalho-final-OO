// br/ufjf/dcc/sistemadefranquias/visao/TelaLogin.java
package br.ufjf.dcc.sistemadefranquias.visao;

import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import br.ufjf.dcc.sistemadefranquias.controle.Sistema;
import br.ufjf.dcc.sistemadefranquias.modelo.Dono;
import br.ufjf.dcc.sistemadefranquias.modelo.Gerente;
import br.ufjf.dcc.sistemadefranquias.modelo.Usuario;
import br.ufjf.dcc.sistemadefranquias.modelo.Vendedor;

public class TelaLogin extends JFrame {
    private Sistema sistema;
    private JTextField campoCpf;
    private JPasswordField campoSenha;

    public TelaLogin(Sistema sistema) {
        super("Login - Sistema de Franquias");
        this.sistema = sistema;

        campoCpf = new JTextField(20);
        campoSenha = new JPasswordField(20);
        JButton botaoLogin = new JButton("Entrar");

        setLayout(new GridLayout(3, 2, 5, 5));
        add(new JLabel("CPF:"));
        add(campoCpf);
        add(new JLabel("Senha:"));
        add(campoSenha);
        add(new JLabel(""));
        add(botaoLogin);

        botaoLogin.addActionListener(e -> realizarLogin());

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
                // new TelaDono(sistema, (Dono) usuario);
                JOptionPane.showMessageDialog(this, "Bem-vindo, Dono!");
            } else if (usuario instanceof Gerente) {
                // new TelaGerente(sistema, (Gerente) usuario);
                 JOptionPane.showMessageDialog(this, "Bem-vindo, Gerente!");
            } else if (usuario instanceof Vendedor) {
                // new TelaVendedor(sistema, (Vendedor) usuario);
                 JOptionPane.showMessageDialog(this, "Bem-vindo, Vendedor!");
            }
            this.dispose(); // Fecha a tela de login
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro de Login", JOptionPane.ERROR_MESSAGE);
        }
    }
}