package br.com.desafiob2w.api.controle;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import br.com.desafiob2w.api.modelo.Planeta;

@RestController
@RequestMapping("/api/planetas")
public class PlanetaController {

	@RequestMapping(value = "/adicionar", method = RequestMethod.POST)
	public @ResponseBody ResponseEntity<Planeta> adicionar(@RequestBody Planeta planeta) {
		return new ResponseEntity<Planeta>(planeta, HttpStatus.CREATED);
	}

	@RequestMapping("/")
	public @ResponseBody ResponseEntity<List<Planeta>> listarTodos() {
		return null;
	}

	@RequestMapping("/{nome}")
	public @ResponseBody ResponseEntity<Planeta> buscarPorNome(@PathVariable String nome) {
		return null;
	}
	
	@RequestMapping("/{id}")
	public @ResponseBody ResponseEntity<Planeta> buscarPorId(@PathVariable Long id) {
		return null;
	}
	
	@RequestMapping(value = "/{id}/remover")
	public @ResponseBody ResponseEntity<String> remover(@PathVariable String id) {
		return new ResponseEntity<String>("", HttpStatus.OK);
	}
}
