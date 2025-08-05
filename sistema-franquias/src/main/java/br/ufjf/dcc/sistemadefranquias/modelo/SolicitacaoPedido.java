// br/ufjf/dcc/sistemadefranquias/modelo/SolicitacaoPedido.java
package br.ufjf.dcc.sistemadefranquias.modelo;

import java.util.Date;

public class SolicitacaoPedido {
    private int id;
    private Date dataSolicitacao;
    private Vendedor vendedor;
    private Pedido pedido;
    private String tipoSolicitacao; // "ALTERAR" ou "EXCLUIR"
    private String justificativa;
    private String status; // "PENDENTE", "APROVADA", "REJEITADA"
    private String comentarioGerente;
    private Date dataResposta;
    
    public SolicitacaoPedido(int id, Vendedor vendedor, Pedido pedido, String tipoSolicitacao, String justificativa) {
        this.id = id;
        this.dataSolicitacao = new Date();
        this.vendedor = vendedor;
        this.pedido = pedido;
        this.tipoSolicitacao = tipoSolicitacao;
        this.justificativa = justificativa;
        this.status = "PENDENTE";
    }
    
    // --- Getters ---
    public int getId() { return id; }
    public Date getDataSolicitacao() { return dataSolicitacao; }
    public Vendedor getVendedor() { return vendedor; }
    public Pedido getPedido() { return pedido; }
    public String getTipoSolicitacao() { return tipoSolicitacao; }
    public String getJustificativa() { return justificativa; }
    public String getStatus() { return status; }
    public String getComentarioGerente() { return comentarioGerente; }
    public Date getDataResposta() { return dataResposta; }
    
    // --- Setters ---
    public void setId(int id) { this.id = id; }
    public void setDataSolicitacao(Date dataSolicitacao) { this.dataSolicitacao = dataSolicitacao; }
    public void setVendedor(Vendedor vendedor) { this.vendedor = vendedor; }
    public void setPedido(Pedido pedido) { this.pedido = pedido; }
    public void setTipoSolicitacao(String tipoSolicitacao) { this.tipoSolicitacao = tipoSolicitacao; }
    public void setJustificativa(String justificativa) { this.justificativa = justificativa; }
    public void setStatus(String status) { this.status = status; }
    public void setComentarioGerente(String comentarioGerente) { this.comentarioGerente = comentarioGerente; }
    public void setDataResposta(Date dataResposta) { this.dataResposta = dataResposta; }
}
