package mapa.SudestePR.DTO;

import lombok.Data;
import mapa.SudestePR.Entity.Cidade;

@Data
public class RotaDtoRequest {
    private double distancia;
    private Cidade cidadeInicio;
    private Cidade cidadeFim;
}
