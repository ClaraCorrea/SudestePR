package mapa.SudestePR.Service;

import mapa.SudestePR.Entity.Rota;
import mapa.SudestePR.Repository.RotaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GrafoRotaService {
    @Autowired
    private RotaRepository rotaRepository;

    public Object getRotasByCidades(Long cidadeInicio, Long cidadeFim) {
        return rotaRepository.findCidadeById(cidadeInicio, cidadeFim);
    }

    public List<Rota> getRotas() {
        List<Rota> rotas = rotaRepository.findAll();
        return rotas;
    }
}
