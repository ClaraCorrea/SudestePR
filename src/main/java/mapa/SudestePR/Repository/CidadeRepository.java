package mapa.SudestePR.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import mapa.SudestePR.Entity.Cidade;

import java.util.List;

@Repository
public interface CidadeRepository extends JpaRepository<Cidade, Long>{
}
