package mapa.SudestePR.Controller;

import mapa.SudestePR.Entity.Cidade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import mapa.SudestePR.Service.CidadeService;

import java.util.List;

@RestController
@RequestMapping("/cidade")
public class CidadeController {
	@Autowired
	private CidadeService cidadeService;

	@GetMapping("/getCars")
	public ResponseEntity<List<Cidade>> getRandomCars() {
		List<Cidade> cidades = cidadeService.getCidades();
		return ResponseEntity.ok(cidades);
	}
}
