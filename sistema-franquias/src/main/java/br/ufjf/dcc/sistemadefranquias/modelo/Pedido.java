package br.ufjf.dcc.sistemadefranquias.modelo;


import java.util.Date;
import java.util.HashMap; 
import java.util.Map; 

public class Pedido {
    private int id;
    private Date dataPedido;
    private Cliente cliente; 
    private Map<Produto, Integer> produtosPedidos; 
    private double valorTotal;
    private String status; // Ex: "Pendente", "Confirmado", "Entregue", "Cancelado"

    public Pedido(int id, Date dataPedido, Cliente cliente, double valorTotal, String status) {
        this.id = id;
        this.dataPedido = dataPedido;
        this.cliente = cliente;
        this.valorTotal = valorTotal;
        this.status = status;
        this.produtosPedidos = new HashMap<>(); 
    }

    // --- Getters ---

    public int getId() {
        return id;
    }

    public Date getDataPedido() {
        return dataPedido;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public Map<Produto, Integer> getProdutosPedidos() {
        return produtosPedidos;
    }

    public double getValorTotal() {
        return valorTotal;
    }

    public String getStatus() {
        return status;
    }

    // --- Setters ---

    public void setId(int id) {
        this.id = id;
    }

    public void setDataPedido(Date dataPedido) {
        this.dataPedido = dataPedido;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public void setProdutosPedidos(Map<Produto, Integer> produtosPedidos) {
        this.produtosPedidos = produtosPedidos;
    }

    public void setValorTotal(double valorTotal) {
        this.valorTotal = valorTotal;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}