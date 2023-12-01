package mapa.SudestePR.Repository;

import mapa.SudestePR.Entity.Cidade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import mapa.SudestePR.Entity.Rota;

@Repository
public interface RotaRepository extends JpaRepository<Rota, Long>{
    @Query("SELECT r FROM Rota r WHERE r.cidadeInicio.id = :cidadeInicio AND r.cidadeFim.id = :cidadeFim")
    Rota findCidadeById(@Param("cidadeInicio") Long cidadeInicio, @Param("cidadeFim") Long cidadeFim);

}
