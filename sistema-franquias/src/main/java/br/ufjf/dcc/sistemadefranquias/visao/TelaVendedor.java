// br/ufjf/dcc/sistemadefranquias/visao/TelaVendedor.java
package br.ufjf.dcc.sistemadefranquias.visao;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import br.ufjf.dcc.sistemadefranquias.controle.Sistema;
import br.ufjf.dcc.sistemadefranquias.modelo.Cliente;
import br.ufjf.dcc.sistemadefranquias.modelo.Franquia;
import br.ufjf.dcc.sistemadefranquias.modelo.Pedido;
import br.ufjf.dcc.sistemadefranquias.modelo.Vendedor;

public class TelaVendedor extends JFrame {
    private Sistema sistema;
    private Vendedor vendedor;
    private Franquia franquia;

    public TelaVendedor(Sistema sistema, Vendedor vendedor) {
        this.sistema = sistema;
        this.vendedor = vendedor;
        this.franquia = vendedor.getFranquia();

        setTitle("Painel do Vendedor: " + vendedor.getNome() + " | Franquia: " + franquia.getNome());
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        JPanel painel = new JPanel(new GridLayout(0, 2, 10, 10));
        painel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JButton btnNovoPedido = new JButton("Registrar Novo Pedido");
        JButton btnMeusPedidos = new JButton("Visualizar Meus Pedidos");
        JButton btnEstoque = new JButton("Ver Estoque Disponível");
        JButton btnSolicitacoes = new JButton("Solicitar Alteração/Exclusão");
        JButton btnVerSolicitacoes = new JButton("Ver Status das Solicitações");
        JButton btnAlterarPedidos = new JButton("Alterar Pedidos Aprovados");
        JButton btnSair = new JButton("Sair");

        btnNovoPedido.addActionListener(e -> registrarNovoPedido());
        btnMeusPedidos.addActionListener(e -> visualizarMeusPedidos());
        btnEstoque.addActionListener(e -> visualizarEstoque());
        btnSolicitacoes.addActionListener(e -> criarSolicitacao());
        btnVerSolicitacoes.addActionListener(e -> verStatusSolicitacoes());
        btnAlterarPedidos.addActionListener(e -> alterarPedidosAprovados());

        btnSair.addActionListener(e -> {
            new TelaLogin(sistema);
            dispose();
        });

        painel.add(btnNovoPedido);
        painel.add(btnMeusPedidos);
        painel.add(btnEstoque);
        painel.add(btnSolicitacoes);
        painel.add(btnVerSolicitacoes);
        painel.add(btnAlterarPedidos);
        painel.add(btnSair);

        add(painel);
        setVisible(true);
    }

