package mapa.SudestePR.DTO;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mapa.SudestePR.Entity.Cidade;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RotaDtoRequest {
    private double distancia;

    @ManyToOne
    @JoinColumn(name = "cidade_inicio_id")
    private Cidade cidadeInicio;

    @ManyToOne
    @JoinColumn(name = "cidade_fim_id")
    private Cidade cidadeFim;
}
