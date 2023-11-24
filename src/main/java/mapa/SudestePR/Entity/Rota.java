package mapa.SudestePR.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
public class Rota {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double distancia;

    // Cidade de início da rota
    @ManyToOne
    @JoinColumn(name = "cidade_inicio_id")
    private Cidade cidadeInicio;

    // Cidade de destino da rota
    @ManyToOne
    @JoinColumn(name = "cidade_fim_id")
    private Cidade cidadeFim;

    public Rota(double distancia, Cidade cidadeInicio, Cidade cidadeFim) {
        this.distancia = distancia;
        this.cidadeInicio = cidadeInicio;
        this.cidadeFim = cidadeFim;
    }
}
