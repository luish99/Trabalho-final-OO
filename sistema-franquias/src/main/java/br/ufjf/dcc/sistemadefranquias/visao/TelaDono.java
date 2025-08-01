package br.ufjf.dcc.sistemadefranquias.visao;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import br.ufjf.dcc.sistemadefranquias.controle.Sistema;
import br.ufjf.dcc.sistemadefranquias.excecoes.ValidacaoException;
import br.ufjf.dcc.sistemadefranquias.modelo.Endereco;
import br.ufjf.dcc.sistemadefranquias.modelo.Franquia;
import br.ufjf.dcc.sistemadefranquias.modelo.Gerente;
import br.ufjf.dcc.sistemadefranquias.modelo.Vendedor;

public class TelaDono extends JFrame {
    private Sistema sistema;

    public TelaDono(Sistema sistema) {
        this.sistema = sistema;
        setTitle("Menu Dono");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel painel = new JPanel(new GridLayout(4, 1, 10, 10));
        JButton btnCadastrarFranquia = new JButton("Cadastrar Franquia");
        JButton btnRemoverFranquia = new JButton("Remover Franquia");
        JButton btnListarFranquias = new JButton("Listar Franquias");
        JButton btnVerDesempenho = new JButton("Ver Desempenho das Franquias");
        JButton btnSair = new JButton("Sair");

        painel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        btnCadastrarFranquia.addActionListener(e -> abrirDialogCadastroFranquia());
        btnListarFranquias.addActionListener(e -> exibirListaFranquias());
        btnRemoverFranquia.addActionListener(e -> removerFranquia());
        btnVerDesempenho.addActionListener(e -> exibirDesempenhoFranquias());


        btnSair.addActionListener(e -> System.exit(0));

        painel.add(btnCadastrarFranquia);
        painel.add(btnRemoverFranquia);  
        painel.add(btnListarFranquias);  // ainda não implementado
        painel.add(btnVerDesempenho);
        painel.add(btnSair);

        add(painel);
    }

