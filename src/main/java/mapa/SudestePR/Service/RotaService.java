package mapa.SudestePR.Service;

import mapa.SudestePR.DTO.RotaDtoResponse;
import mapa.SudestePR.Entity.Cidade;
import mapa.SudestePR.Entity.RotaResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mapa.SudestePR.DTO.RotaDtoRequest;
import mapa.SudestePR.Entity.Rota;
import mapa.SudestePR.Repository.RotaRepository;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.aspectj.runtime.internal.Conversions.intValue;

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
            String tempo = calculoTempo(veiculo, dist);
            Double alimentacao = calculoAlimentacao(tempo);
            if (alimentacao > 0){
                return new RotaResponse(true, rotaCompleta, dist, tempo, gastoGasolina, alimentacao,  "Operação realizada com sucesso!");
            } else {
                return new RotaResponse(true, rotaCompleta, dist, tempo, gastoGasolina, alimentacao,  "Operação realizada com sucesso!");
            }
        } else {
            return new RotaResponse(false,null, null, null, null, null, "Operação falhou! Caminho não encontrado");
        }
    }

    public String calculoTempo (String veiculo, Double dist){
        int horas =0;
        int minutos =0;
        switch (veiculo) {
            case "carro":
                Double tempo = (dist / 90);
                horas = converterHoras(tempo);
                minutos = converterMinutos(horas);
                break;
            case "motocicleta":
                tempo = (dist / 95);
                horas = converterHoras(tempo);
                minutos = converterMinutos(horas);
                break;
            case "ônibus":
                tempo = (dist / 80);
                horas = converterHoras(tempo);
                minutos = converterMinutos(horas);
                break;
            case "micro-ônibus":
                tempo = (dist / 85);
                horas = converterHoras(tempo);
                minutos = converterMinutos(horas);
                break;
            case "caminhão":
                tempo = (dist / 75);
                horas = converterHoras(tempo);
                minutos = converterMinutos(horas);
                break;
        }
        return (horas+":"+minutos);
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
        Pattern pattern = Pattern.compile("(\\d+):(\\d+)");
        Matcher matcher = pattern.matcher(tempo);

        int horas = 0;
        if (matcher.matches()) {
            horas = Integer.parseInt(matcher.group(1));
        } else {
            System.err.println("Formato inválido para tempo.");
        }

        int tempoT = (int) Math.floor(horas / 3);
        int contador = 0;
        int paradas = 0;
        Double valorGasto = 0.0;

        for (int i = 1; i <= tempoT; i++) {
            contador++;
            System.out.println("Contador - fora if: " + contador);
            if (contador == 3) {
                System.out.println("Contador - dentro if: " + contador);
                paradas++;
                contador = 0;
                System.out.println("Contador final if: " + contador);
            }
        }
        valorGasto = (double) (paradas * 51); // Considerando que o veículo possuirá 2 indivíduos  e cada um gaste 25.50
        return valorGasto;
    }

    private int converterHoras(Double tempo) {
        int horas = tempo.intValue();
        System.out.println("ConverterHoras "+horas);
        return (horas);
    }

    private int converterMinutos(int horas){
        int minutos = (int) ((horas - horas) * 60);
        System.out.println("ConverterMinutos "+minutos);
        return minutos;
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
