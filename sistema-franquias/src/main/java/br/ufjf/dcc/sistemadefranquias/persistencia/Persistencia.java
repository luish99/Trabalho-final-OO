// br/ufjf/dcc/sistemadefranquias/persistencia/Persistencia.java
package br.ufjf.dcc.sistemadefranquias.persistencia;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import br.ufjf.dcc.sistemadefranquias.controle.Sistema;
import br.ufjf.dcc.sistemadefranquias.modelo.Cliente;
import br.ufjf.dcc.sistemadefranquias.modelo.Dono;
import br.ufjf.dcc.sistemadefranquias.modelo.Franquia;
import br.ufjf.dcc.sistemadefranquias.modelo.Gerente;
import br.ufjf.dcc.sistemadefranquias.modelo.Pedido;
import br.ufjf.dcc.sistemadefranquias.modelo.Produto;
import br.ufjf.dcc.sistemadefranquias.modelo.Usuario;
import br.ufjf.dcc.sistemadefranquias.modelo.Vendedor;

public class Persistencia {

    public static final String ARQUIVO_DADOS = "dados_franquia.txt"; // Mudamos para .txt para indicar que é texto
    private static final String DELIMITADOR = ";";

    // MÉTODO PARA SALVAR OS DADOS EM FORMATO TEXTO
    public static void salvar(Sistema sistema) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(ARQUIVO_DADOS))) {
            
            // --- SALVAR USUÁRIOS ---
            writer.println("---USUARIOS---");
            for (Usuario u : sistema.getUsuarios().values()) {
                String tipo = "";
                String franquiaRef = ""; // Referência à franquia para Gerentes e Vendedores
                if (u instanceof Dono) tipo = "Dono";
                if (u instanceof Gerente) {
                    tipo = "Gerente";
                    Gerente g = (Gerente) u;
                    // Salva o ID da franquia que ele gerencia, ou -1 se não tiver
                    franquiaRef = (g.getFranquiaGerenciada() != null) ? String.valueOf(g.getFranquiaGerenciada().getId()) : "-1";
                }
                if (u instanceof Vendedor) {
                    tipo = "Vendedor";
                    Vendedor v = (Vendedor) u;
                    franquiaRef = String.valueOf(v.getFranquia().getId());
                }
                
                // Formato: TIPO;CPF;NOME;EMAIL;SENHA;FRANQUIA_ID
                writer.println(tipo + DELIMITADOR + u.getCpf() + DELIMITADOR + u.getNome() + DELIMITADOR + u.getEmail() + DELIMITADOR + u.getSenha() + DELIMITADOR + franquiaRef);
            }

            // --- SALVAR FRANQUIAS ---
            writer.println("---FRANQUIAS---");
            for (Franquia f : sistema.getFranquias().values()) {
                 // Formato: ID;NOME;GERENTE_CPF
                 String gerenteCpf = (f.getGerente() != null) ? f.getGerente().getCpf() : "null";
                 writer.println(f.getId() + DELIMITADOR + f.getNome() + DELIMITADOR + gerenteCpf);
            }
            
            // --- SALVAR CONFIGURAÇÕES DO SISTEMA ---
            writer.println("---SISTEMA---");
            writer.println("proximoIdFranquia" + DELIMITADOR + sistema.getProximoIdFranquia());
            writer.println("proximoIdPedido" + DELIMITADOR + sistema.getProximoIdPedido());
            
            // --- SALVAR PRODUTOS ---
            writer.println("---PRODUTOS---");
            System.out.println("Salvando produtos...");
            int contadorProdutos = 0;
            for (Franquia f : sistema.getFranquias().values()) {
                for (Produto p : f.getEstoque().values()) {
                    // Formato: FRANQUIA_ID;PRODUTO_ID;NOME;DESCRICAO;PRECO;QUANTIDADE
                    writer.println(f.getId() + DELIMITADOR + p.getId() + DELIMITADOR + p.getNome() + DELIMITADOR + 
                                 p.getDescricao() + DELIMITADOR + p.getPreco() + DELIMITADOR + p.getQuantidadeEmEstoque());
                    contadorProdutos++;
                }
            }
            System.out.println("Total de produtos salvos: " + contadorProdutos);
            
            // --- SALVAR CLIENTES (únicos) PRIMEIRO ---
            writer.println("---CLIENTES---");
            System.out.println("Salvando clientes...");
            java.util.Set<Integer> clientesSalvos = new java.util.HashSet<>();
            int contadorClientes = 0;
            for (Franquia f : sistema.getFranquias().values()) {
                for (Pedido pedido : f.getPedidos().values()) {
                    Cliente cliente = pedido.getCliente();
                    // Evitar duplicatas de clientes
                    if (!clientesSalvos.contains(cliente.getId())) {
                        // Formato: ID;NOME;CPF;EMAIL
                        writer.println(cliente.getId() + DELIMITADOR + cliente.getNome() + DELIMITADOR + 
                                     cliente.getCpf() + DELIMITADOR + cliente.getEmail());
                        clientesSalvos.add(cliente.getId());
                        contadorClientes++;
                    }
                }
            }
            System.out.println("Total de clientes salvos: " + contadorClientes);
            
            // --- SALVAR PEDIDOS DEPOIS ---
            writer.println("---PEDIDOS---");
            System.out.println("Salvando pedidos...");
            int contadorPedidos = 0;
            for (Franquia f : sistema.getFranquias().values()) {
                for (Pedido pedido : f.getPedidos().values()) {
                    // Formato: FRANQUIA_ID;PEDIDO_ID;DATA;CLIENTE_ID;VALOR_TOTAL;STATUS;VENDEDOR_CPF
                    String vendedorCpf = (pedido.getVendedor() != null) ? pedido.getVendedor().getCpf() : "null";
                    writer.println(f.getId() + DELIMITADOR + pedido.getId() + DELIMITADOR + pedido.getDataPedido().getTime() + DELIMITADOR + 
                                 pedido.getCliente().getId() + DELIMITADOR + pedido.getValorTotal() + DELIMITADOR + 
                                 pedido.getStatus() + DELIMITADOR + vendedorCpf);
                    contadorPedidos++;
                }
            }
            System.out.println("Total de pedidos salvos: " + contadorPedidos);

            writer.println("---FIM---");
        }
    }

    // MÉTODO PARA CARREGAR OS DADOS DO ARQUIVO TEXTO
    public static Sistema carregar() throws IOException {
        Sistema sistema = new Sistema();
        File arquivo = new File(ARQUIVO_DADOS);
        if (!arquivo.exists()) {
            return sistema; // Retorna um sistema novo se o arquivo não existe
        }

        // Mapas temporários para guardar as referências que serão ligadas depois
        Map<String, String> gerenteParaFranquia = new HashMap<>(); // gerente_cpf -> franquia_id
        Map<String, String> vendedorParaFranquia = new HashMap<>(); // vendedor_cpf -> franquia_id
        Map<Integer, String> franquiaParaGerente = new HashMap<>(); // franquia_id -> gerente_cpf
        Map<Integer, Cliente> clientesPorId = new HashMap<>(); // Para reutilizar clientes

        try (BufferedReader reader = new BufferedReader(new FileReader(ARQUIVO_DADOS))) {
            String linha;
            String secaoAtual = "";

            while ((linha = reader.readLine()) != null) {
                if (linha.startsWith("---")) {
                    secaoAtual = linha;
                    continue;
                }

                String[] dados = linha.split(DELIMITADOR);

                if (secaoAtual.equals("---USUARIOS---")) {
                    String tipo = dados[0];
                    String cpf = dados[1];
                    String nome = dados[2];
                    String email = dados[3];
                    String senha = dados[4];
                    String franquiaId = (dados.length > 5) ? dados[5] : "";

                    Usuario u = null;
                    if (tipo.equals("Dono")) {
                        u = new Dono(nome, cpf, email, senha);
                    } else if (tipo.equals("Gerente")) {
                        u = new Gerente(nome, cpf, email, senha, null);
                        if (!franquiaId.equals("-1")) {
                            gerenteParaFranquia.put(cpf, franquiaId);
                        }
                    } else if (tipo.equals("Vendedor")) {
                        // Vendedor precisa da franquia no construtor, então adiamos a criação
                        // ou usamos um construtor que permita setar depois. Para simplificar,
                        // vamos assumir que podemos criar e setar depois.
                        Vendedor v = new Vendedor(nome, cpf, email, senha, null); // Franquia null por enquanto
                        u = v;
                        vendedorParaFranquia.put(cpf, franquiaId);
                    }
                    if (u != null) {
                        sistema.getUsuarios().put(cpf, u);
                    }
                } else if (secaoAtual.equals("---FRANQUIAS---")) {
                    int id = Integer.parseInt(dados[0]);
                    String nome = dados[1];
                    String gerenteCpf = dados[2];

                    Franquia f = new Franquia(id, nome, null); // Endereço null por simplicidade
                    sistema.getFranquias().put(id, f);
                    if (!gerenteCpf.equals("null")) {
                        franquiaParaGerente.put(id, gerenteCpf);
                    }
                    sistema.setProximoIdFranquia(Math.max(sistema.getProximoIdFranquia(), id + 1));
                    
                } else if (secaoAtual.equals("---PRODUTOS---")) {
                    // Formato: FRANQUIA_ID;PRODUTO_ID;NOME;DESCRICAO;PRECO;QUANTIDADE
                    int franquiaId = Integer.parseInt(dados[0]);
                    int produtoId = Integer.parseInt(dados[1]);
                    String nome = dados[2];
                    String descricao = dados[3];
                    double preco = Double.parseDouble(dados[4]);
                    int quantidade = Integer.parseInt(dados[5]);
                    
                    Produto produto = new Produto(produtoId, nome, descricao, preco, quantidade);
                    Franquia franquia = sistema.getFranquias().get(franquiaId);
                    if (franquia != null) {
                        franquia.getEstoque().put(produtoId, produto);
                    }
                    
                } else if (secaoAtual.equals("---SISTEMA---")) {
                    // Formato: chave;valor
                    if (dados.length >= 2) {
                        String chave = dados[0];
                        String valor = dados[1];
                        
                        if ("proximoIdFranquia".equals(chave)) {
                            sistema.setProximoIdFranquia(Integer.parseInt(valor));
                        } else if ("proximoIdPedido".equals(chave)) {
                            sistema.setProximoIdPedido(Integer.parseInt(valor));
                        }
                    }
                    
                } else if (secaoAtual.equals("---CLIENTES---")) {
                    // Formato: ID;NOME;CPF;EMAIL
                    int id = Integer.parseInt(dados[0]);
                    String nome = dados[1];
                    String cpf = dados[2];
                    String email = dados[3];
                    
                    Cliente cliente = new Cliente(id, nome, cpf, email);
                    clientesPorId.put(id, cliente);
                    
                } else if (secaoAtual.equals("---PEDIDOS---")) {
                    // Formato: FRANQUIA_ID;PEDIDO_ID;DATA;CLIENTE_ID;VALOR_TOTAL;STATUS;VENDEDOR_CPF
                    int franquiaId = Integer.parseInt(dados[0]);
                    int pedidoId = Integer.parseInt(dados[1]);
                    long dataTime = Long.parseLong(dados[2]);
                    int clienteId = Integer.parseInt(dados[3]);
                    double valorTotal = Double.parseDouble(dados[4]);
                    String status = dados[5];
                    String vendedorCpf = dados[6];
                    
                    Cliente cliente = clientesPorId.get(clienteId);
                    if (cliente != null) {
                        Pedido pedido = new Pedido(pedidoId, new java.util.Date(dataTime), cliente, valorTotal, status);
                        
                        // Associar vendedor (será feito na segunda passagem)
                        if (!vendedorCpf.equals("null")) {
                            Vendedor vendedor = (Vendedor) sistema.getUsuarios().get(vendedorCpf);
                            if (vendedor != null) {
                                pedido.setVendedor(vendedor);
                            }
                        }
                        
                        Franquia franquia = sistema.getFranquias().get(franquiaId);
                        if (franquia != null) {
                            franquia.getPedidos().put(pedidoId, pedido);
                            // Atualizar o próximo ID se necessário
                            if (pedidoId >= sistema.getProximoIdPedido()) {
                                sistema.setProximoIdPedido(pedidoId + 1);
                            }
                        }
                    }
                }
            }
        }

        // --- SEGUNDA PASSAGEM: LIGANDO AS REFERÊNCIAS ---
        // Ligar Gerentes às suas Franquias
        for (Map.Entry<Integer, String> entry : franquiaParaGerente.entrySet()) {
            Franquia f = sistema.getFranquias().get(entry.getKey());
            Gerente g = (Gerente) sistema.getUsuarios().get(entry.getValue());
            if (f != null && g != null) {
                f.setGerente(g);
                g.setFranquiaGerenciada(f);
            }
        }
        
        // Ligar Vendedores às suas Franquias
        for (Map.Entry<String, String> entry : vendedorParaFranquia.entrySet()) {
            Vendedor v = (Vendedor) sistema.getUsuarios().get(entry.getKey());
            Franquia f = sistema.getFranquias().get(Integer.parseInt(entry.getValue()));
            if (v != null && f != null) {
                v.setFranquia(f);
                f.getVendedores().put(v.getCpf(), v);
            }
        }

        return sistema;
    }
}