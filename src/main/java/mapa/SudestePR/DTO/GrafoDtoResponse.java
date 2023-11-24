package mapa.SudestePR.DTO;

import java.util.List;

import lombok.Data;
import mapa.SudestePR.Entity.Cidade;
import mapa.SudestePR.Entity.Rota;

@Data
public class GrafoDtoResponse {
    private List<Cidade> cidades;
    private List<Rota> rotas;
}
