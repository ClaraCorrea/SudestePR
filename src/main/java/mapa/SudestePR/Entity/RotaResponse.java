package mapa.SudestePR.Entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import mapa.SudestePR.DTO.RotaDtoRequest;

import java.util.List;

@Getter
@AllArgsConstructor
public class RotaResponse {
    private boolean success;
    List<Rota> Cidades_Caminho;
    private Double distancia_Total;
    private String tempo_de_Viagem;
    private Double gasto_de_Gasolina;
    private Double gasto_de_Alimentacao;
    private String message;
}