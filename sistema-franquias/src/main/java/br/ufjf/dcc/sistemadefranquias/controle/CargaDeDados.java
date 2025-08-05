// br/ufjf/dcc/sistemadefranquias/controle/CargaDeDados.java
package br.ufjf.dcc.sistemadefranquias.controle;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import br.ufjf.dcc.sistemadefranquias.modelo.Cliente;
import br.ufjf.dcc.sistemadefranquias.modelo.Dono;
import br.ufjf.dcc.sistemadefranquias.modelo.Endereco;
import br.ufjf.dcc.sistemadefranquias.modelo.Franquia;
import br.ufjf.dcc.sistemadefranquias.modelo.Gerente;
import br.ufjf.dcc.sistemadefranquias.modelo.Pedido;
import br.ufjf.dcc.sistemadefranquias.modelo.Produto;
import br.ufjf.dcc.sistemadefranquias.modelo.Vendedor;

/**
 * Classe utilitária para popular o sistema com uma grande massa de dados para teste.
 */
public class CargaDeDados {

    // Arrays de nomes para geração aleatória de dados
    private static final String[] PRIMEIROS_NOMES = {"Lucas", "Julia", "Mateus", "Mariana", "Gabriel", "Laura", "Pedro", "Sofia", "Guilherme", "Isabela", "Enzo", "Manuela", "Rafael", "Helena", "Felipe", "Valentina", "Gustavo", "Alice", "Bruno", "Livia"};
    private static final String[] ULTIMOS_NOMES = {"Silva", "Santos", "Oliveira", "Souza", "Rodrigues", "Ferreira", "Alves", "Pereira", "Lima", "Gomes", "Ribeiro", "Martins", "Carvalho", "Almeida", "Melo", "Barbosa"};

    // Dados para as 5 franquias
    private static final String[][] DADOS_FRANQUIAS = {
        {"Franquia Sudeste - SP", "Av. Paulista", "1578", "Cerqueira César", "São Paulo", "SP", "01310-200"},
        {"Franquia Litorânea - RJ", "Av. Atlântica", "1702", "Copacabana", "Rio de Janeiro", "RJ", "22021-001"},
        {"Franquia Central - MG", "Av. Afonso Pena", "1500", "Centro", "Belo Horizonte", "MG", "30130-005"},
        {"Franquia Nordeste - BA", "Av. Oceânica", "2345", "Barra", "Salvador", "BA", "40140-130"},
        {"Franquia Local - JF", "Av. Rio Branco", "2000", "Centro", "Juiz de Fora", "MG", "36016-311"}
    };

