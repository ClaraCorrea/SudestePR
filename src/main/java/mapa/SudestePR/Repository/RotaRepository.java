package mapa.SudestePR.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import mapa.SudestePR.Entity.Rota;

@Repository
public interface RotaRepository extends JpaRepository<Rota, Long>{

}
