package mapa.SudestePR.Entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Entity
public class Grafo {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
    private List<Cidade> cidades;
    private List<Rota> rotas;

    public Grafo(Long id) {
    	this.id = id;
        this.cidades = new ArrayList<>();
        this.rotas = new ArrayList<>();
    }
}