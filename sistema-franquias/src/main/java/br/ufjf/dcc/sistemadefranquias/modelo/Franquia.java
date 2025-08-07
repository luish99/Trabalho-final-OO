package br.ufjf.dcc.sistemadefranquias.modelo;


import java.util.HashMap;
import java.util.Map;

public class Franquia  {
    private int id;
    private String nome;
    private Endereco endereco;
    private Gerente gerente;
    private Map<String, Vendedor> vendedores; // Chave: CPF do Vendedor
    private Map<Integer, Produto> estoque;    // Chave: ID do Produto
    private Map<Integer, Pedido> pedidos;     // Chave: ID do Pedido
    private Map<Integer, SolicitacaoPedido> solicitacoesPendentes; // Chave: ID da Solicitação
    private Map<Integer, SolicitacaoPedido> historicoSolicitacoes; // Chave: ID da Solicitação

    public Franquia(int id, String nome, Endereco endereco) {
        this.id = id;
        this.nome = nome;
        this.endereco = endereco;
        this.vendedores = new HashMap<>();
        this.estoque = new HashMap<>();
        this.pedidos = new HashMap<>();
        this.solicitacoesPendentes = new HashMap<>();
        this.historicoSolicitacoes = new HashMap<>();
    }


    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public Endereco getEndereco() {
        return endereco;
    }

    public Gerente getGerente() {
        return gerente;
    }

    public Map<String, Vendedor> getVendedores() {
        return vendedores;
    }

    public Map<Integer, Produto> getEstoque() {
        return estoque;
    }

    public Map<Integer, Pedido> getPedidos() {
        return pedidos;
    }

    public Map<Integer, SolicitacaoPedido> getSolicitacoesPendentes() {
        return solicitacoesPendentes;
    }

    public Map<Integer, SolicitacaoPedido> getHistoricoSolicitacoes() {
        return historicoSolicitacoes;
    }


    public void setId(int id) {
        this.id = id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }

    public void setGerente(Gerente gerente) {
        this.gerente = gerente;
    }

    public void setVendedores(Map<String, Vendedor> vendedores) {
        this.vendedores = vendedores;
    }

    public void setEstoque(Map<Integer, Produto> estoque) {
        this.estoque = estoque;
    }

    public void setPedidos(Map<Integer, Pedido> pedidos) {
        this.pedidos = pedidos;
    }

    public void setSolicitacoesPendentes(Map<Integer, SolicitacaoPedido> solicitacoesPendentes) {
        this.solicitacoesPendentes = solicitacoesPendentes;
    }
}