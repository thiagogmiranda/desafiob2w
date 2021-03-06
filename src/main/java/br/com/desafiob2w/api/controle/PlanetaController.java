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
import org.springframework.web.bind.annotation.RequestParam;
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

				if (jaEstaCadastrado(planeta.getNome())) {
					return new ResponseEntity<>(
							String.format("Já existe um planeta cadastrado com o nome '%s'.", planeta.getNome()),
							HttpStatus.BAD_REQUEST);
				}

				planeta.set_id(ObjectId.get());
				planeta.setAparicoesEmFilmes(starWarsApiServico.obterNumeroDeAparicoesEmFilmes(planeta.getNome()));

				planeta = planetaRepositorio.insert(planeta);

				return new ResponseEntity<>(planeta, HttpStatus.CREATED);
			} else {
				return new ResponseEntity<>("O planeta deve ter um nome, clima e terreno.", HttpStatus.BAD_REQUEST);
			}
		} catch (Exception ex) {
			ex.printStackTrace();

			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(value = { "", "/" }, params = "!nome")
	public @ResponseBody ResponseEntity<List<Planeta>> listarTodos() {
		try {
			List<Planeta> planetas = planetaRepositorio.findAll();

			return new ResponseEntity<>(planetas, HttpStatus.OK);
		} catch (Exception ex) {
			ex.printStackTrace();

			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(value = { "", "/" }, params = "nome")
	public @ResponseBody ResponseEntity<Planeta> buscarPorNome(
			@RequestParam(value = "nome", required = true) String nome) {
		try {
			Planeta planeta = planetaRepositorio.findBynome(nome);

			if (planeta == null) {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}

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

			return new ResponseEntity<>(planeta, HttpStatus.OK);
		} catch (Exception ex) {
			ex.printStackTrace();

			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(value = "/{id}/remover")
	public @ResponseBody ResponseEntity<String> remover(@PathVariable String id) {
		try {
			Planeta planeta = planetaRepositorio.findBy_id(toObjectId(id));

			if (planeta == null) {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}

			planetaRepositorio.delete(planeta);

			return new ResponseEntity<>("Planeta deletado", HttpStatus.OK);
		} catch (Exception ex) {
			ex.printStackTrace();

			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	private boolean jaEstaCadastrado(String nome) {
		return planetaRepositorio.findBynome(nome) != null;
	}

	private boolean dadosValidosParaAdicionar(Planeta planeta) {
		return planeta != null && !isNullOrWhiteSpace(planeta.getNome()) && !isNullOrWhiteSpace(planeta.getClima())
				&& !isNullOrWhiteSpace(planeta.getTerreno());
	}

	private boolean isNullOrWhiteSpace(String texto) {
		return texto == null || texto.trim().isEmpty();
	}

	private ObjectId toObjectId(String id) {
		try {
			return new ObjectId(id);
		} catch (Exception ex) {
			return null;
		}
	}
}
