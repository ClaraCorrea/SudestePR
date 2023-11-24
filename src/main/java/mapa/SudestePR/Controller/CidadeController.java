package mapa.SudestePR.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import mapa.SudestePR.DTO.CidadeDtoRequest;
import mapa.SudestePR.Service.CidadeService;

@RestController
@RequestMapping("/cidade")
public class CidadeController {
	@Autowired
	private CidadeService cidadeService;
	
	@PostMapping("/post")
	public void postCidade(@RequestBody CidadeDtoRequest cidadeDto) {
		cidadeService.saveCidade(cidadeDto);
	}
}
