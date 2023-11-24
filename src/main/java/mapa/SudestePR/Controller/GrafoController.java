package mapa.SudestePR.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import mapa.SudestePR.DTO.GrafoDtoResponse;
import mapa.SudestePR.Service.GrafoService;

public class GrafoController {
	
	@Autowired
	private GrafoService grafoService;
	
	@GetMapping("/get")
	public GrafoDtoResponse getGrafo (@PathVariable Long id) {
		return grafoService.getById(id);
	}
}
