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
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class RotaService {

    @Autowired
    private RotaRepository rotaRepository;
    @Autowired
    private GrafoService grafoService;

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
            if (gastoGasolina == null){
                return new RotaResponse(false, null, null, null, null, null,  "Veiculo não reconhecido");
            }

            String tempo = calculoTempo(veiculo, dist);
            Double alimentacao = calculoAlimentacao(tempo);
            if (alimentacao > 0){
                return new RotaResponse(true, rotaCompleta, dist, tempo, gastoGasolina, alimentacao,  "Operação realizada com sucesso!");
            } else {
                return new RotaResponse(true, rotaCompleta, dist, tempo, gastoGasolina, null,  "Operação realizada com sucesso!");
            }
        } else {
            return new RotaResponse(false,null, null, null, null, null, "Operação falhou! Caminho ou cidade não encontrado!");
        }
    }

    public String calculoTempo (String veiculo, Double dist){
    String tempoF = null;
        switch (veiculo) {
            case "carro":
                Double tempo = (dist / 90);
                tempoF = conversorTempo(tempo);
                break;
            case "motocicleta", "moto":
                tempo = (dist / 95);
                tempoF = conversorTempo(tempo);
                break;
            case "ônibus":
                tempo = (dist / 80);
                tempoF = conversorTempo(tempo);
                break;
            case "micro-ônibus":
                tempo = (dist / 85);
                tempoF = conversorTempo(tempo);
                break;
            case "caminhão":
                tempo = (dist / 75);
                tempoF = conversorTempo(tempo);
                break;
        }
        return (tempoF);
    }

    private Double gastoGasolina(String veiculo, Double distanciaTotal) {
        if(veiculo.equalsIgnoreCase("carro")){
            return gastoGasolinaCarro(distanciaTotal);
        } else if(veiculo.equalsIgnoreCase("motocicleta") || veiculo.equalsIgnoreCase("moto")){
            return gastoGasolinaMoto(distanciaTotal);
        } else if(veiculo.equalsIgnoreCase("micro-ônibus")){
            return gastoGasolinaMicroOnibus(distanciaTotal);
        } else if (veiculo.equalsIgnoreCase("ônibus")) {
            return gastoGasolinaOnibus(distanciaTotal);
        } else if (veiculo.equalsIgnoreCase("caminhão")) {
            return gastoGasolinaCaminhao(distanciaTotal);
        }
        return null;
    }

    private Double calculoAlimentacao(String tempo) {
        Pattern pattern = Pattern.compile("(\\d+)h (\\d+)min");
        Matcher matcher = pattern.matcher(tempo);

        int horas = 0;
        if (matcher.matches()) {
            horas = Integer.parseInt(matcher.group(1));
        } else {
            System.err.println("Formato inválido para tempo.");
        }

        int tempoT = (int) Math.floor(horas);
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

    private String conversorTempo(Double tempo){
        int minutos = (int) (tempo * 60);

        int horas = minutos / 60;
        minutos %= 60;

        return String.format("%dh %dmin", horas, minutos);
    }

    public Double gastoGasolinaCarro(Double distanciaTotal){
        Double gastoD = (distanciaTotal / 15) * 5.10;
        String gasto = String.format("%.2f", gastoD).replace(',', '.');;
        return Double.parseDouble(gasto);
    }

    private Double gastoGasolinaMoto(Double distanciaTotal) {
        Double gastoD = (distanciaTotal / 28) * 5.10;
        String gasto = String.format("%.2f", gastoD).replace(',', '.');;
        return Double.parseDouble(gasto);
    }

    private Double gastoGasolinaMicroOnibus(Double distanciaTotal) {
        Double gastoD = (distanciaTotal / 4.8) * 6;
        String gasto = String.format("%.2f", gastoD).replace(',', '.');;
        return Double.parseDouble(gasto);
    }

    private Double gastoGasolinaOnibus(Double distanciaTotal) {
        Double gastoD = (distanciaTotal / 4) * 6;
        String gasto = String.format("%.2f", gastoD).replace(',', '.');;
        return Double.parseDouble(gasto);
    }

    private Double gastoGasolinaCaminhao(Double distanciaTotal) {
        Double gastoD = (distanciaTotal / 3.4) * 6;
        String gasto = String.format("%.2f", gastoD).replace(',', '.');;
        return Double.parseDouble(gasto);
    }
}
