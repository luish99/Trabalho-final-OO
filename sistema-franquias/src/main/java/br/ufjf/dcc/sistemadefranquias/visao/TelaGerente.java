// br/ufjf/dcc/sistemadefranquias/visao/TelaGerente.java
package br.ufjf.dcc.sistemadefranquias.visao;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import br.ufjf.dcc.sistemadefranquias.controle.Sistema;
import br.ufjf.dcc.sistemadefranquias.excecoes.ValidacaoException;
import br.ufjf.dcc.sistemadefranquias.modelo.Cliente;
import br.ufjf.dcc.sistemadefranquias.modelo.Franquia;
import br.ufjf.dcc.sistemadefranquias.modelo.Gerente;
import br.ufjf.dcc.sistemadefranquias.modelo.Pedido;
import br.ufjf.dcc.sistemadefranquias.modelo.Produto;
import br.ufjf.dcc.sistemadefranquias.modelo.SolicitacaoPedido;

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
        JButton btnVerEstoqueCompleto = new JButton("Ver Estoque Completo");
        
        painel.add(btnCadastrarProduto);
        painel.add(btnEditarProduto);
        painel.add(btnRemoverProduto);
        painel.add(btnVerEstoqueBaixo);
        painel.add(btnVerEstoqueCompleto);
        painel.add(new JLabel("")); // Espaçador
        
        // Seção de Pedidos
        painel.add(new JLabel("<html><b>--- Pedidos e Relatórios ---</b></html>"));
        painel.add(new JLabel(""));
        
        JButton btnVerPedidos = new JButton("Ver Todos os Pedidos");
        JButton btnRelatorioClientes = new JButton("Clientes Mais Recorrentes");
        JButton btnAvaliarSolicitacoes = new JButton("Avaliar Solicitações");
        
        painel.add(btnVerPedidos);
        painel.add(btnRelatorioClientes);
        painel.add(btnAvaliarSolicitacoes);
        painel.add(new JLabel("")); // Espaçador

        JButton btnSair = new JButton("Sair");
        
        // Adicionando actions
        btnCadastrarVendedor.addActionListener(e -> cadastrarVendedor());
        btnRemoverVendedor.addActionListener(e -> removerVendedor());
        btnCadastrarProduto.addActionListener(e -> cadastrarProduto());
        btnEditarProduto.addActionListener(e -> editarProduto());
        btnRemoverProduto.addActionListener(e -> removerProduto());
        btnVerEstoqueBaixo.addActionListener(e -> verEstoqueBaixo());
        btnVerEstoqueCompleto.addActionListener(e -> verEstoqueCompleto());
        btnVerPedidos.addActionListener(e -> verPedidos());
        btnRelatorioClientes.addActionListener(e -> relatorioClientes());
        btnAvaliarSolicitacoes.addActionListener(e -> avaliarSolicitacoes());

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
        JButton btnCancelar = new JButton("Cancelar");
        JButton btnCadastrar = new JButton("Cadastrar");

        btnCancelar.addActionListener(e -> dialog.dispose());

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
        dialog.add(btnCancelar); dialog.add(btnCadastrar);
        
        dialog.setVisible(true);
        
    }
    
    private void removerVendedor() {
        if (franquia.getVendedores().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Não há vendedores para remover.", "Aviso", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        String[] nomesVendedores = franquia.getVendedores().values().stream()
            .map(v -> v.getNome() + " (" + v.getCpf() + ")")
            .toArray(String[]::new);
            
        String vendedorSelecionado = (String) JOptionPane.showInputDialog(this, 
            "Selecione o vendedor a ser removido:", "Remover Vendedor", 
            JOptionPane.QUESTION_MESSAGE, null, nomesVendedores, nomesVendedores[0]);

        if (vendedorSelecionado != null) {
            String cpf = vendedorSelecionado.split("\\(")[1].replace(")", "");
            int confirm = JOptionPane.showConfirmDialog(this, 
                "Tem certeza que deseja remover este vendedor?", "Confirmação", JOptionPane.YES_NO_OPTION);
            
            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    sistema.removerUsuario(cpf);
                    JOptionPane.showMessageDialog(this, "Vendedor removido com sucesso.");
                } catch (ValidacaoException e) {
                    JOptionPane.showMessageDialog(this, e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }
    
    private void cadastrarProduto() {
        JDialog dialog = new JDialog(this, "Novo Produto", true);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(this);
        
        JPanel painelPrincipal = new JPanel(new GridLayout(6, 2, 10, 10));
        painelPrincipal.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JTextField campoNome = new JTextField();
        JTextField campoDescricao = new JTextField();
        JTextField campoPreco = new JTextField();
        JTextField campoQuantidade = new JTextField();
        
        JButton btnCancelar = new JButton("Cancelar");
        JButton btnCadastrar = new JButton("Cadastrar");
        
        btnCancelar.addActionListener(e -> dialog.dispose());

        btnCadastrar.addActionListener(e -> {
            try {
                String nome = campoNome.getText().trim();
                String descricao = campoDescricao.getText().trim();
                String precoStr = campoPreco.getText().trim();
                String quantidadeStr = campoQuantidade.getText().trim();
                
                if (nome.isEmpty() || descricao.isEmpty() || precoStr.isEmpty() || quantidadeStr.isEmpty()) {
                    throw new ValidacaoException("Todos os campos são obrigatórios.");
                }
                
                double preco = Double.parseDouble(precoStr);
                int quantidade = Integer.parseInt(quantidadeStr);
                
                if (preco <= 0) {
                    throw new ValidacaoException("O preço deve ser maior que zero.");
                }
                
                if (quantidade < 0) {
                    throw new ValidacaoException("A quantidade não pode ser negativa.");
                }
                
                Produto produto = new Produto(franquia.getEstoque().size() + 1, nome, descricao, preco, quantidade);
                sistema.adicionarProduto(franquia, produto);
                JOptionPane.showMessageDialog(dialog, "Produto cadastrado com sucesso!");
                dialog.dispose();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Preço e quantidade devem ser números válidos.", "Erro", JOptionPane.ERROR_MESSAGE);
            } catch (ValidacaoException ex) {
                JOptionPane.showMessageDialog(dialog, ex.getMessage(), "Erro de Validação", JOptionPane.ERROR_MESSAGE);
            }
        });

        painelPrincipal.add(new JLabel("Nome:")); painelPrincipal.add(campoNome);
        painelPrincipal.add(new JLabel("Descrição:")); painelPrincipal.add(campoDescricao);
        painelPrincipal.add(new JLabel("Preço:")); painelPrincipal.add(campoPreco);
        painelPrincipal.add(new JLabel("Quantidade:")); painelPrincipal.add(campoQuantidade);
        painelPrincipal.add(new JLabel("")); painelPrincipal.add(new JLabel(""));
        painelPrincipal.add(btnCancelar); painelPrincipal.add(btnCadastrar);

        dialog.add(painelPrincipal);
        dialog.setVisible(true);
    }
    
    private void editarProduto() {
        if (franquia.getEstoque().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Não há produtos para editar.", "Aviso", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        String[] nomesProdutos = franquia.getEstoque().values().stream()
            .map(p -> p.getNome() + " (ID: " + p.getId() + ")")
            .toArray(String[]::new);
            
        String produtoSelecionado = (String) JOptionPane.showInputDialog(this, 
            "Selecione o produto a ser editado:", "Editar Produto", 
            JOptionPane.QUESTION_MESSAGE, null, nomesProdutos, nomesProdutos[0]);

        if (produtoSelecionado != null) {
            int id = Integer.parseInt(produtoSelecionado.split("ID: ")[1].replace(")", ""));
            Produto produto = franquia.getEstoque().get(id);
            
            JDialog dialog = new JDialog(this, "Editar Produto", true);
            dialog.setSize(400, 300);
            dialog.setLocationRelativeTo(this);
            
            JPanel painelPrincipal = new JPanel(new GridLayout(6, 2, 10, 10));
            painelPrincipal.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

            JTextField campoNome = new JTextField(produto.getNome());
            JTextField campoDescricao = new JTextField(produto.getDescricao());
            JTextField campoPreco = new JTextField(String.valueOf(produto.getPreco()));
            JTextField campoQuantidade = new JTextField(String.valueOf(produto.getQuantidadeEmEstoque()));
            
            JButton btnCancelar = new JButton("Cancelar");
            JButton btnSalvar = new JButton("Salvar");
            
            btnCancelar.addActionListener(e -> dialog.dispose());

            btnSalvar.addActionListener(e -> {
                try {
                    String nome = campoNome.getText().trim();
                    String descricao = campoDescricao.getText().trim();
                    String precoStr = campoPreco.getText().trim();
                    String quantidadeStr = campoQuantidade.getText().trim();
                    
                    if (nome.isEmpty() || descricao.isEmpty() || precoStr.isEmpty() || quantidadeStr.isEmpty()) {
                        throw new ValidacaoException("Todos os campos são obrigatórios.");
                    }
                    
                    double preco = Double.parseDouble(precoStr);
                    int quantidade = Integer.parseInt(quantidadeStr);
                    
                    if (preco <= 0) {
                        throw new ValidacaoException("O preço deve ser maior que zero.");
                    }
                    
                    if (quantidade < 0) {
                        throw new ValidacaoException("A quantidade não pode ser negativa.");
                    }
                    
                    produto.setNome(nome);
                    produto.setDescricao(descricao);
                    produto.setPreco(preco);
                    produto.setQuantidadeEmEstoque(quantidade);
                    
                    JOptionPane.showMessageDialog(dialog, "Produto editado com sucesso!");
                    dialog.dispose();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(dialog, "Preço e quantidade devem ser números válidos.", "Erro", JOptionPane.ERROR_MESSAGE);
                } catch (ValidacaoException ex) {
                    JOptionPane.showMessageDialog(dialog, ex.getMessage(), "Erro de Validação", JOptionPane.ERROR_MESSAGE);
                }
            });

            painelPrincipal.add(new JLabel("Nome:")); painelPrincipal.add(campoNome);
            painelPrincipal.add(new JLabel("Descrição:")); painelPrincipal.add(campoDescricao);
            painelPrincipal.add(new JLabel("Preço:")); painelPrincipal.add(campoPreco);
            painelPrincipal.add(new JLabel("Quantidade:")); painelPrincipal.add(campoQuantidade);
            painelPrincipal.add(new JLabel("")); painelPrincipal.add(new JLabel(""));
            painelPrincipal.add(btnCancelar); painelPrincipal.add(btnSalvar);

            dialog.add(painelPrincipal);
            dialog.setVisible(true);
        }
    }
    
    private void removerProduto() {
        if (franquia.getEstoque().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Não há produtos para remover.", "Aviso", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        String[] nomesProdutos = franquia.getEstoque().values().stream()
            .map(p -> p.getNome() + " (ID: " + p.getId() + ")")
            .toArray(String[]::new);
            
        String produtoSelecionado = (String) JOptionPane.showInputDialog(this, 
            "Selecione o produto a ser removido:", "Remover Produto", 
            JOptionPane.QUESTION_MESSAGE, null, nomesProdutos, nomesProdutos[0]);

        if (produtoSelecionado != null) {
            int id = Integer.parseInt(produtoSelecionado.split("ID: ")[1].replace(")", ""));
            int confirm = JOptionPane.showConfirmDialog(this, 
                "Tem certeza que deseja remover este produto?", "Confirmação", JOptionPane.YES_NO_OPTION);
            
            if (confirm == JOptionPane.YES_OPTION) {
                franquia.getEstoque().remove(id);
                JOptionPane.showMessageDialog(this, "Produto removido com sucesso.");
            }
        }
    }
    
    private void verEstoqueBaixo() {
        JDialog dialog = new JDialog(this, "Produtos com Estoque Baixo", true);
        dialog.setSize(500, 400);
        dialog.setLocationRelativeTo(this);
        
        JTextArea areaTexto = new JTextArea();
        areaTexto.setEditable(false);
        areaTexto.setFont(new Font("Monospaced", Font.PLAIN, 12));
        
        StringBuilder sb = new StringBuilder();
        sb.append("Produtos com estoque baixo (≤ 10 unidades):\n");
        sb.append("================================================\n\n");
        
        boolean temEstoqueBaixo = false;
        for (Produto produto : franquia.getEstoque().values()) {
            if (produto.getQuantidadeEmEstoque() <= 10) {
                sb.append(String.format("ID: %d\n", produto.getId()));
                sb.append(String.format("Nome: %s\n", produto.getNome()));
                sb.append(String.format("Quantidade: %d unidades\n", produto.getQuantidadeEmEstoque()));
                sb.append("----------------------------------------\n");
                temEstoqueBaixo = true;
            }
        }
        
        if (!temEstoqueBaixo) {
            sb.append("Todos os produtos estão com estoque adequado!");
        }
        
        areaTexto.setText(sb.toString());
        dialog.add(new JScrollPane(areaTexto));
        dialog.setVisible(true);
    }
    
    private void verEstoqueCompleto() {
        JDialog dialog = new JDialog(this, "Estoque Completo - " + franquia.getNome(), true);
        dialog.setSize(700, 500);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());
        
        JTextArea areaTexto = new JTextArea();
        areaTexto.setEditable(false);
        areaTexto.setFont(new Font("Monospaced", Font.PLAIN, 12));
        
        StringBuilder sb = new StringBuilder();
        sb.append("RELATÓRIO COMPLETO DO ESTOQUE\n");
        sb.append("Franquia: ").append(franquia.getNome()).append("\n");
        sb.append("=".repeat(60)).append("\n\n");
        
        if (franquia.getEstoque().isEmpty()) {
            sb.append("Nenhum produto cadastrado no estoque.\n");
        } else {
            double valorTotalEstoque = 0.0;
            int totalItens = 0;
            
            sb.append(String.format("%-4s %-25s %-8s %-12s %-15s %s\n", 
                     "ID", "PRODUTO", "QTD", "PREÇO (R$)", "VALOR TOTAL", "STATUS"));
            sb.append("-".repeat(80)).append("\n");
            
            for (Produto produto : franquia.getEstoque().values()) {
                double valorTotalProduto = produto.getPreco() * produto.getQuantidadeEmEstoque();
                valorTotalEstoque += valorTotalProduto;
                totalItens += produto.getQuantidadeEmEstoque();
                
                String status;
                if (produto.getQuantidadeEmEstoque() == 0) {
                    status = "SEM ESTOQUE";
                } else if (produto.getQuantidadeEmEstoque() <= 5) {
                    status = "CRÍTICO";
                } else if (produto.getQuantidadeEmEstoque() <= 10) {
                    status = "BAIXO";
                } else {
                    status = "OK";
                }
                
                sb.append(String.format("%-4d %-25s %-8d R$ %-9.2f R$ %-12.2f %s\n",
                         produto.getId(),
                         produto.getNome().length() > 25 ? produto.getNome().substring(0, 22) + "..." : produto.getNome(),
                         produto.getQuantidadeEmEstoque(),
                         produto.getPreco(),
                         valorTotalProduto,
                         status));
            }
            
            sb.append("-".repeat(80)).append("\n");
            sb.append(String.format("TOTAL: %d produtos diferentes | %d itens | Valor total: R$ %.2f\n", 
                     franquia.getEstoque().size(), totalItens, valorTotalEstoque));
            
            // Estatísticas adicionais
            sb.append("\n").append("=".repeat(60)).append("\n");
            sb.append("ESTATÍSTICAS:\n");
            
            long produtosCriticos = franquia.getEstoque().values().stream()
                .filter(p -> p.getQuantidadeEmEstoque() <= 5)
                .count();
            long produtosBaixo = franquia.getEstoque().values().stream()
                .filter(p -> p.getQuantidadeEmEstoque() > 5 && p.getQuantidadeEmEstoque() <= 10)
                .count();
            long produtosOk = franquia.getEstoque().values().stream()
                .filter(p -> p.getQuantidadeEmEstoque() > 10)
                .count();
                
            sb.append(String.format("• Produtos em estado crítico (≤5): %d\n", produtosCriticos));
            sb.append(String.format("• Produtos com estoque baixo (6-10): %d\n", produtosBaixo));
            sb.append(String.format("• Produtos com estoque adequado (>10): %d\n", produtosOk));
        }
        
        areaTexto.setText(sb.toString());
        
        // Painel superior com título
        JPanel painelTitulo = new JPanel(new BorderLayout());
        JLabel lblTitulo = new JLabel("📦 Relatório Completo do Estoque", JLabel.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 16));
        painelTitulo.add(lblTitulo, BorderLayout.CENTER);
        painelTitulo.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Painel de botões
        JPanel painelBotoes = new JPanel();
        JButton btnFechar = new JButton("Fechar");
        btnFechar.addActionListener(e -> dialog.dispose());
        painelBotoes.add(btnFechar);
        
        dialog.add(painelTitulo, BorderLayout.NORTH);
        dialog.add(new JScrollPane(areaTexto), BorderLayout.CENTER);
        dialog.add(painelBotoes, BorderLayout.SOUTH);
        
        dialog.setVisible(true);
    }
    
    private void verPedidos() {
        JDialog dialog = new JDialog(this, "Todos os Pedidos", true);
        dialog.setSize(600, 500);
        dialog.setLocationRelativeTo(this);
        
        JTextArea areaTexto = new JTextArea();
        areaTexto.setEditable(false);
        areaTexto.setFont(new Font("Monospaced", Font.PLAIN, 12));
        
        StringBuilder sb = new StringBuilder();
        sb.append("Pedidos da Franquia: ").append(franquia.getNome()).append("\n");
        sb.append("=================================================\n\n");
        
        if (franquia.getPedidos().isEmpty()) {
            sb.append("Nenhum pedido registrado.");
        } else {
            for (Pedido pedido : franquia.getPedidos().values()) {
                sb.append(String.format("ID: %d\n", pedido.getId()));
                sb.append(String.format("Cliente: %s\n", pedido.getCliente().getNome()));
                sb.append(String.format("Vendedor: %s\n", pedido.getVendedor() != null ? pedido.getVendedor().getNome() : "N/A"));
                sb.append(String.format("Valor Total: R$ %.2f\n", pedido.getValorTotal()));
                sb.append(String.format("Data: %s\n", pedido.getDataPedido()));
                sb.append("----------------------------------------\n");
            }
        }
        
        areaTexto.setText(sb.toString());
        dialog.add(new JScrollPane(areaTexto));
        dialog.setVisible(true);
    }
    
    private void relatorioClientes() {
        JDialog dialog = new JDialog(this, "Clientes Mais Recorrentes", true);
        dialog.setSize(500, 400);
        dialog.setLocationRelativeTo(this);
        
        JTextArea areaTexto = new JTextArea();
        areaTexto.setEditable(false);
        areaTexto.setFont(new Font("Monospaced", Font.PLAIN, 12));
        
        StringBuilder sb = new StringBuilder();
        sb.append("Clientes Mais Recorrentes\n");
        sb.append("=========================\n\n");
        
        if (franquia.getPedidos().isEmpty()) {
            sb.append("Nenhum pedido registrado para análise.");
        } else {
            // Contar pedidos por cliente
            var clientesCount = franquia.getPedidos().values().stream()
                .collect(java.util.stream.Collectors.groupingBy(
                    Pedido::getCliente,
                    java.util.stream.Collectors.counting()
                ));
            
            // Ordenar por quantidade de pedidos (decrescente)
            clientesCount.entrySet().stream()
                .sorted(java.util.Map.Entry.<Cliente, Long>comparingByValue().reversed())
                .forEach(entry -> {
                    Cliente cliente = entry.getKey();
                    Long count = entry.getValue();
                    double valorTotal = franquia.getPedidos().values().stream()
                        .filter(p -> p.getCliente().equals(cliente))
                        .mapToDouble(Pedido::getValorTotal)
                        .sum();
                    
                    sb.append(String.format("Cliente: %s\n", cliente.getNome()));
                    sb.append(String.format("CPF: %s\n", cliente.getCpf()));
                    sb.append(String.format("Total de Pedidos: %d\n", count));
                    sb.append(String.format("Valor Total Gasto: R$ %.2f\n", valorTotal));
                    sb.append("----------------------------------------\n");
                });
        }
        
        areaTexto.setText(sb.toString());
        dialog.add(new JScrollPane(areaTexto));
        dialog.setVisible(true);
    }
    
    private void avaliarSolicitacoes() {
        if (franquia.getSolicitacoesPendentes().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Não há solicitações pendentes.", "Aviso", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        // Criar lista de solicitações para seleção
        String[] solicitacoesList = franquia.getSolicitacoesPendentes().values().stream()
            .map(s -> String.format("ID %d - %s pedido %d (%s)", 
                s.getId(), 
                s.getTipoSolicitacao(), 
                s.getPedido().getId(), 
                s.getVendedor().getNome()))
            .toArray(String[]::new);
            
        String solicitacaoSelecionada = (String) JOptionPane.showInputDialog(this, 
            "Selecione a solicitação para avaliar:", "Avaliar Solicitações", 
            JOptionPane.QUESTION_MESSAGE, null, solicitacoesList, solicitacoesList[0]);

        if (solicitacaoSelecionada != null) {
            int idSolicitacao = Integer.parseInt(solicitacaoSelecionada.split(" ")[1]);
            SolicitacaoPedido solicitacao = franquia.getSolicitacoesPendentes().get(idSolicitacao);
            
            // Mostrar detalhes da solicitação
            mostrarDetalhesSolicitacao(solicitacao);
        }
    }
    
    private void mostrarDetalhesSolicitacao(SolicitacaoPedido solicitacao) {
        JDialog dialog = new JDialog(this, "Detalhes da Solicitação", true);
        dialog.setSize(500, 400);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());
        
        // Painel de informações
        JPanel painelInfo = new JPanel(new GridLayout(0, 1, 5, 5));
        painelInfo.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));
        
        painelInfo.add(new JLabel("<html><b>Tipo de Solicitação:</b> " + solicitacao.getTipoSolicitacao() + "</html>"));
        painelInfo.add(new JLabel("<html><b>Vendedor:</b> " + solicitacao.getVendedor().getNome() + "</html>"));
        painelInfo.add(new JLabel("<html><b>Pedido ID:</b> " + solicitacao.getPedido().getId() + "</html>"));
        painelInfo.add(new JLabel("<html><b>Cliente:</b> " + solicitacao.getPedido().getCliente().getNome() + "</html>"));
        painelInfo.add(new JLabel("<html><b>Valor do Pedido:</b> R$ " + String.format("%.2f", solicitacao.getPedido().getValorTotal()) + "</html>"));
        painelInfo.add(new JLabel("<html><b>Data da Solicitação:</b> " + solicitacao.getDataSolicitacao() + "</html>"));
        
        // Área de justificativa
        JLabel labelJustificativa = new JLabel("Justificativa do Vendedor:");
        JTextArea areaJustificativa = new JTextArea(solicitacao.getJustificativa());
        areaJustificativa.setEditable(false);
        areaJustificativa.setWrapStyleWord(true);
        areaJustificativa.setLineWrap(true);
        areaJustificativa.setBorder(BorderFactory.createLoweredBevelBorder());
        
        JPanel painelJustificativa = new JPanel(new BorderLayout());
        painelJustificativa.add(labelJustificativa, BorderLayout.NORTH);
        painelJustificativa.add(new JScrollPane(areaJustificativa), BorderLayout.CENTER);
        painelJustificativa.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        
        // Campo para comentário do gerente
        JPanel painelComentario = new JPanel(new BorderLayout());
        painelComentario.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        JLabel labelComentario = new JLabel("Comentário do Gerente (opcional):");
        JTextArea areaComentario = new JTextArea(3, 30);
        areaComentario.setWrapStyleWord(true);
        areaComentario.setLineWrap(true);
        areaComentario.setBorder(BorderFactory.createLoweredBevelBorder());
        
        painelComentario.add(labelComentario, BorderLayout.NORTH);
        painelComentario.add(new JScrollPane(areaComentario), BorderLayout.CENTER);
        
        // Painel de botões
        JPanel painelBotoes = new JPanel();
        JButton btnAprovar = new JButton("Aprovar");
        JButton btnRejeitar = new JButton("Rejeitar");
        JButton btnCancelar = new JButton("Cancelar");
        
        btnAprovar.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(dialog, 
                "Tem certeza que deseja APROVAR esta solicitação?", 
                "Confirmação", JOptionPane.YES_NO_OPTION);
            
            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    sistema.aprovarSolicitacaoPedido(franquia, solicitacao.getId(), areaComentario.getText().trim());
                    JOptionPane.showMessageDialog(dialog, "Solicitação aprovada com sucesso!");
                    dialog.dispose();
                } catch (ValidacaoException ex) {
                    JOptionPane.showMessageDialog(dialog, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        
        btnRejeitar.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(dialog, 
                "Tem certeza que deseja REJEITAR esta solicitação?", 
                "Confirmação", JOptionPane.YES_NO_OPTION);
            
            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    sistema.rejeitarSolicitacaoPedido(franquia, solicitacao.getId(), areaComentario.getText().trim());
                    JOptionPane.showMessageDialog(dialog, "Solicitação rejeitada.");
                    dialog.dispose();
                } catch (ValidacaoException ex) {
                    JOptionPane.showMessageDialog(dialog, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        
        btnCancelar.addActionListener(e -> dialog.dispose());
        
        painelBotoes.add(btnAprovar);
        painelBotoes.add(btnRejeitar);
        painelBotoes.add(btnCancelar);
        
        dialog.add(painelInfo, BorderLayout.NORTH);
        dialog.add(painelJustificativa, BorderLayout.CENTER);
        
        // Painel inferior com comentário e botões
        JPanel painelInferior = new JPanel(new BorderLayout());
        painelInferior.add(painelComentario, BorderLayout.CENTER);
        painelInferior.add(painelBotoes, BorderLayout.SOUTH);
        
        dialog.add(painelInferior, BorderLayout.SOUTH);
        
        dialog.setVisible(true);
    }
}