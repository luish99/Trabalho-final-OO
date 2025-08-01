// br/ufjf/dcc/sistemadefranquias/controle/Sistema.java
package br.ufjf.dcc.sistemadefranquias.controle;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;

import br.ufjf.dcc.sistemadefranquias.excecoes.ValidacaoException;
import br.ufjf.dcc.sistemadefranquias.modelo.Dono;
import br.ufjf.dcc.sistemadefranquias.modelo.Endereco;
import br.ufjf.dcc.sistemadefranquias.modelo.Franquia;
import br.ufjf.dcc.sistemadefranquias.modelo.Gerente;
import br.ufjf.dcc.sistemadefranquias.modelo.Pedido;
import br.ufjf.dcc.sistemadefranquias.modelo.Produto;
import br.ufjf.dcc.sistemadefranquias.modelo.Usuario;
import br.ufjf.dcc.sistemadefranquias.modelo.Vendedor;

public class Sistema {
    private Map<String, Usuario> usuarios;      // Chave: CPF
    private Map<Integer, Franquia> franquias;   // Chave: ID da Franquia
    private int proximoIdFranquia = 1;

    public Sistema() {
        this.usuarios = new HashMap<>();
        this.franquias = new HashMap<>();
    }

    // --- Métodos de Autenticação e Usuários ---

    public Usuario autenticar(String cpf, String senha) throws ValidacaoException {
        if (!usuarios.containsKey(cpf)) {
            throw new ValidacaoException("Usuário não encontrado.");
        }
        Usuario usuario = usuarios.get(cpf);
        if (usuario.checarSenha(senha)) {
            return usuario;
        }
        throw new ValidacaoException("Senha incorreta.");
    }
    
    public void adicionarDono(String nome, String cpf, String email, String senha) throws ValidacaoException {
        if(usuarios.containsKey(cpf)) {
            throw new ValidacaoException("CPF já cadastrado.");
        }
        // Outras validações...
        Dono novoDono = new Dono(nome, cpf, email, senha);
        usuarios.put(cpf, novoDono);
    }

    public void adicionarGerente(String nome, String cpf, String email, String senha, Franquia franquia) throws ValidacaoException {
        if(usuarios.containsKey(cpf)) {
            throw new ValidacaoException("CPF já cadastrado.");
        }
        Gerente novoGerente = new Gerente(nome, cpf, email, senha, franquia);
        usuarios.put(cpf, novoGerente);
        franquia.setGerente(novoGerente);
    }
    
    public void adicionarVendedor(String nome, String cpf, String email, String senha, Franquia franquia) throws ValidacaoException {
        if(usuarios.containsKey(cpf)) {
            throw new ValidacaoException("CPF já cadastrado.");
        }
        Vendedor novoVendedor = new Vendedor(nome, cpf, email, senha, franquia);
        usuarios.put(cpf, novoVendedor);
        franquia.getVendedores().put(cpf, novoVendedor);
    }

    public void removerUsuario(String cpf) throws ValidacaoException {
        if (!usuarios.containsKey(cpf)) {
            throw new ValidacaoException("Usuário não encontrado.");
        }
        // Lógica para remover o usuário de uma franquia, se aplicável
        Usuario usuario = usuarios.get(cpf);
        if (usuario instanceof Gerente) {
            ((Gerente) usuario).getFranquiaGerenciada().setGerente(null);
        } else if (usuario instanceof Vendedor) {
            ((Vendedor) usuario).getFranquia().getVendedores().remove(cpf);
        }
        usuarios.remove(cpf);
    }

    // --- Métodos de Gerenciamento de Franquias ---
    
    public void adicionarFranquia(String nome, Endereco endereco, Gerente gerente) throws ValidacaoException {
        if (franquias.values().stream().anyMatch(f -> f.getNome().equals(nome))) {
            throw new ValidacaoException("Já existe uma franquia com este nome.");
        }
        
        Franquia novaFranquia = new Franquia(proximoIdFranquia, nome, endereco);
        novaFranquia.setGerente(gerente); // Associa o gerente à franquia
        franquias.put(proximoIdFranquia, novaFranquia);
        proximoIdFranquia++;
    }

    public Franquia buscarFranquiaPorId(int id) {
        return franquias.get(id);
    }

    public void removerFranquia(int id) throws ValidacaoException {
    Franquia franquia = franquias.get(id);
    if (franquia == null) {
        throw new ValidacaoException("Franquia não encontrada.");
    }

    // 1. Remover o gerente, se existir
    Gerente gerente = franquia.getGerente();
    if (gerente != null) {
        usuarios.remove(gerente.getCpf());
    }

    // 2. Remover todos os vendedores da franquia
    for (String cpfVendedor : franquia.getVendedores().keySet()) {
        usuarios.remove(cpfVendedor);
    }

    // 3. Remover a franquia do mapa
    franquias.remove(id);
}


    // --- Métodos de Gerenciamento de Pedidos e Produtos ---

    public void registrarPedido(Franquia franquia, Pedido pedido) throws ValidacaoException {
        if (franquia == null) {
            throw new ValidacaoException("Franquia inválida para registrar o pedido.");
        }
        // Lógica de processamento do pedido...
        franquia.getPedidos().put(pedido.getId(), pedido);
    }

    public void adicionarProduto(Franquia franquia, Produto produto) throws ValidacaoException {
        if (franquia == null) {
            throw new ValidacaoException("Franquia inválida para adicionar o produto.");
        }
        franquia.getEstoque().put(produto.getId(), produto);
    }

   

    // Este método salva o estado do sistema em um arquivo
    public void salvarSistema(String nomeArquivo) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(nomeArquivo);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(this);
        }
    }

    // Este método carrega o estado do sistema a partir de um arquivo
    public static Sistema carregarSistema(String nomeArquivo) throws IOException, ClassNotFoundException {
        try (FileInputStream fis = new FileInputStream(nomeArquivo);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            return (Sistema) ois.readObject();
        }
    }

    // --- Getters e Setters para a persistência ---

    public Map<String, Usuario> getUsuarios() {
        return usuarios;
    }

    public Map<Integer, Franquia> getFranquias() {
        return franquias;
    }

    public int getProximoIdFranquia() {
        return proximoIdFranquia;
    }
    
    public void setProximoIdFranquia(int proximoIdFranquia){
        this.proximoIdFranquia = proximoIdFranquia;
    }
    


public Map<String, Gerente> getGerentes() {
    Map<String, Gerente> gerentes = new HashMap<>();
    for (Usuario usuario : usuarios.values()) {
        if (usuario instanceof Gerente) {
            gerentes.put(usuario.getCpf(), (Gerente) usuario);
        }
    }
    return gerentes;
}


public Franquia buscarFranquiaPorNome(String nome) {
    for (Franquia franquia : franquias.values()) {
        if (franquia.getNome().equalsIgnoreCase(nome)) {
            return franquia;
        }
    }
    return null; 
}


}