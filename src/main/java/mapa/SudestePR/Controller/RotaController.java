package mapa.SudestePR.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import mapa.SudestePR.DTO.RotaDtoRequest;
import mapa.SudestePR.Service.RotaService;

@RestController
@RequestMapping("/rota")
public class RotaController {
	@Autowired
	private RotaService rotaService;
	
	@PostMapping("/post")
	public void postRota(@RequestBody RotaDtoRequest rotaDto) {
		rotaService.saveCidade(rotaDto);
	}
}
