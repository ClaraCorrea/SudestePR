package mapa.SudestePR.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mapa.SudestePR.DTO.RotaDtoRequest;
import mapa.SudestePR.Entity.Rota;
import mapa.SudestePR.Repository.RotaRepository;

@Service
public class RotaService {
	
	@Autowired
	private RotaRepository rotaRepository;
	
	public void saveCidade(RotaDtoRequest rotaDtoRequest) {
		Rota rota = new Rota(
				rotaDtoRequest.getDistancia(),
				rotaDtoRequest.getCidadeInicio(),
				rotaDtoRequest.getCidadeFim()
				);
		rotaRepository.save(rota);
	}
}
