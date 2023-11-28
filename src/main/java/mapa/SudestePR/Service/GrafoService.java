package mapa.SudestePR.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mapa.SudestePR.Entity.Cidade;
import mapa.SudestePR.Entity.Rota;

@Service
public class GrafoService {
	@Autowired
	private CidadeService cidadeService;
	
    private final List<Cidade> cidades;
    private final List<Rota> rotas;
	
    public GrafoService() {
        this.cidades = new ArrayList<>();
        this.rotas = new ArrayList<>();
    }
	

    public void setCidade(String nome) {
        Cidade novaCidade = new Cidade(nome);
        this.cidades.add(novaCidade);
    }

    public Cidade getCidade(String nome) {
        return cidades.stream()
                .filter(cidade -> cidade.getNome().equals(nome))
                .findFirst()
                .orElse(null);
    }

    public void setRota(double distancia, String nomeCidadeInicio, String nomeCidadeFim) {
        Cidade cidadeInicio = getCidade(nomeCidadeInicio);
        Cidade cidadeFim = getCidade(nomeCidadeFim);
        Rota rota = new Rota(distancia, cidadeInicio, cidadeFim);

        cidadeService.adicionarArestaEntrada(rota);
        cidadeService.adicionarArestaSaida(rota);
        this.rotas.add(rota);
    }

    public List<Cidade> calcularMenorDistancia(String nomeCidadeInicio, String nomeCidadeFim) {
        Cidade cidadeInicio = this.getCidade(nomeCidadeInicio);
        Cidade cidadeFim = this.getCidade(nomeCidadeFim);

        Map<Cidade, Double> distancias = new HashMap<>();
        Map<Cidade, Cidade> anteriores = new HashMap<>();

        PriorityQueue<Cidade> fila = new PriorityQueue<>(Comparator.comparingDouble(distancias::get));

        for (Cidade cidade : this.cidades) {
            if (cidade.equals(cidadeInicio)) {
                distancias.put(cidade, 0.0);
            } else {
                distancias.put(cidade, Double.MAX_VALUE);
            }
            fila.add(cidade);
        }

        while (!fila.isEmpty()) {
            Cidade atual = fila.poll();

            for (Rota aresta : atual.getRotasSaida()) {
                Cidade vizinho = aresta.getCidadeFim();
                double distanciaAlternativa = distancias.get(atual) + aresta.getDistancia();

                if (distanciaAlternativa < distancias.get(vizinho)) {
                    fila.remove(vizinho);
                    distancias.put(vizinho, distanciaAlternativa);
                    anteriores.put(vizinho, atual);
                    fila.add(vizinho);
                }
            }
        }

        List<Cidade> caminho = new ArrayList<>();
        for (Cidade cidade = cidadeFim; cidade != null; cidade = anteriores.get(cidade)) {
            caminho.add(cidade);
        }
        Collections.reverse(caminho);

        return caminho;
    }
}