    public static void popularSistemaComDadosIniciais(Sistema sistema) {
        System.out.println("Populando o sistema com MASSA DE DADOS INICIAIS para teste...");
        Random random = new Random();

        try {
            // --- 1. CRIANDO DONO ---
            sistema.getUsuarios().put("admin", new Dono("Dono Admin", "admin", "admin@franquia.com", "admin"));

            // --- Listas para guardar os objetos criados ---
            List<Franquia> franquiasCriadas = new ArrayList<>();
            List<Vendedor> todosOsVendedores = new ArrayList<>();

            // --- 2. CRIANDO 5 FRANQUIAS E 5 GERENTES ---
            for (int i = 0; i < DADOS_FRANQUIAS.length; i++) {
                String[] dados = DADOS_FRANQUIAS[i];
                
                // Cria a franquia
                Endereco end = new Endereco(dados[1], dados[2], "Loja " + (i + 1), dados[3], dados[4], dados[5], dados[6]);
                Franquia franquia = new Franquia(sistema.getProximoIdFranquia(), dados[0], end);
                sistema.getFranquias().put(franquia.getId(), franquia);
                sistema.setProximoIdFranquia(sistema.getProximoIdFranquia() + 1);
                franquiasCriadas.add(franquia);

                // Cria o gerente para esta franquia
                String nomeGerente = gerarNomeAleatorio(random);
                String cpfGerente = "g" + (i + 1); // g1, g2, g3...
                Gerente gerente = new Gerente(nomeGerente, cpfGerente, cpfGerente + "@franquia.com", "123", franquia);
                sistema.getUsuarios().put(gerente.getCpf(), gerente);
                franquia.setGerente(gerente);

                System.out.println("Criada " + franquia.getNome() + " com o gerente " + gerente.getNome());
            }

            // --- 3. CRIANDO 50 VENDEDORES (10 POR FRANQUIA) ---
            int vendedorIdCounter = 1;
            for (Franquia franquia : franquiasCriadas) {
                System.out.println("Criando 10 vendedores para a franquia " + franquia.getNome() + "...");
                for (int i = 0; i < 10; i++) {
                    String nomeVendedor = gerarNomeAleatorio(random);
                    String cpfVendedor = "v" + (vendedorIdCounter++); // v1, v2, v3...
                    Vendedor vendedor = new Vendedor(nomeVendedor, cpfVendedor, cpfVendedor + "@franquia.com", "123", franquia);
                    
                    sistema.getUsuarios().put(vendedor.getCpf(), vendedor);
                    franquia.getVendedores().put(vendedor.getCpf(), vendedor);
                    todosOsVendedores.add(vendedor);
                }
            }

            // --- 4. CRIANDO PRODUTOS NO ESTOQUE DE TODAS AS FRANQUIAS ---
            Produto p1 = new Produto(1, "Mouse Gamer", "Mouse com RGB", 150.00, 50);
            Produto p2 = new Produto(2, "Teclado Mecânico", "Teclado ABNT2", 350.00, 40);
            Produto p3 = new Produto(3, "Headset 7.1", "Headset com som surround", 450.00, 30);
            Produto p4 = new Produto(4, "Monitor 24p 144Hz", "Monitor Full HD", 1250.00, 20);
            Produto p5 = new Produto(5, "Webcam 4K", "Webcam Ultra HD com microfone", 800.00, 5);

            for (Franquia f : franquiasCriadas) {
                f.getEstoque().put(p1.getId(), new Produto(p1.getId(), p1.getNome(), p1.getDescricao(), p1.getPreco(), random.nextInt(50) + 10));
                f.getEstoque().put(p2.getId(), new Produto(p2.getId(), p2.getNome(), p2.getDescricao(), p2.getPreco(), random.nextInt(40) + 10));
                f.getEstoque().put(p3.getId(), new Produto(p3.getId(), p3.getNome(), p3.getDescricao(), p3.getPreco(), random.nextInt(30) + 10));
                f.getEstoque().put(p4.getId(), new Produto(p4.getId(), p4.getNome(), p4.getDescricao(), p4.getPreco(), random.nextInt(20) + 5));
                f.getEstoque().put(p5.getId(), new Produto(p5.getId(), p5.getNome(), p5.getDescricao(), p5.getPreco(), 5)); // Quantidade fixa de 5
            }

            // --- 5. CRIANDO CLIENTES E PEDIDOS PARA TESTE ---
            System.out.println("Criando 150 pedidos de exemplo...");
            List<Cliente> clientes = new ArrayList<>();
            for (int i = 0; i < 20; i++) {
                clientes.add(new Cliente(i, gerarNomeAleatorio(random), "c" + i, "c"+i+"@email.com"));
            }

            int proximoIdPedido = sistema.getProximoIdPedido();
            for (int i = 0; i < 150; i++) {
                Vendedor vendedorSorteado = todosOsVendedores.get(random.nextInt(todosOsVendedores.size()));
                Franquia franquiaDoVendedor = vendedorSorteado.getFranquia();
                Cliente clienteSorteado = clientes.get(random.nextInt(clientes.size()));

                double valorTotal = ThreadLocalRandom.current().nextDouble(50.0, 1500.0);

                Pedido pedido = new Pedido(proximoIdPedido++, new Date(), clienteSorteado, valorTotal, "Entregue");
                pedido.setVendedor(vendedorSorteado);
                franquiaDoVendedor.getPedidos().put(pedido.getId(), pedido);
            }
            
            // Atualizar o próximo ID no sistema
            sistema.setProximoIdPedido(proximoIdPedido);

            System.out.println("Carga de dados massivos concluída com sucesso!");

        } catch (Exception e) {
            System.err.println("Ocorreu um erro ao popular o sistema com dados massivos:");
            e.printStackTrace();
        }
    }

    private static String gerarNomeAleatorio(Random random) {
        String nome = PRIMEIROS_NOMES[random.nextInt(PRIMEIROS_NOMES.length)];
        String sobrenome = ULTIMOS_NOMES[random.nextInt(ULTIMOS_NOMES.length)];
        return nome + " " + sobrenome;
    }
}