package mapa.SudestePR.Service;

import mapa.SudestePR.Repository.RotaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GrafoRotaService {
    @Autowired
    private RotaRepository rotaRepository;

    public Object getRotasByCidades(Long cidadeInicio, Long cidadeFim) {
        return rotaRepository.findCidadeById(cidadeInicio, cidadeFim);
    }
}
