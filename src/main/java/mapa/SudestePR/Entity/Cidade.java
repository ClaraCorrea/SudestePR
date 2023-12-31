package mapa.SudestePR.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@Entity
public class Cidade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome")
    private String nome;

    @JsonIgnore
    @ToString.Exclude
    @OneToMany(mappedBy = "cidadeInicio")
    private List<Rota> rotasSaida;

    @JsonIgnore
    @ToString.Exclude
    @OneToMany(mappedBy = "cidadeFim")
    private List<Rota> rotasEntrada;

    public Cidade(String nome) {
        this.nome = nome;
        this.rotasEntrada = new ArrayList<>();
        this.rotasSaida = new ArrayList<>();
    }
}