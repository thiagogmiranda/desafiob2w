package br.com.desafiob2w.api.controle;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import br.com.desafiob2w.api.infraestrutura.repositorios.PlanetaRepositorio;
import br.com.desafiob2w.api.modelo.Planeta;
import br.com.desafiob2w.api.servicos.StarWarsApiServico;

@RestController
@RequestMapping("/api/planetas")
public class PlanetaController {

	@Autowired
	private PlanetaRepositorio planetaRepositorio;

	@Autowired
	private StarWarsApiServico starWarsApiServico;

	@RequestMapping(value = "/adicionar", method = RequestMethod.POST)
	public @ResponseBody ResponseEntity<Object> adicionar(@RequestBody Planeta planeta) {

		try {
			if (dadosValidosParaAdicionar(planeta)) {
				planeta.set_id(ObjectId.get());

				planeta = planetaRepositorio.insert(planeta);
				planeta.setAparicoesEmFilmes(starWarsApiServico.obterNumeroDeAparicoesEmFilmes(planeta.getNome()));

				return new ResponseEntity<>(planeta, HttpStatus.CREATED);
			} else {
				return new ResponseEntity<>("O planeta deve ter um nome, clima e terreno.", HttpStatus.BAD_REQUEST);
			}
		} catch (Exception ex) {
			ex.printStackTrace();

			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(value = { "", "/" })
	public @ResponseBody ResponseEntity<List<Planeta>> listarTodos() {
		List<Planeta> planetas = planetaRepositorio.findAll();

		return new ResponseEntity<>(planetas, HttpStatus.OK);
	}

	@RequestMapping("/buscar/{nome}")
	public @ResponseBody ResponseEntity<Planeta> buscarPorNome(@PathVariable String nome) {
		try {
			Planeta planeta = planetaRepositorio.findBynome(nome);

			if (planeta == null) {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}

			planeta.setAparicoesEmFilmes(starWarsApiServico.obterNumeroDeAparicoesEmFilmes(nome));

			return new ResponseEntity<>(planeta, HttpStatus.OK);
		} catch (Exception ex) {
			ex.printStackTrace();

			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping("/{id}")
	public @ResponseBody ResponseEntity<Planeta> buscarPorId(@PathVariable String id) {
		try {
			Planeta planeta = planetaRepositorio.findBy_id(toObjectId(id));

			if (planeta == null) {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}

			planeta.setAparicoesEmFilmes(starWarsApiServico.obterNumeroDeAparicoesEmFilmes(planeta.getNome()));

			return new ResponseEntity<>(planeta, HttpStatus.OK);
		} catch (Exception ex) {
			ex.printStackTrace();

			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(value = "/{id}/remover")
	public @ResponseBody ResponseEntity<String> remover(@PathVariable String id) {
		Planeta planeta = planetaRepositorio.findBy_id(toObjectId(id));

		if (planeta == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		planetaRepositorio.delete(planeta);

		return new ResponseEntity<>("Planeta deletado", HttpStatus.OK);
	}
	
	private boolean dadosValidosParaAdicionar(Planeta planeta) {
		if(planeta == null)
			return false;
		if(planeta.getNome() == null || planeta.getNome().trim().isEmpty())
			return false;
		if(planeta.getClima() == null || planeta.getClima().trim().isEmpty())
			return false;
		if(planeta.getTerreno() == null || planeta.getTerreno().trim().isEmpty())
			return false;
	
		return true;
	}

	private ObjectId toObjectId(String id) {
		try {
			return new ObjectId(id);
		} catch (Exception ex) {
			return null;
		}
	}
}
