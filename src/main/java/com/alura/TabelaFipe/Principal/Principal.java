package com.alura.TabelaFipe.Principal;

import com.alura.TabelaFipe.Model.DadosAutomovel;
import com.alura.TabelaFipe.Model.Modelos;
import com.alura.TabelaFipe.Model.Veiculo;
import com.alura.TabelaFipe.Service.ConexaoAPI;
import com.alura.TabelaFipe.Service.ConverteDados;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Principal {
    private Scanner leitura = new Scanner(System.in);

    private ConexaoAPI conexao = new ConexaoAPI();
    private ConverteDados conversor = new ConverteDados();

    private final String ENDERECO_BASE = "https://parallelum.com.br/fipe/api/v1/";

    public void telaInicial() throws JsonProcessingException {

            System.out.println("Escolha uma opção: <carro>, <moto>, <caminhão>");
            System.out.println("Digite o nome da opção que você busca: ");
            var opcao = leitura.nextLine();

            if (opcao.toLowerCase().trim().contains("car")){
                opcao = "carros/marcas";
            } else if (opcao.toLowerCase().trim().contains("mot")){
                opcao = "motos/marcas";
            } else {
                opcao = "caminhoes/marcas";
            }

            var url = ENDERECO_BASE + opcao;
            var json = conexao.obterDados(url);
            System.out.println(json);

            var marcas = conversor.obterLista(json, DadosAutomovel.class);
            marcas.stream()
                    .sorted(Comparator.comparing(DadosAutomovel::codigo))
                    .forEach(System.out::println);

            System.out.println("Digite o código da marca: ");
            var marcaCodigo = leitura.nextLine();

            url = url + "/" + marcaCodigo + "/modelos";
            json = conexao.obterDados(url);
            var modeloLista = conversor.obterDados(json, Modelos.class);

            System.out.println("\nModelos dessa marca: ");
            modeloLista.modelos().stream()
                    .sorted(Comparator.comparing(DadosAutomovel::codigo))
                    .forEach(System.out::println);

            System.out.println("\nDigite um trecho do nome do carro: ");
            var nomeVeiculo = leitura.nextLine();

            List<DadosAutomovel> modelosFiltrados = modeloLista.modelos().stream()
                    .filter(m -> m.nome().toLowerCase().contains(nomeVeiculo.toLowerCase()))
                    .collect(Collectors.toList());

            System.out.println("\nModelos filtrados");
            modelosFiltrados.forEach(System.out::println);

            System.out.println("Digite o código do modelo esclhido: ");
            var codigoModelo = leitura.nextLine();

            url = url + "/" + codigoModelo + "/anos";
            json = conexao.obterDados(url);
            List<DadosAutomovel> anos = conversor.obterLista(json, DadosAutomovel.class);
            List<Veiculo> veiculos = new ArrayList<>();

            for(int i = 0; i< anos.size(); i++){
                var urlAnos = url + "/" + anos.get(i).codigo();
                json = conexao.obterDados(urlAnos);
                Veiculo veiculo = conversor.obterDados(json, Veiculo.class);
                veiculos.add(veiculo);
            }

            System.out.println("\nVeículos filtrados com avaliações por ano: ");
            veiculos.forEach(System.out::println);
        }
}
