package br.ufjf.dcc.sistemadefranquias.modelo;

public class Vendedor extends Usuario {
    private Franquia franquia;

    public Vendedor(String nome, String cpf, String email, String senha, Franquia franquia) {
        super(nome, cpf, email, senha);
        this.franquia = franquia;
    }
    
    public Franquia getFranquia() { return franquia; }
    public void setFranquia(Franquia franquia) { this.franquia = franquia; }
}