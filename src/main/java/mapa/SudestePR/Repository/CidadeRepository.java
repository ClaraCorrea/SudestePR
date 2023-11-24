package mapa.SudestePR.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import mapa.SudestePR.Entity.Cidade;

@Repository
public interface CidadeRepository extends JpaRepository<Cidade, Long>{

}
