package mapa.SudestePR.Service;

import java.util.*;

import mapa.SudestePR.Repository.CidadeRepository;
import mapa.SudestePR.Repository.RotaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mapa.SudestePR.Entity.Cidade;
import mapa.SudestePR.Entity.Rota;

@Service
public class GrafoService {
    @Autowired
    private CidadeService cidadeService;
    @Autowired
    private GrafoRotaService rotaService;
    @Autowired
    private CidadeRepository cidadeRepository;

    private List<Cidade> cidades = new ArrayList<>();
    private List<Rota> rotas = new ArrayList<>();


    private void carregarCidades() {
        if (cidades.isEmpty()) {
            this.cidades = cidadeService.getCidades();
        }
    }

    private void carregarRotas() {
        if (rotas.isEmpty()) {
            this.rotas = rotaService.getRotas();
        }
    }

    public Rota getRotaEntreCidades(Cidade cidadeInicio, Cidade cidadeFim) {
        for (Rota rota : rotas) {
            Long cidadeInicioId = cidadeInicio.getId();
            Long cidadeFimId = cidadeFim.getId();
            System.out.println("Resultado de getRotasByCidades: {}"+ rotaService.getRotasByCidades(cidadeInicioId, cidadeFimId));
            return (Rota) rotaService.getRotasByCidades(cidadeInicioId, cidadeFimId);
        }
        System.out.println("Rota não encontrada para: "+ cidadeInicio.getNome() +" , "+ cidadeFim.getNome());
        return null;
    }


    public List<Rota> calcularMenorDistancia(String cidadeInicio, String cidadeFim) {
        carregarCidades();
        carregarRotas();

        Long cidadeInicioC = getIdCidadeByNome(cidadeInicio);
        Long cidadeFimC = getIdCidadeByNome(cidadeFim);

        Map<Long, Double> distancias = new HashMap<>();
        Map<Long, Long> anteriores = new HashMap<>();

        PriorityQueue<Long> fila = new PriorityQueue<>(Comparator.comparingDouble(distancias::get));

        System.out.println("Número de cidades na lista: {}"+ cidades.size());
        for (Cidade cidade : cidades) {
            Long cidadeId = cidade.getId();
            if (cidadeId.equals(cidadeInicioC)) {
                distancias.put(cidadeId, 0.0);
            } else {
                distancias.put(cidadeId, Double.MAX_VALUE);
            }
            fila.add(cidadeId);

            System.out.println("Cidade: "+cidade.getNome() + ", ID: "+ cidadeId +", Distância Inicial: "+ distancias.get(cidadeId));
        }
        System.out.println("Cidade Início: " + cidadeInicioC);
        System.out.println("Cidade Fim: " + cidadeFimC);
        System.out.println("Distâncias iniciais: " + distancias);

        while (!fila.isEmpty()) {
            System.out.println("Entrou no loop - Algoritmo de Dijkstra!");
            Long atualId = fila.poll();
            System.out.println("Processando ID atual: " + atualId);

            Cidade atual = getCidadeById(atualId);

            if (atual == null) {
                System.out.println("Atenção: atual é nulo para ID " + atualId);
                continue;
            }

            System.out.println("Processando cidade atual: " + atual.getNome());

            for (Rota aresta : atual.getRotasSaida()) {
                Cidade vizinho = aresta.getCidadeFim();

                if (vizinho == null) {
                    System.out.println("Atenção: vizinho é nulo para rota de " + atual.getNome());
                    continue;
                }

                Long vizinhoId = vizinho.getId();
                double distanciaAlternativa = distancias.get(atualId) + aresta.getDistancia();

                System.out.println("Processando rota de " + atual.getNome() + " para " + vizinho.getNome());
                System.out.println("  Vizinho: " + vizinho.getNome() + ", Distância Alternativa: " + distanciaAlternativa);

                if (distanciaAlternativa < distancias.get(vizinhoId)) {
                    fila.remove(vizinhoId);
                    distancias.put(vizinhoId, distanciaAlternativa);
                    anteriores.put(vizinhoId, atualId);
                    fila.add(vizinhoId);
                }
            }
        }

        List<Long> caminhoIds = new ArrayList<>();
        for (Long cidade = cidadeFimC; cidade != null; cidade = anteriores.get(cidade)) {
            caminhoIds.add(cidade);
        }
        Collections.reverse(caminhoIds);

        System.out.println("Ids Rotas: "+caminhoIds);
        List<Rota> caminho = new ArrayList<>();
        if (!caminhoIds.isEmpty()) {
            for (int i = 0; i < caminhoIds.size() - 1; i++) {
                Long cidadeAtualId = caminhoIds.get(i);
                Long cidadeProximaId = caminhoIds.get(i + 1);

                System.out.println("Cidade Atual ID: " + cidadeAtualId);
                System.out.println("Cidade Próxima ID: " + cidadeProximaId);

                Cidade cidadeAtual = getCidadeById(cidadeAtualId);
                Cidade cidadeProxima = getCidadeById(cidadeProximaId);

                System.out.println("Cidade Atual: " + cidadeAtual);
                System.out.println("Cidade Próxima: " + cidadeProxima);

                Rota rota = getRotaEntreCidades(cidadeAtual, cidadeProxima);
                System.out.println("Rota: " + rota);

                caminho.add(rota);
            }

        }

        System.out.println("CAMINHO NO FINAL: "+caminho);
        return caminho;
    }

    private Cidade getCidadeById(Long cidadeId) {
        return cidadeService.getCidades().stream()
                .filter(cidadeDTO -> cidadeDTO.getId().equals(cidadeId))
                .findFirst()
                .orElse(null);
    }

    private Long getIdCidadeByNome(String nome) {
        List<Cidade> cidadesList = cidadeRepository.findAll();

        for (Cidade cidade : cidadesList) {
            System.out.println("Comparando: " + cidade.getNome() + " com " + nome);

            if (cidade.getNome().equalsIgnoreCase(nome.trim())) {
                System.out.println("Correspondência encontrada para " + nome + ". ID: " + cidade.getId());
                return cidade.getId();
            }
        }

        System.out.println("Nenhuma correspondência encontrada para " + nome);
        return null;
    }
}
