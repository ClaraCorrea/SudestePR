package mapa.SudestePR.Controller;

import mapa.SudestePR.DTO.RotaDtoResponse;
import mapa.SudestePR.Entity.CustomResponse;
import mapa.SudestePR.Entity.RotaResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import mapa.SudestePR.DTO.RotaDtoRequest;
import mapa.SudestePR.Service.RotaService;

@RestController
@RequestMapping("/rota")
public class RotaController {
	@Autowired
	private RotaService rotaService;

	@PostMapping("/rotas")
	public RotaResponse getRota(RotaDtoResponse rotaDtoResponse) {
		return rotaService.getRota(rotaDtoResponse);
	}
}
