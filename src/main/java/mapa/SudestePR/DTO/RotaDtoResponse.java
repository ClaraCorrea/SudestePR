package mapa.SudestePR.DTO;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import mapa.SudestePR.Entity.Cidade;

@Data
@AllArgsConstructor
public class RotaDtoResponse {
    private String veiculo;
    private String cidadeInicio;
    private String cidadeFim;
    private Double distancia;
}
