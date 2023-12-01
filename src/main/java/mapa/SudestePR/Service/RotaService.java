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
        String veiculo = rotaDtoResponse.getVeiculo();
        String cidadeInicio = rotaDtoResponse.getCidadeInicio();
        String cidadeFim = rotaDtoResponse.getCidadeFim();
        Double dist= 0.0;

        List<Rota> caminho = grafoService.calcularMenorDistancia(cidadeInicio, cidadeFim);

        if (caminho != null && !caminho.isEmpty()) {
            List<Rota> rotaCompleta = new ArrayList<>();

            for (int i = 0; i < caminho.size(); i++) {
                if (caminho.get(i) != null) {
                    Cidade cidadeAtual = caminho.get(i).getCidadeInicio();
                    Cidade proximaCidade = caminho.get(i).getCidadeFim();

                    if (cidadeAtual != null) {
                        Rota rota = grafoService.getRotaEntreCidades(cidadeAtual, proximaCidade);
                        rotaCompleta.add(rota);
                        dist = dist + rota.getDistancia();
                    } else {
                        return new RotaResponse(false, null, null, null, null, null,"Operação falhou! Cidade não encontrada");
                    }
                } else {
                    return new RotaResponse(false, null, null, null, null, null, "Operação falhou! Elemento nulo encontrado");
                }
            }
            Double gastoGasolina = gastoGasolina(veiculo, dist);
            String tempoViagem = converterHoras(dist);
            Double alimentacao = calculoAlimentacao(tempoViagem);
            if (alimentacao > 0){
                return new RotaResponse(true, rotaCompleta, dist, tempoViagem, gastoGasolina, alimentacao,  "Operação realizada com sucesso!");
            } else {
                return new RotaResponse(true, rotaCompleta, dist, tempoViagem, gastoGasolina, alimentacao,  "Operação realizada com sucesso!");
            }
        } else {
            return new RotaResponse(false,null, null, null, null, null, "Operação falhou! Caminho não encontrado");
        }
    }

    private Double gastoGasolina(String veiculo, Double distanciaTotal) {
        if(veiculo.equalsIgnoreCase("carro")){
            return gastoGasolinaCarro(distanciaTotal);
        } else if(veiculo.equalsIgnoreCase("motocicleta")){
            return gastoGasolinaMoto(distanciaTotal);
        } else if(veiculo.equalsIgnoreCase("micro-ônibus")){
            return gastoGasolinaMicroOnibus(distanciaTotal);
        } else if (veiculo.equalsIgnoreCase("ônibus")) {
            return gastoGasolinaOnibus(distanciaTotal);
        } else if (veiculo.equalsIgnoreCase("caminhão")) {
            return gastoGasolinaCaminhao(distanciaTotal);
        } else {

        }
        return null;
    }


    private Double calculoAlimentacao(String tempo) {
        int tempoInt = Integer.parseInt(tempo);
        int tempoT = (int) Math.floor(tempoInt / 3);
        int contador = 0;
        int paradas = 0;
        Double valorGasto = 0.0;

        for (int i = 1; i <= tempoT; i++) {
            contador++;
            if (contador == 3) {
                paradas++;
                contador = 0;
            }
        }
        valorGasto = (double) (paradas * 51); // Considerando que o veículo possuirá 2 indivíduos  e cada um gaste 25.50
        return valorGasto;
    }

    private String converterHoras(Double tempo) {
        int horas = tempo.intValue();
        int minutos = (int) ((tempo - horas) * 60);

        String resultado = String.format("%02d:%02d", horas, minutos);

        return resultado;
    }

    public Double gastoGasolinaCarro(Double distanciaTotal){
        Double gasto = (distanciaTotal / 15) * 5.10;
        return gasto;
    }

    private Double gastoGasolinaMoto(Double distanciaTotal) {
        Double gasto = (distanciaTotal / 28) * 5.10;
        return gasto;
    }

    private Double gastoGasolinaMicroOnibus(Double distanciaTotal) {
        Double gasto = (distanciaTotal / 4.8) * 6;
        return gasto;
    }

    private Double gastoGasolinaOnibus(Double distanciaTotal) {
        Double gasto = (distanciaTotal / 4) * 6;
        return gasto;
    }

    private Double gastoGasolinaCaminhao(Double distanciaTotal) {
        Double gasto = (distanciaTotal / 3.4) * 6;
        return gasto;
    }
}
