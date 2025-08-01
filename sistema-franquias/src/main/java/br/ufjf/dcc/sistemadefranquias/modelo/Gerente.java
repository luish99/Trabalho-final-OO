package br.ufjf.dcc.sistemadefranquias.modelo;

public class Gerente extends Usuario {
    private Franquia franquiaGerenciada;

    // Construtor corrigido para incluir a Franquia gerenciada
    public Gerente(String nome, String cpf, String email, String senha, Franquia franquiaGerenciada) {
        super(nome, cpf, email, senha);
        this.franquiaGerenciada = franquiaGerenciada;
    }
    
    public Franquia getFranquiaGerenciada() {
        return franquiaGerenciada;
    }

    public void setFranquiaGerenciada(Franquia franquia) {
        this.franquiaGerenciada = franquia;
    }
}