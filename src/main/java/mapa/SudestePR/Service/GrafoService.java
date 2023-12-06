package mapa.SudestePR.Service;

import java.util.*;

import mapa.SudestePR.Repository.CidadeRepository;
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
            return (Rota) rotaService.getRotasByCidades(cidadeInicioId, cidadeFimId);
        }
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

        for (Cidade cidade : cidades) {
            Long cidadeId = cidade.getId();
            if (cidadeId.equals(cidadeInicioC)) {
                distancias.put(cidadeId, 0.0);
            } else {
                distancias.put(cidadeId, Double.MAX_VALUE);
            }
            fila.add(cidadeId);
        }

        while (!fila.isEmpty()) {
            Long atualId = fila.poll();

            Cidade atual = getCidadeById(atualId);

            if (atual == null) {
                continue;
            }

            for (Rota aresta : atual.getRotasSaida()) {
                Cidade vizinho = aresta.getCidadeFim();

                if (vizinho == null) {
                    continue;
                }

                Long vizinhoId = vizinho.getId();
                double distanciaAlternativa = distancias.get(atualId) + aresta.getDistancia();

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

        List<Rota> caminho = new ArrayList<>();
        if (!caminhoIds.isEmpty()) {
            for (int i = 0; i < caminhoIds.size() - 1; i++) {
                Long cidadeAtualId = caminhoIds.get(i);
                Long cidadeProximaId = caminhoIds.get(i + 1);

                Cidade cidadeAtual = getCidadeById(cidadeAtualId);
                Cidade cidadeProxima = getCidadeById(cidadeProximaId);

                Rota rota = getRotaEntreCidades(cidadeAtual, cidadeProxima);
                caminho.add(rota);
            }

        }
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
            if (cidade.getNome().equalsIgnoreCase(nome.trim())) {
                return cidade.getId();
            }
        }
        return null;
    }
}
