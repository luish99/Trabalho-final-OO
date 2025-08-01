// br/ufjf/dcc/sistemadefranquias/visao/TelaVendedor.java
package br.ufjf.dcc.sistemadefranquias.visao;

import br.ufjf.dcc.sistemadefranquias.controle.Sistema;
import br.ufjf.dcc.sistemadefranquias.modelo.*;

import javax.swing.*;
import java.awt.*;
import java.util.Date;
import java.util.HashMap;

public class TelaVendedor extends JFrame {
    private Sistema sistema;
    private Vendedor vendedor;
    private Franquia franquia;
    private int proximoIdPedido = 1; // Simplificação: idealmente viria do sistema/banco

    public TelaVendedor(Sistema sistema, Vendedor vendedor) {
        this.sistema = sistema;
        this.vendedor = vendedor;
        this.franquia = vendedor.getFranquia();

        setTitle("Painel do Vendedor: " + vendedor.getNome() + " | Franquia: " + franquia.getNome());
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        JPanel painel = new JPanel(new GridLayout(0, 2, 10, 10));
        painel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JButton btnNovoPedido = new JButton("Registrar Novo Pedido");
        JButton btnMeusPedidos = new JButton("Visualizar Meus Pedidos");
        JButton btnSair = new JButton("Sair");

        btnNovoPedido.addActionListener(e -> registrarNovoPedido());
        btnMeusPedidos.addActionListener(e -> visualizarMeusPedidos());

        btnSair.addActionListener(e -> {
            new TelaLogin(sistema);
            dispose();
        });

        painel.add(btnNovoPedido);
        painel.add(btnMeusPedidos);
        painel.add(new JLabel(""));
        painel.add(btnSair);

        add(painel);
        setVisible(true);
    }

    private void registrarNovoPedido() {
        JDialog dialog = new JDialog(this, "Registrar Pedido", true);
        dialog.setSize(500, 400);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout(10, 10));

        // Painel de dados do pedido
        JPanel painelDados = new JPanel(new GridLayout(0, 2, 5, 5));
        JTextField campoNomeCliente = new JTextField();
        JTextField campoCpfCliente = new JTextField();
        JComboBox<String> comboPagamento = new JComboBox<>(new String[]{"Cartão de Crédito", "Dinheiro", "PIX"});
        
        painelDados.add(new JLabel("Nome do Cliente:"));
        painelDados.add(campoNomeCliente);
        painelDados.add(new JLabel("CPF do Cliente:"));
        painelDados.add(campoCpfCliente);
        painelDados.add(new JLabel("Forma de Pagamento:"));
        painelDados.add(comboPagamento);

        // Painel de produtos
        JPanel painelProdutos = new JPanel(new BorderLayout());
        DefaultListModel<String> modeloLista = new DefaultListModel<>();
        JList<String> listaProdutosPedido = new JList<>(modeloLista);
        painelProdutos.add(new JScrollPane(listaProdutosPedido), BorderLayout.CENTER);
        
        JButton btnAddProduto = new JButton("Adicionar Produto");
        
        // Lógica simplificada para adicionar produtos
        btnAddProduto.addActionListener(e -> {
            // Em um sistema real, aqui abriria outra janela para selecionar produtos do estoque
            String nomeProduto = JOptionPane.showInputDialog(dialog, "Nome do Produto:");
            if(nomeProduto != null && !nomeProduto.isBlank()) {
                modeloLista.addElement(nomeProduto);
            }
        });
        
        painelProdutos.add(btnAddProduto, BorderLayout.SOUTH);

        // Painel de botões
        JPanel painelBotoes = new JPanel();
        JButton btnSalvar = new JButton("Salvar Pedido");
        JButton btnCancelar = new JButton("Cancelar");
        painelBotoes.add(btnSalvar);
        painelBotoes.add(btnCancelar);

        btnSalvar.addActionListener(e -> {
            try {
                Cliente cliente = new Cliente(1, campoNomeCliente.getText(), campoCpfCliente.getText(), "");
                // Lógica de cálculo de valor e criação do pedido
                double valorTotal = modeloLista.size() * 50.0; // Exemplo: cada produto custa 50.0
                
                Pedido novoPedido = new Pedido(proximoIdPedido++, new Date(), cliente, valorTotal, "Pendente");
                novoPedido.setVendedor(vendedor);
                novoPedido.setProdutosPedidos(new HashMap<>()); // Simplificado

                sistema.registrarPedido(franquia, novoPedido);
                JOptionPane.showMessageDialog(dialog, "Pedido registrado com sucesso!");
                dialog.dispose();

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Erro ao registrar pedido: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        btnCancelar.addActionListener(e -> dialog.dispose());

        dialog.add(painelDados, BorderLayout.NORTH);
        dialog.add(painelProdutos, BorderLayout.CENTER);
        dialog.add(painelBotoes, BorderLayout.SOUTH);

        dialog.setVisible(true);
    }
    
    private void visualizarMeusPedidos() {
        // Implementação para mostrar os pedidos do vendedor logado
        JDialog dialog = new JDialog(this, "Meus Pedidos", true);
        dialog.setSize(600, 400);
        dialog.setLocationRelativeTo(this);
        JTextArea areaTexto = new JTextArea();
        areaTexto.setEditable(false);
        
        StringBuilder sb = new StringBuilder();
        franquia.getPedidos().values().stream()
            .filter(p -> p.getVendedor() != null && p.getVendedor().equals(this.vendedor))
            .forEach(p -> {
                sb.append("ID: ").append(p.getId()).append("\n");
                sb.append("Data: ").append(p.getDataPedido()).append("\n");
                sb.append("Cliente: ").append(p.getCliente().getNome()).append("\n");
                sb.append(String.format("Valor: R$ %.2f\n", p.getValorTotal()));
                sb.append("Status: ").append(p.getStatus()).append("\n");
                sb.append("-----------------------------\n");
            });

        if(sb.length() == 0) {
            sb.append("Você ainda não registrou nenhum pedido.");
        }

        areaTexto.setText(sb.toString());
        dialog.add(new JScrollPane(areaTexto));
        dialog.setVisible(true);
    }
}