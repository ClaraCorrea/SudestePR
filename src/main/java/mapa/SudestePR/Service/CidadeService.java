package mapa.SudestePR.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import mapa.SudestePR.DTO.CidadeDtoRequest;
import mapa.SudestePR.Entity.Cidade;
import mapa.SudestePR.Repository.CidadeRepository;
import mapa.SudestePR.Entity.Rota;

@Service
public class CidadeService {

	@Autowired
	private CidadeRepository cidadeRepository;
	
	public void saveCidade(CidadeDtoRequest cidadeDtoRequest) {
		Cidade cidade = new Cidade(
				cidadeDtoRequest.getNome()	
				);
		cidadeRepository.save(cidade);
	}

    public List<Cidade> getCidades() {
        List<Cidade> cidades = cidadeRepository.findAll();
        return cidades;
    }

}