    private void abrirDialogCadastroFranquia() {
    JDialog dialog = new JDialog(this, "Nova Franquia", true);
    dialog.setSize(400, 400);
    dialog.setLayout(new GridLayout(10, 2, 10, 10));
    dialog.setLocationRelativeTo(this);

    JLabel lblNome = new JLabel("Nome:");
    JTextField campoNome = new JTextField();

    JLabel lblRua = new JLabel("Rua:");
    JTextField campoRua = new JTextField();

    JLabel lblNumero = new JLabel("Número:");
    JTextField campoNumero = new JTextField();

    JLabel lblBairro = new JLabel("Bairro:");
    JTextField campoBairro = new JTextField();

    JLabel lblCidade = new JLabel("Cidade:");
    JTextField campoCidade = new JTextField();

    JLabel lblEstado = new JLabel("Estado:");
    JTextField campoEstado = new JTextField();

    JLabel lblCep = new JLabel("CEP:");
    JTextField campoCep = new JTextField();

    JLabel lblComplemento = new JLabel("Complemento:");
    JTextField campoComplemento = new JTextField();

    JLabel lblGerente = new JLabel("Gerente:");
    JComboBox<Gerente> comboGerentes = new JComboBox<>();

    // Adiciona os gerentes disponíveis no comboBox
    for (Gerente gerente : sistema.getGerentes().values()) {
        comboGerentes.addItem(gerente);
    }

    JButton btnCadastrar = new JButton("Cadastrar");
    JButton btnCancelar = new JButton("Cancelar");

    btnCadastrar.addActionListener(e -> {
        String nome = campoNome.getText().trim();
        String rua = campoRua.getText().trim();
        String numero = campoNumero.getText().trim();
        String bairro = campoBairro.getText().trim();
        String cidade = campoCidade.getText().trim();
        String estado = campoEstado.getText().trim();
        String cep = campoCep.getText().trim();
        String complemento = campoComplemento.getText().trim();
        Gerente gerenteSelecionado = (Gerente) comboGerentes.getSelectedItem();

        if (nome.isEmpty() || rua.isEmpty() || numero.isEmpty() || bairro.isEmpty() ||
            cidade.isEmpty() || estado.isEmpty() || cep.isEmpty() || gerenteSelecionado == null) {
            JOptionPane.showMessageDialog(dialog, "Preencha todos os campos obrigatórios.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Endereco endereco = new Endereco(rua, numero, bairro, cidade, estado, cep, complemento);

        try {
            sistema.adicionarFranquia(nome, endereco, gerenteSelecionado);
            JOptionPane.showMessageDialog(dialog, "Franquia cadastrada com sucesso!");
            dialog.dispose();
        } catch (ValidacaoException ex) {
            JOptionPane.showMessageDialog(dialog, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    });

    btnCancelar.addActionListener(e -> dialog.dispose());

    dialog.add(lblNome);
    dialog.add(campoNome);
    dialog.add(lblRua);
    dialog.add(campoRua);
    dialog.add(lblNumero);
    dialog.add(campoNumero);
    dialog.add(lblBairro);
    dialog.add(campoBairro);
    dialog.add(lblCidade);
    dialog.add(campoCidade);
    dialog.add(lblEstado);
    dialog.add(campoEstado);
    dialog.add(lblCep);
    dialog.add(campoCep);
    dialog.add(lblComplemento);
    dialog.add(campoComplemento);
    dialog.add(lblGerente);
    dialog.add(comboGerentes);
    dialog.add(btnCadastrar);
    dialog.add(btnCancelar);

    dialog.setVisible(true);
    }
    private void removerFranquia() {
    String nome = JOptionPane.showInputDialog(null, "Digite o nome da franquia a remover:");
    if (nome == null || nome.isBlank()) {
        JOptionPane.showMessageDialog(null, "Nome inválido.");
        return;
    }

    Franquia franquia = sistema.buscarFranquiaPorNome(nome);
    if (franquia == null) {
        JOptionPane.showMessageDialog(null, "Franquia não encontrada.");
        return;
    }

    try {
        sistema.removerFranquia(franquia.getId());
        JOptionPane.showMessageDialog(null, "Franquia removida com sucesso.");
    } catch (ValidacaoException e) {
        JOptionPane.showMessageDialog(null, "Erro: " + e.getMessage());
    }
}

    private void exibirListaFranquias() {
    JDialog dialog = new JDialog(this, "Lista de Franquias", true);
    dialog.setSize(600, 400);
    dialog.setLocationRelativeTo(this);
    dialog.setLayout(new BorderLayout());

    JTextArea areaTexto = new JTextArea();
    areaTexto.setEditable(false);

    StringBuilder sb = new StringBuilder();
    if (sistema.getFranquias().isEmpty()) {
        sb.append("Nenhuma franquia cadastrada.");
    } else {
        for (Franquia franquia : sistema.getFranquias().values()) {
            
            sb.append("ID: ").append(franquia.getId()).append("\n");
            sb.append("Nome: ").append(franquia.getNome()).append("\n");
            sb.append("Endereço: ").append(franquia.getEndereco().toString()).append("\n");

            
            Gerente gerente = franquia.getGerente();
            sb.append("Gerente: ").append(gerente != null ? gerente.getNome() : "Sem gerente").append("\n");

            sb.append("Vendedores:\n");
            Map<String, Vendedor> vendedores = franquia.getVendedores();
            if (vendedores.isEmpty()) {
                sb.append("  Nenhum vendedor associado.\n");
            } else {
                for (Vendedor vendedor : vendedores.values()) {
                    sb.append("  - ").append(vendedor.getNome()).append("\n");
                }
            }

            
            sb.append("Resumo:\n");
            sb.append("  Total de produtos no estoque: ").append(franquia.getEstoque().size()).append("\n");
            sb.append("  Total de pedidos: ").append(franquia.getPedidos().size()).append("\n");
            
            
            sb.append("--------------------------------------------------\n");
        }
    }

    areaTexto.setText(sb.toString());

    JScrollPane scroll = new JScrollPane(areaTexto);
    dialog.add(scroll, BorderLayout.CENTER);

    JButton btnFechar = new JButton("Fechar");
    btnFechar.addActionListener(e -> dialog.dispose());
    dialog.add(btnFechar, BorderLayout.SOUTH);

    dialog.setVisible(true);
}
private void exibirDesempenhoFranquias() {
    JDialog dialog = new JDialog(this, "Desempenho das Franquias", true);
    dialog.setSize(600, 400);
    dialog.setLocationRelativeTo(this);

    JTextArea texto = new JTextArea();
    texto.setEditable(false);

    StringBuilder sb = new StringBuilder();

    if (sistema.getFranquias().isEmpty()) {
        sb.append("Nenhuma franquia cadastrada.");
    } else {
        for (Franquia f : sistema.getFranquias().values()) {
            sb.append("Franquia: ").append(f.getNome()).append("\n");

            // Exemplo: número de pedidos
            int totalPedidos = f.getPedidos().size();

            // Exemplo: faturamento (supondo que Pedido tenha método getValorTotal())
            double faturamento = f.getPedidos().values().stream()
                .mapToDouble(p -> p.getValorTotal())
                .sum();

            // Ticket médio
            double ticketMedio = totalPedidos > 0 ? faturamento / totalPedidos : 0;

            sb.append("  Total de Pedidos: ").append(totalPedidos).append("\n");
            sb.append("  Faturamento Total: R$ ").append(String.format("%.2f", faturamento)).append("\n");
            sb.append("  Ticket Médio: R$ ").append(String.format("%.2f", ticketMedio)).append("\n");
            sb.append("-----------------------------------------\n");
        }
    }

    texto.setText(sb.toString());
    dialog.add(new JScrollPane(texto));
    dialog.setVisible(true);
}

}

