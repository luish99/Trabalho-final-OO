// br/ufjf/dcc/sistemadefranquias/visao/TelaDono.java
package br.ufjf.dcc.sistemadefranquias.visao;

import br.ufjf.dcc.sistemadefranquias.controle.Sistema;
import br.ufjf.dcc.sistemadefranquias.excecoes.ValidacaoException;
import br.ufjf.dcc.sistemadefranquias.modelo.*;

import javax.swing.*;
import java.awt.*;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TelaDono extends JFrame {
    private Sistema sistema;
    private Dono dono;

    public TelaDono(Sistema sistema, Dono dono) {
        this.sistema = sistema;
        this.dono = dono;
        setTitle("Painel do Dono: " + dono.getNome());
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel painel = new JPanel(new GridLayout(0, 2, 10, 10));
        painel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Botões de Franquias
        painel.add(new JLabel("<html><b>--- Gestão de Franquias ---</b></html>"));
        painel.add(new JLabel(""));
        JButton btnCadastrarFranquia = new JButton("Cadastrar Franquia");
        JButton btnRemoverFranquia = new JButton("Remover Franquia");
        JButton btnListarFranquias = new JButton("Listar Franquias");
        JButton btnVerDesempenho = new JButton("Desempenho das Franquias");

        // Botões de Gerentes
        painel.add(new JLabel("<html><b>--- Gestão de Gerentes ---</b></html>"));
        painel.add(new JLabel(""));
        JButton btnCadastrarGerente = new JButton("Cadastrar Gerente");
        JButton btnRemoverGerente = new JButton("Remover Gerente");

        // Botões de Relatórios
        painel.add(new JLabel("<html><b>--- Relatórios ---</b></html>"));
        painel.add(new JLabel(""));
        JButton btnRankingVendedores = new JButton("Ranking de Vendedores");

        // Botão de Sair
        JButton btnSair = new JButton("Sair");

        // Adicionando Actions
        btnCadastrarFranquia.addActionListener(e -> abrirDialogCadastroFranquia());
        btnRemoverFranquia.addActionListener(e -> removerFranquia());
        btnListarFranquias.addActionListener(e -> exibirListaFranquias());
        btnVerDesempenho.addActionListener(e -> exibirDesempenhoFranquias());
        btnCadastrarGerente.addActionListener(e -> abrirDialogCadastroGerente());
        btnRemoverGerente.addActionListener(e -> removerGerente());
        btnRankingVendedores.addActionListener(e -> exibirRankingVendedores());
        btnSair.addActionListener(e -> {
            new TelaLogin(sistema);
            dispose();
        });


        // Adicionando ao painel
        painel.add(btnCadastrarFranquia);
        painel.add(btnRemoverFranquia);
        painel.add(btnListarFranquias);
        painel.add(btnVerDesempenho);
        painel.add(btnCadastrarGerente);
        painel.add(btnRemoverGerente);
        painel.add(btnRankingVendedores);
        painel.add(btnSair);


        add(painel);
        setVisible(true);

        // Notificação de franquias sem gerente
        verificarFranquiasSemGerente();
    }

    private void verificarFranquiasSemGerente() {
        String franquiasSemGerente = sistema.getFranquias().values().stream()
                .filter(f -> f.getGerente() == null)
                .map(Franquia::getNome)
                .collect(Collectors.joining(", "));

        if (!franquiasSemGerente.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Atenção! As seguintes franquias estão sem gerente:\n" + franquiasSemGerente,
                    "Alerta", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void abrirDialogCadastroFranquia() {
        // Implementação já existente, mantida
        JDialog dialog = new JDialog(this, "Nova Franquia", true);
        dialog.setSize(400, 400);
        dialog.setLayout(new GridLayout(10, 2, 10, 10));
        dialog.setLocationRelativeTo(this);

        JTextField campoNome = new JTextField();
        JTextField campoRua = new JTextField();
        JTextField campoNumero = new JTextField();
        JTextField campoBairro = new JTextField();
        JTextField campoCidade = new JTextField();
        JTextField campoEstado = new JTextField();
        JTextField campoCep = new JTextField();
        JTextField campoComplemento = new JTextField();
        JComboBox<String> comboGerentes = new JComboBox<>();

        // Popula o combo com gerentes que ainda não gerenciam uma franquia
        sistema.getGerentes().values().stream()
                .filter(g -> g.getFranquiaGerenciada() == null)
                .forEach(g -> comboGerentes.addItem(g.getNome() + " (" + g.getCpf() + ")"));
        comboGerentes.addItem("Nenhum (cadastrar depois)");


        JButton btnCadastrar = new JButton("Cadastrar");

        btnCadastrar.addActionListener(e -> {
            try {
                String nome = campoNome.getText().trim();
                Endereco endereco = new Endereco(campoRua.getText(), campoNumero.getText(), campoComplemento.getText(), campoBairro.getText(), campoCidade.getText(), campoEstado.getText(), campoCep.getText());
                Gerente gerenteSelecionado = null;

                if (comboGerentes.getSelectedIndex() != -1 && !comboGerentes.getSelectedItem().equals("Nenhum (cadastrar depois)")) {
                    String cpfGerente = ((String) comboGerentes.getSelectedItem()).split("\\(")[1].replace(")", "");
                    gerenteSelecionado = sistema.getGerentes().get(cpfGerente);
                }
                
                // Note que o método adicionarFranquia no Sistema foi ajustado para aceitar gerente nulo
                sistema.adicionarFranquia(nome, endereco, gerenteSelecionado);
                JOptionPane.showMessageDialog(dialog, "Franquia cadastrada com sucesso!");
                dialog.dispose();
            } catch (ValidacaoException ex) {
                JOptionPane.showMessageDialog(dialog, ex.getMessage(), "Erro de Validação", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Erro inesperado: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        dialog.add(new JLabel("Nome:")); dialog.add(campoNome);
        dialog.add(new JLabel("Rua:")); dialog.add(campoRua);
        dialog.add(new JLabel("Número:")); dialog.add(campoNumero);
        dialog.add(new JLabel("Bairro:")); dialog.add(campoBairro);
        dialog.add(new JLabel("Cidade:")); dialog.add(campoCidade);
        dialog.add(new JLabel("Estado:")); dialog.add(campoEstado);
        dialog.add(new JLabel("CEP:")); dialog.add(campoCep);
        dialog.add(new JLabel("Complemento:")); dialog.add(campoComplemento);
        dialog.add(new JLabel("Gerente:")); dialog.add(comboGerentes);
        dialog.add(new JButton("Cancelar")); dialog.add(btnCadastrar);

        dialog.setVisible(true);
    }

    private void removerFranquia() {
        // Implementação já existente, mantida
        if (sistema.getFranquias().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nenhuma franquia para remover.", "Aviso", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        String[] nomesFranquias = sistema.getFranquias().values().stream().map(Franquia::getNome).toArray(String[]::new);
        String nomeFranquia = (String) JOptionPane.showInputDialog(this, "Selecione a franquia a ser removida:", "Remover Franquia", JOptionPane.QUESTION_MESSAGE, null, nomesFranquias, nomesFranquias[0]);

        if (nomeFranquia != null) {
            Franquia franquia = sistema.buscarFranquiaPorNome(nomeFranquia);
            int confirm = JOptionPane.showConfirmDialog(this, "Tem certeza que deseja remover a franquia '" + nomeFranquia + "'? Esta ação não pode ser desfeita.", "Confirmação", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    sistema.removerFranquia(franquia.getId());
                    JOptionPane.showMessageDialog(this, "Franquia removida com sucesso.");
                } catch (ValidacaoException e) {
                    JOptionPane.showMessageDialog(this, e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    private void exibirListaFranquias() {
        // Implementação já existente, melhorada
        JDialog dialog = new JDialog(this, "Lista de Franquias", true);
        dialog.setSize(600, 400);
        dialog.setLocationRelativeTo(this);
        JTextArea areaTexto = new JTextArea();
        areaTexto.setEditable(false);
        areaTexto.setFont(new Font("Monospaced", Font.PLAIN, 12));
        StringBuilder sb = new StringBuilder();

        if (sistema.getFranquias().isEmpty()) {
            sb.append("Nenhuma franquia cadastrada.");
        } else {
            for (Franquia f : sistema.getFranquias().values()) {
                sb.append("ID: ").append(f.getId()).append("\n");
                sb.append("Nome: ").append(f.getNome()).append("\n");
                Endereco end = f.getEndereco();
                sb.append("Endereço: ").append(end.getLogradouro()).append(", ").append(end.getNumero()).append(" - ").append(end.getCidade()).append("\n");
                sb.append("Gerente: ").append(f.getGerente() != null ? f.getGerente().getNome() : "SEM GERENTE").append("\n");
                sb.append("Vendedores: ").append(f.getVendedores().size()).append("\n");
                sb.append("----------------------------------------\n");
            }
        }
        areaTexto.setText(sb.toString());
        dialog.add(new JScrollPane(areaTexto));
        dialog.setVisible(true);
    }

    private void exibirDesempenhoFranquias() {
        // Implementação já existente, mantida
         JDialog dialog = new JDialog(this, "Desempenho das Franquias", true);
        dialog.setSize(600, 400);
        dialog.setLocationRelativeTo(this);

        JTextArea texto = new JTextArea();
        texto.setEditable(false);
        texto.setFont(new Font("Monospaced", Font.PLAIN, 12));

        StringBuilder sb = new StringBuilder();
        if (sistema.getFranquias().isEmpty()) {
            sb.append("Nenhuma franquia cadastrada para exibir desempenho.");
        } else {
            for (Franquia f : sistema.getFranquias().values()) {
                sb.append("Franquia: ").append(f.getNome()).append("\n");
                int totalPedidos = f.getPedidos().size();
                double faturamento = f.getPedidos().values().stream().mapToDouble(Pedido::getValorTotal).sum();
                double ticketMedio = totalPedidos > 0 ? faturamento / totalPedidos : 0;
                sb.append(String.format("  - Faturamento Bruto: R$ %.2f\n", faturamento));
                sb.append(String.format("  - Total de Pedidos: %d\n", totalPedidos));
                sb.append(String.format("  - Ticket Médio: R$ %.2f\n", ticketMedio));
                sb.append("----------------------------------------\n");
            }
        }
        texto.setText(sb.toString());
        dialog.add(new JScrollPane(texto));
        dialog.setVisible(true);
    }

    private void abrirDialogCadastroGerente() {
        JDialog dialog = new JDialog(this, "Novo Gerente", true);
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
                
                if(nome.isEmpty() || cpf.isEmpty() || email.isEmpty() || senha.isEmpty()){
                    throw new ValidacaoException("Todos os campos são obrigatórios.");
                }

                // Adiciona o gerente sem franquia inicialmente.
                sistema.adicionarGerente(nome, cpf, email, senha, null);
                JOptionPane.showMessageDialog(dialog, "Gerente cadastrado com sucesso!");
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
    
    private void removerGerente() {
        if (sistema.getGerentes().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nenhum gerente para remover.", "Aviso", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        String[] nomesGerentes = sistema.getGerentes().values().stream()
            .map(g -> g.getNome() + " (" + g.getCpf() + ")")
            .toArray(String[]::new);
            
        String gerenteSelecionado = (String) JOptionPane.showInputDialog(this, "Selecione o gerente a ser removido:", "Remover Gerente", JOptionPane.QUESTION_MESSAGE, null, nomesGerentes, nomesGerentes[0]);

        if (gerenteSelecionado != null) {
            String cpf = gerenteSelecionado.split("\\(")[1].replace(")", "");
            int confirm = JOptionPane.showConfirmDialog(this, "Tem certeza que deseja remover este gerente? Se ele estiver associado a uma franquia, ela ficará sem gerente.", "Confirmação", JOptionPane.YES_NO_OPTION);
            
            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    sistema.removerUsuario(cpf);
                    JOptionPane.showMessageDialog(this, "Gerente removido com sucesso.");
                } catch (ValidacaoException e) {
                    JOptionPane.showMessageDialog(this, e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }
    
    private void exibirRankingVendedores() {
        if (sistema.getFranquias().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nenhuma franquia cadastrada.", "Aviso", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        String[] nomesFranquias = sistema.getFranquias().values().stream().map(Franquia::getNome).toArray(String[]::new);
        String nomeFranquia = (String) JOptionPane.showInputDialog(this, "Selecione a franquia para ver o ranking:", "Ranking de Vendedores", JOptionPane.QUESTION_MESSAGE, null, nomesFranquias, nomesFranquias[0]);
    
        if (nomeFranquia == null) return;

        Franquia franquia = sistema.buscarFranquiaPorNome(nomeFranquia);
        if (franquia.getVendedores().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Esta franquia não possui vendedores.", "Aviso", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        JDialog dialog = new JDialog(this, "Ranking de Vendedores - " + nomeFranquia, true);
        dialog.setSize(500, 350);
        dialog.setLocationRelativeTo(this);
        JTextArea areaTexto = new JTextArea();
        areaTexto.setFont(new Font("Monospaced", Font.PLAIN, 12));
        areaTexto.setEditable(false);

        // Calcula o valor de vendas por vendedor
        Map<Vendedor, Double> vendasPorVendedor = franquia.getPedidos().values().stream()
                .filter(p -> p.getVendedor() != null) // Garante que o pedido tem um vendedor
                .collect(Collectors.groupingBy(Pedido::getVendedor, Collectors.summingDouble(Pedido::getValorTotal)));

        // Adiciona vendedores que não venderam nada
        franquia.getVendedores().values().forEach(v -> vendasPorVendedor.putIfAbsent(v, 0.0));
        
        List<Map.Entry<Vendedor, Double>> ranking = vendasPorVendedor.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toList());

        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%-4s %-25s %15s\n", "Pos.", "Vendedor", "Valor Vendido"));
        sb.append("--------------------------------------------------\n");
        int pos = 1;
        for (Map.Entry<Vendedor, Double> entry : ranking) {
            sb.append(String.format("%-4d %-25s R$ %13.2f\n", pos++, entry.getKey().getNome(), entry.getValue()));
        }

        areaTexto.setText(sb.toString());
        dialog.add(new JScrollPane(areaTexto));
        dialog.setVisible(true);
    }
}