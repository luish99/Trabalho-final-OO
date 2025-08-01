// br/ufjf/dcc/sistemadefranquias/visao/TelaGerente.java
package br.ufjf.dcc.sistemadefranquias.visao;

import br.ufjf.dcc.sistemadefranquias.controle.Sistema;
import br.ufjf.dcc.sistemadefranquias.excecoes.ValidacaoException;
import br.ufjf.dcc.sistemadefranquias.modelo.*;

import javax.swing.*;
import java.awt.*;

public class TelaGerente extends JFrame {
    private Sistema sistema;
    private Gerente gerente;
    private Franquia franquia;

    public TelaGerente(Sistema sistema, Gerente gerente) {
        this.sistema = sistema;
        this.gerente = gerente;
        this.franquia = gerente.getFranquiaGerenciada();

        setTitle("Painel do Gerente: " + gerente.getNome() + " | Franquia: " + franquia.getNome());
        setSize(500, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel painel = new JPanel(new GridLayout(0, 2, 10, 10));
        painel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Títulos de Seção
        painel.add(new JLabel("<html><b>--- Equipe de Vendas ---</b></html>"));
        painel.add(new JLabel(""));
        
        // Botões de Vendedores
        JButton btnCadastrarVendedor = new JButton("Cadastrar Vendedor");
        JButton btnRemoverVendedor = new JButton("Remover Vendedor");
        
        painel.add(btnCadastrarVendedor);
        painel.add(btnRemoverVendedor);

        // Seção de Produtos
        painel.add(new JLabel("<html><b>--- Estoque e Produtos ---</b></html>"));
        painel.add(new JLabel(""));
        
        JButton btnCadastrarProduto = new JButton("Cadastrar Produto");
        JButton btnEditarProduto = new JButton("Editar Produto");
        JButton btnRemoverProduto = new JButton("Remover Produto");
        JButton btnVerEstoqueBaixo = new JButton("Ver Estoque Baixo");
        
        painel.add(btnCadastrarProduto);
        painel.add(btnEditarProduto);
        painel.add(btnRemoverProduto);
        painel.add(btnVerEstoqueBaixo);
        
        // Seção de Pedidos
        painel.add(new JLabel("<html><b>--- Pedidos e Relatórios ---</b></html>"));
        painel.add(new JLabel(""));
        
        JButton btnVerPedidos = new JButton("Ver Todos os Pedidos");
        JButton btnRelatorioClientes = new JButton("Clientes Mais Recorrentes");
        
        painel.add(btnVerPedidos);
        painel.add(btnRelatorioClientes);

        JButton btnSair = new JButton("Sair");
        
        // Adicionando actions
        btnCadastrarVendedor.addActionListener(e -> cadastrarVendedor());
        // ... (outras actions)

        btnSair.addActionListener(e -> {
            new TelaLogin(sistema);
            dispose();
        });
        
        painel.add(new JLabel("")); // Espaçador
        painel.add(btnSair);

        add(painel);
        setVisible(true);
    }

    private void cadastrarVendedor() {
        JDialog dialog = new JDialog(this, "Novo Vendedor", true);
        dialog.setLayout(new GridLayout(5, 2, 10, 10));
        dialog.setSize(400, 250);
        dialog.setLocationRelativeTo(this);

        JTextField campoNome = new JTextField();
        JTextField campoCpf = new JTextField();
        JTextField campoEmail = new JTextField();
        JPasswordField campoSenha = new JPasswordField();
        JButton btnCadastrar = new JButton("Cadastrar");

        btnCadastrar.addActionListener(e -> {
            try {
                String nome = campoNome.getText().trim();
                String cpf = campoCpf.getText().trim();
                String email = campoEmail.getText().trim();
                String senha = new String(campoSenha.getPassword());

                if (nome.isEmpty() || cpf.isEmpty() || email.isEmpty() || senha.isEmpty()) {
                    throw new ValidacaoException("Todos os campos são obrigatórios.");
                }

                sistema.adicionarVendedor(nome, cpf, email, senha, this.franquia);
                JOptionPane.showMessageDialog(dialog, "Vendedor cadastrado com sucesso!");
                dialog.dispose();
            } catch (ValidacaoException ex) {
                JOptionPane.showMessageDialog(dialog, ex.getMessage(), "Erro de Validação", JOptionPane.ERROR_MESSAGE);
            }
        });

        dialog.add(new JLabel("Nome:")); dialog.add(campoNome);
        dialog.add(new JLabel("CPF:")); dialog.add(campoCpf);
        dialog.add(new JLabel("Email:")); dialog.add(campoEmail);
        dialog.add(new JLabel("Senha:")); dialog.add(campoSenha);
        dialog.add(new JButton("Cancelar")); dialog.add(btnCadastrar);
        
        dialog.setVisible(true);
    }
    
    // Outros métodos para as funcionalidades do gerente (remover vendedor, gerenciar produtos, etc.)
    // seriam implementados de forma similar, criando JDialogs para interação
    // e chamando os métodos correspondentes no objeto 'sistema' ou 'franquia'.
}