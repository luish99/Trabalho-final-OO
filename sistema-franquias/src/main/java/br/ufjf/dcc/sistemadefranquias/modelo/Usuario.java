
package br.ufjf.dcc.sistemadefranquias.modelo;

//essa e uma super classe que virao as classes dono gerente e vendedor
//ela contem os atributos comuns a todos os usuarios do sistema
//como nome cpf email e senha
//além de um método para validar a senha

public abstract class Usuario  {
    private String nome;
    private String cpf;
    private String email;
    private String senha;

    // Construtor
    public Usuario(String nome, String cpf, String email, String senha) {
        this.nome = nome;
        this.cpf = cpf;
        this.email = email;
        this.senha = senha;
    }

   // Getters e Setters
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }

    // Método para validar a senha
    public boolean checarSenha(String senha) {
        return this.senha.equals(senha);
    }
}