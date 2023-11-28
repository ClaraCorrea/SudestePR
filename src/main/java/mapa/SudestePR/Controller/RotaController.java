package mapa.SudestePR.Controller;

import mapa.SudestePR.Entity.Cidade;
import mapa.SudestePR.Entity.CustomResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import mapa.SudestePR.DTO.RotaDtoRequest;
import mapa.SudestePR.Service.RotaService;

import java.util.List;

@RestController
@RequestMapping("/rota")
public class RotaController {
	@Autowired
	private RotaService rotaService;
	
	@PostMapping("/post")
	public void postRota(@RequestBody RotaDtoRequest rotaDto) {
		rotaService.saveRota(rotaDto);
	}

	@GetMapping("/rota")
	public CustomResponse getRandomCars() {
		return new CustomResponse(true, "Operação realizada com sucesso!");
	}
}
