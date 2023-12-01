package mapa.SudestePR.Service;

import mapa.SudestePR.DTO.RotaDtoResponse;
import mapa.SudestePR.Entity.Cidade;
import mapa.SudestePR.Entity.RotaResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mapa.SudestePR.DTO.RotaDtoRequest;
import mapa.SudestePR.Entity.Rota;
import mapa.SudestePR.Repository.RotaRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class RotaService {

    @Autowired
    private RotaRepository rotaRepository;
    @Autowired
    private GrafoService grafoService;

    @Autowired
    private CidadeService cidadeService;

    public void saveRota(RotaDtoRequest rotaDtoRequest) {
        Rota rota = new Rota(
                rotaDtoRequest.getDistancia(),
                rotaDtoRequest.getCidadeInicio(),
                rotaDtoRequest.getCidadeFim()
        );
        rotaRepository.save(rota);
    }

    public RotaResponse getRota(RotaDtoResponse rotaDtoResponse) {
        String cidadeInicio = rotaDtoResponse.getCidadeInicio();
        String cidadeFim = rotaDtoResponse.getCidadeFim();
        String veiculo = rotaDtoResponse.getVeiculo();

        System.out.println(veiculo);
        System.out.println(cidadeInicio);
        System.out.println(cidadeFim);

        List<Rota> caminho = grafoService.calcularMenorDistancia(cidadeInicio, cidadeFim);
        System.out.println("COMO CAMINHO CHEGA EM ROTASERVICE: "+caminho);

        if (caminho != null && !caminho.isEmpty()) {
            List<Rota> rotaCompleta = new ArrayList<>();

            for (int i = 0; i < caminho.size() - 1; i++) {
                System.out.println("Tamanho do Caminho: " + caminho.size());
                System.out.println("Índice Atual no Loop: " + i);
                System.out.println("caminho.get(i) != null? " + caminho.get(i));
                System.out.println("caminho.get(i + 1) != null? " + caminho.get(i + 1));

                if (caminho.get(i) != null && caminho.get(i + 1) != null) {
                    System.out.println("Entrou no IF!");
                    Cidade cidadeAtual = caminho.get(i).getCidadeInicio();
                    Cidade proximaCidade = caminho.get(i).getCidadeFim();

                    System.out.println("Cidade Atual: (dentro do for caminho.size)" + cidadeAtual);

                    if (cidadeAtual != null && proximaCidade != null) {
                        System.out.println("ENTROU NO IF DENTRO DE FOR");
                        Rota rota = grafoService.getRotaEntreCidades(cidadeAtual, proximaCidade);
                        System.out.println("Rota: (dentro do IF EM for caminho.size:)" + rota);
                        rotaCompleta.add(rota);
                    } else {
                        return new RotaResponse(false, null, null, "Operação falhou! Cidade não encontrada");
                    }
                } else {
                    return new RotaResponse(false, null, null, "Operação falhou! Elemento nulo encontrado");
                }
            }

            return new RotaResponse(true, null, rotaCompleta, "Operação realizada com sucesso!");
        } else {
            return new RotaResponse(false, null, Collections.emptyList(), "Operação falhou! Caminho não encontrado");
        }
    }
}
