// br/ufjf/dcc/sistemadefranquias/persistencia/Persistencia.java
package br.ufjf.dcc.sistemadefranquias.persistencia;

import br.ufjf.dcc.sistemadefranquias.controle.Sistema;
import br.ufjf.dcc.sistemadefranquias.modelo.*;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

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
            
            // Poderíamos continuar para Produtos, Pedidos, etc., seguindo a mesma lógica...
            // Por simplicidade, este exemplo foca em Usuários e Franquias.

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