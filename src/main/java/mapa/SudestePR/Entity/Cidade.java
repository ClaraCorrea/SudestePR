package mapa.SudestePR.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
public class Cidade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    @OneToMany(mappedBy = "cidadeInicio")
    private List<Rota> rotasSaida;

    @OneToMany(mappedBy = "cidadeFim")
    private List<Rota> rotasEntrada;

    public Cidade(String nome) {
        this.nome = nome;
        this.rotasEntrada = new ArrayList<>();
        this.rotasSaida = new ArrayList<>();
    }
}