    private void registrarNovoPedido() {
        JDialog dialog = new JDialog(this, "Registrar Pedido", true);
        dialog.setSize(600, 500);
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
        
        // HashMap para armazenar produtos com quantidades
        java.util.Map<String, Integer> produtosSelecionados = new java.util.HashMap<>();
        
        // Lógica para adicionar produtos com verificação de estoque
        btnAddProduto.addActionListener(e -> {
            String nomeProduto = JOptionPane.showInputDialog(dialog, "Nome do Produto:");
            if(nomeProduto != null && !nomeProduto.isBlank()) {
                // Verificar se o produto existe no estoque
                if (sistema.buscarProdutoPorNome(franquia, nomeProduto) == null) {
                    JOptionPane.showMessageDialog(dialog, "Produto '" + nomeProduto + "' não encontrado no estoque!", 
                                                "Produto Não Encontrado", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                String quantidadeStr = JOptionPane.showInputDialog(dialog, "Quantidade do produto '" + nomeProduto + "':");
                if (quantidadeStr != null && !quantidadeStr.isBlank()) {
                    try {
                        int quantidade = Integer.parseInt(quantidadeStr);
                        if (quantidade <= 0) {
                            JOptionPane.showMessageDialog(dialog, "Quantidade deve ser maior que zero!", 
                                                        "Quantidade Inválida", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                        
                        // Verificar se há estoque suficiente
                        if (!sistema.verificarEstoqueProduto(franquia, nomeProduto, quantidade)) {
                            JOptionPane.showMessageDialog(dialog, "Estoque insuficiente para o produto '" + nomeProduto + "'!", 
                                                        "Estoque Insuficiente", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                        
                        // Adicionar à lista
                        produtosSelecionados.put(nomeProduto, quantidade);
                        modeloLista.addElement(nomeProduto + " (Qtd: " + quantidade + ")");
                        
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(dialog, "Quantidade deve ser um número válido!", 
                                                    "Quantidade Inválida", JOptionPane.ERROR_MESSAGE);
                    }
                }
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
                // Validar campos obrigatórios
                if (campoNomeCliente.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(dialog, "Nome do cliente é obrigatório!", "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (campoCpfCliente.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(dialog, "CPF do cliente é obrigatório!", "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (produtosSelecionados.isEmpty()) {
                    JOptionPane.showMessageDialog(dialog, "Deve haver pelo menos um produto no pedido!", "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                Cliente cliente = new Cliente(1, campoNomeCliente.getText(), campoCpfCliente.getText(), "");
                
                // Calcular valor total baseado nos preços reais dos produtos
                double valorTotal = 0.0;
                java.util.Map<br.ufjf.dcc.sistemadefranquias.modelo.Produto, Integer> produtosPedido = new java.util.HashMap<>();
                
                for (java.util.Map.Entry<String, Integer> entry : produtosSelecionados.entrySet()) {
                    String nomeProduto = entry.getKey();
                    int quantidade = entry.getValue();
                    
                    br.ufjf.dcc.sistemadefranquias.modelo.Produto produto = sistema.buscarProdutoPorNome(franquia, nomeProduto);
                    if (produto != null) {
                        // Verificar estoque novamente antes de confirmar
                        if (!sistema.verificarEstoqueProduto(franquia, nomeProduto, quantidade)) {
                            JOptionPane.showMessageDialog(dialog, "Estoque insuficiente para o produto '" + nomeProduto + "'!", 
                                                        "Estoque Insuficiente", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                        
                        valorTotal += produto.getPreco() * quantidade;
                        produtosPedido.put(produto, quantidade);
                        
                        // Reduzir estoque
                        sistema.reduzirEstoqueProduto(franquia, nomeProduto, quantidade);
                    }
                }
                
                Pedido novoPedido = new Pedido(sistema.gerarNovoIdPedido(), new Date(), cliente, valorTotal, "Pendente");
                novoPedido.setVendedor(vendedor);
                novoPedido.setProdutosPedidos(produtosPedido);

                sistema.registrarPedido(franquia, novoPedido);
                JOptionPane.showMessageDialog(dialog, "Pedido registrado com sucesso!\nValor total: R$ " + String.format("%.2f", valorTotal));
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
                sb.append("Status: ").append(p.getStatus());
                
                // Indicador especial para pedidos em alteração
                if ("EM ALTERAÇÃO".equals(p.getStatus())) {
                    sb.append(" ⚠️ [CLIQUE EM 'ALTERAR PEDIDOS APROVADOS']");
                }
                
                sb.append("\n");
                sb.append("-----------------------------\n");
            });

        if(sb.length() == 0) {
            sb.append("Você ainda não registrou nenhum pedido.");
        }

        areaTexto.setText(sb.toString());
        dialog.add(new JScrollPane(areaTexto));
        dialog.setVisible(true);
    }
    
    private void criarSolicitacao() {
        // Primeiro, listar os pedidos do vendedor
        var meusPedidos = franquia.getPedidos().values().stream()
            .filter(p -> p.getVendedor() != null && p.getVendedor().equals(this.vendedor))
            .toArray(Pedido[]::new);
            
        if (meusPedidos.length == 0) {
            JOptionPane.showMessageDialog(this, "Você não possui pedidos para solicitar alteração/exclusão.", "Aviso", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        // Selecionar pedido
        String[] opcoesPedidos = new String[meusPedidos.length];
        for (int i = 0; i < meusPedidos.length; i++) {
            Pedido p = meusPedidos[i];
            opcoesPedidos[i] = String.format("Pedido %d - %s - R$ %.2f", 
                p.getId(), p.getCliente().getNome(), p.getValorTotal());
        }
        
        String pedidoSelecionado = (String) JOptionPane.showInputDialog(this, 
            "Selecione o pedido:", "Criar Solicitação", 
            JOptionPane.QUESTION_MESSAGE, null, opcoesPedidos, opcoesPedidos[0]);
            
        if (pedidoSelecionado == null) {
            return;
        }
        
        int indicePedido = -1;
        for (int i = 0; i < opcoesPedidos.length; i++) {
            if (opcoesPedidos[i].equals(pedidoSelecionado)) {
                indicePedido = i;
                break;
            }
        }
        
        Pedido pedido = meusPedidos[indicePedido];
        
        // Selecionar tipo de solicitação
        String[] tiposSolicitacao = {"ALTERAR", "EXCLUIR"};
        String tipoSelecionado = (String) JOptionPane.showInputDialog(this, 
            "Tipo de solicitação:", "Criar Solicitação", 
            JOptionPane.QUESTION_MESSAGE, null, tiposSolicitacao, tiposSolicitacao[0]);
            
        if (tipoSelecionado == null) {
            return;
        }
        
        // Solicitar justificativa
        String justificativa = JOptionPane.showInputDialog(this, 
            "Digite a justificativa para esta solicitação:", 
            "Justificativa", JOptionPane.QUESTION_MESSAGE);
            
        if (justificativa == null || justificativa.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Justificativa é obrigatória.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Criar a solicitação
        try {
            sistema.criarSolicitacaoPedido(franquia, vendedor, pedido, tipoSelecionado, justificativa.trim());
            JOptionPane.showMessageDialog(this, "Solicitação criada com sucesso! Aguarde a avaliação do gerente.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao criar solicitação: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void verStatusSolicitacoes() {
        JDialog dialog = new JDialog(this, "Status das Minhas Solicitações", true);
        dialog.setSize(600, 400);
        dialog.setLocationRelativeTo(this);
        
        JTextArea areaTexto = new JTextArea();
        areaTexto.setEditable(false);
        areaTexto.setFont(new Font("Monospaced", Font.PLAIN, 12));
        
        StringBuilder sb = new StringBuilder();
        sb.append("Status das Solicitações de ").append(vendedor.getNome()).append("\n");
        sb.append("================================================\n\n");
        
        // Verificar solicitações pendentes
        boolean temSolicitacoes = false;
        for (var solicitacao : franquia.getSolicitacoesPendentes().values()) {
            if (solicitacao.getVendedor().equals(this.vendedor)) {
                sb.append("--- SOLICITAÇÃO PENDENTE ---\n");
                sb.append(String.format("ID: %d\n", solicitacao.getId()));
                sb.append(String.format("Tipo: %s\n", solicitacao.getTipoSolicitacao()));
                sb.append(String.format("Pedido: %d - %s\n", solicitacao.getPedido().getId(), solicitacao.getPedido().getCliente().getNome()));
                sb.append(String.format("Data da Solicitação: %s\n", solicitacao.getDataSolicitacao()));
                sb.append(String.format("Status: %s\n", solicitacao.getStatus()));
                sb.append("Justificativa: ").append(solicitacao.getJustificativa()).append("\n");
                sb.append("--------------------------------\n\n");
                temSolicitacoes = true;
            }
        }
        
        // Verificar histórico de solicitações processadas
        for (var solicitacao : franquia.getHistoricoSolicitacoes().values()) {
            if (solicitacao.getVendedor().equals(this.vendedor)) {
                sb.append("--- SOLICITAÇÃO PROCESSADA ---\n");
                sb.append(String.format("ID: %d\n", solicitacao.getId()));
                sb.append(String.format("Tipo: %s\n", solicitacao.getTipoSolicitacao()));
                sb.append(String.format("Pedido: %d - %s\n", solicitacao.getPedido().getId(), solicitacao.getPedido().getCliente().getNome()));
                sb.append(String.format("Data da Solicitação: %s\n", solicitacao.getDataSolicitacao()));
                sb.append(String.format("Status: %s\n", solicitacao.getStatus()));
                if (solicitacao.getDataResposta() != null) {
                    sb.append(String.format("Data da Resposta: %s\n", solicitacao.getDataResposta()));
                }
                sb.append("Justificativa: ").append(solicitacao.getJustificativa()).append("\n");
                if (solicitacao.getComentarioGerente() != null && !solicitacao.getComentarioGerente().trim().isEmpty()) {
                    sb.append("Comentário do Gerente: ").append(solicitacao.getComentarioGerente()).append("\n");
                }
                sb.append("--------------------------------\n\n");
                temSolicitacoes = true;
            }
        }
        
        if (!temSolicitacoes) {
            sb.append("Você não possui solicitações em andamento.");
        }
        
        areaTexto.setText(sb.toString());
        dialog.add(new JScrollPane(areaTexto));
        dialog.setVisible(true);
    }
    
    private void alterarPedidosAprovados() {
        // Buscar pedidos do vendedor com status "EM ALTERAÇÃO"
        var pedidosParaAlterar = franquia.getPedidos().values().stream()
            .filter(p -> p.getVendedor() != null && 
                        p.getVendedor().equals(this.vendedor) && 
                        "EM ALTERAÇÃO".equals(p.getStatus()))
            .toArray(Pedido[]::new);
            
        if (pedidosParaAlterar.length == 0) {
            JOptionPane.showMessageDialog(this, 
                "Não há pedidos aprovados para alteração.", 
                "Aviso", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        // Selecionar pedido para alterar
        String[] opcoesPedidos = new String[pedidosParaAlterar.length];
        for (int i = 0; i < pedidosParaAlterar.length; i++) {
            Pedido p = pedidosParaAlterar[i];
            opcoesPedidos[i] = String.format("Pedido %d - %s - R$ %.2f", 
                p.getId(), p.getCliente().getNome(), p.getValorTotal());
        }
        
        String pedidoSelecionado = (String) JOptionPane.showInputDialog(this, 
            "Selecione o pedido para alterar:", "Alterar Pedido", 
            JOptionPane.QUESTION_MESSAGE, null, opcoesPedidos, opcoesPedidos[0]);
            
        if (pedidoSelecionado == null) {
            return;
        }
        
        int indicePedido = -1;
        for (int i = 0; i < opcoesPedidos.length; i++) {
            if (opcoesPedidos[i].equals(pedidoSelecionado)) {
                indicePedido = i;
                break;
            }
        }
        
        Pedido pedido = pedidosParaAlterar[indicePedido];
        
        // Abrir interface de alteração
        abrirInterfaceAlteracaoPedido(pedido);
    }
    
    private void abrirInterfaceAlteracaoPedido(Pedido pedido) {
        JDialog dialog = new JDialog(this, "Alterar Pedido #" + pedido.getId(), true);
        dialog.setSize(600, 500);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout(10, 10));
        
        // Painel de informações atuais
        JPanel painelInfo = new JPanel(new GridLayout(0, 2, 10, 10));
        painelInfo.setBorder(BorderFactory.createTitledBorder("Informações Atuais"));
        
        painelInfo.add(new JLabel("ID do Pedido:"));
        painelInfo.add(new JLabel(String.valueOf(pedido.getId())));
        
        painelInfo.add(new JLabel("Cliente:"));
        JTextField campoCliente = new JTextField(pedido.getCliente().getNome());
        painelInfo.add(campoCliente);
        
        painelInfo.add(new JLabel("Status Atual:"));
        painelInfo.add(new JLabel(pedido.getStatus()));
        
        painelInfo.add(new JLabel("Valor Atual:"));
        JTextField campoValor = new JTextField(String.format("%.2f", pedido.getValorTotal()));
        painelInfo.add(campoValor);
        
        // Painel de novos dados
        JPanel painelNovos = new JPanel(new GridLayout(0, 2, 10, 10));
        painelNovos.setBorder(BorderFactory.createTitledBorder("Novos Dados"));
        
        painelNovos.add(new JLabel("Novo Status:"));
        JComboBox<String> comboStatus = new JComboBox<>(new String[]{
            "Pendente", "Confirmado", "Em Preparação", "Entregue", "Cancelado"
        });
        comboStatus.setSelectedItem(pedido.getStatus());
        painelNovos.add(comboStatus);
        
        painelNovos.add(new JLabel("Observações:"));
        JTextArea areaObservacoes = new JTextArea(3, 20);
        areaObservacoes.setBorder(BorderFactory.createLoweredBevelBorder());
        areaObservacoes.setWrapStyleWord(true);
        areaObservacoes.setLineWrap(true);
        painelNovos.add(new JScrollPane(areaObservacoes));
        
        // Painel de botões
        JPanel painelBotoes = new JPanel();
        JButton btnSalvarAlteracoes = new JButton("Salvar Alterações");
        JButton btnCancelar = new JButton("Cancelar");
        
        btnSalvarAlteracoes.addActionListener(e -> {
            try {
                // Validar dados
                String novoValorStr = campoValor.getText().trim();
                String novoCliente = campoCliente.getText().trim();
                String novoStatus = (String) comboStatus.getSelectedItem();
                
                if (novoCliente.isEmpty()) {
                    throw new Exception("Nome do cliente não pode estar vazio.");
                }
                
                double novoValor = Double.parseDouble(novoValorStr);
                if (novoValor <= 0) {
                    throw new Exception("Valor deve ser maior que zero.");
                }
                
                // Aplicar alterações
                pedido.getCliente().setNome(novoCliente);
                pedido.setValorTotal(novoValor);
                
                // Se finalizar alteração, mudar status para algo definitivo
                if (!"EM ALTERAÇÃO".equals(novoStatus)) {
                    // Status foi alterado para algo diferente de "EM ALTERAÇÃO"
                    // Usar o sistema para finalizar a alteração
                    sistema.finalizarAlteracaoPedido(franquia, pedido, novoStatus);
                } else {
                    // Manter em alteração
                    pedido.setStatus(novoStatus);
                }
                
                JOptionPane.showMessageDialog(dialog, 
                    "Pedido alterado com sucesso!\n" +
                    "Cliente: " + novoCliente + "\n" +
                    "Valor: R$ " + String.format("%.2f", novoValor) + "\n" +
                    "Status: " + novoStatus);
                    
                dialog.dispose();
                
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, 
                    "Valor deve ser um número válido.", "Erro", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, 
                    ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        btnCancelar.addActionListener(e -> dialog.dispose());
        
        painelBotoes.add(btnSalvarAlteracoes);
        painelBotoes.add(btnCancelar);
        
        // Montar dialog
        JPanel painelSuperior = new JPanel(new GridLayout(1, 2, 10, 10));
        painelSuperior.add(painelInfo);
        painelSuperior.add(painelNovos);
        
        dialog.add(painelSuperior, BorderLayout.CENTER);
        dialog.add(painelBotoes, BorderLayout.SOUTH);
        
        dialog.setVisible(true);
    }
    
    private void visualizarEstoque() {
        JDialog dialog = new JDialog(this, "Estoque Disponível - " + franquia.getNome(), true);
        dialog.setSize(600, 400);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());
        
        // Criar modelo da lista com informações detalhadas dos produtos
        DefaultListModel<String> modeloLista = new DefaultListModel<>();
        
        if (franquia.getEstoque().isEmpty()) {
            modeloLista.addElement("Nenhum produto disponível no estoque.");
        } else {
            for (br.ufjf.dcc.sistemadefranquias.modelo.Produto produto : franquia.getEstoque().values()) {
                String info = String.format("%s - R$ %.2f (Estoque: %d unidades)", 
                    produto.getNome(), 
                    produto.getPreco(), 
                    produto.getQuantidadeEmEstoque());
                modeloLista.addElement(info);
            }
        }
        
        JList<String> listaEstoque = new JList<>(modeloLista);
        JScrollPane scrollPane = new JScrollPane(listaEstoque);
        
        // Painel de informações
        JPanel painelInfo = new JPanel(new BorderLayout());
        JLabel lblTitulo = new JLabel("Produtos Disponíveis no Estoque", JLabel.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 16));
        painelInfo.add(lblTitulo, BorderLayout.NORTH);
        painelInfo.add(scrollPane, BorderLayout.CENTER);
        
        // Botão de fechar
        JPanel painelBotoes = new JPanel();
        JButton btnFechar = new JButton("Fechar");
        btnFechar.addActionListener(e -> dialog.dispose());
        painelBotoes.add(btnFechar);
        
        dialog.add(painelInfo, BorderLayout.CENTER);
        dialog.add(painelBotoes, BorderLayout.SOUTH);
        
        dialog.setVisible(true);
    }
}