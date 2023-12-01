package mapa.SudestePR.Entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import mapa.SudestePR.DTO.RotaDtoRequest;

import java.util.List;

@Getter
@AllArgsConstructor
public class RotaResponse {
    private boolean success;
    List<String> cidadesCaminho;
    List<Rota> dist;
    private String message;
}