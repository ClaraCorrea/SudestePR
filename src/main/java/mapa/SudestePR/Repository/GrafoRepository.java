package mapa.SudestePR.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import mapa.SudestePR.Entity.Grafo;

@Repository
public interface GrafoRepository extends JpaRepository<Grafo, Long>{

}
