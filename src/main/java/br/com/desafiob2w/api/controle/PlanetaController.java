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
import org.springframework.web.client.RestTemplate;

import br.com.desafiob2w.api.infraestrutura.repositorios.PlanetaRepositorio;
import br.com.desafiob2w.api.modelo.Planeta;

@RestController
@RequestMapping("/api/planetas")
public class PlanetaController {

	@Autowired
	private PlanetaRepositorio planetaRepositorio;

	@RequestMapping(value = "/adicionar", method = RequestMethod.POST)
	public @ResponseBody ResponseEntity<Planeta> adicionar(@RequestBody Planeta planeta) {

		planeta.setNumeroAparicoesEmFilmes(obterNumeroDeAparicoesEmFilmes(planeta.getNome()));
		planeta.set_id(ObjectId.get());

		planeta = planetaRepositorio.insert(planeta);

		return new ResponseEntity<>(planeta, HttpStatus.CREATED);
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
		Planeta planeta = planetaRepositorio.findBy_id(toObjectId(id));

		if (planeta == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		planetaRepositorio.delete(planeta);

		return new ResponseEntity<>("Planeta deletado", HttpStatus.OK);
	}

	private int obterNumeroDeAparicoesEmFilmes(String nome) {
		RestTemplate rest = new RestTemplate();
		ResponseEntity<SWAPISearch> response = rest.getForEntity("http://swapi.co/api/planets/?search=" + nome,
				SWAPISearch.class);

		if (response.getStatusCode() == HttpStatus.OK) {
			SWAPISearch search = response.getBody();

			if (search.getCount() > 0) {
				return search.getResults()[0].getFilms().length;
			}
		}

		return 0;
	}

	private ObjectId toObjectId(String id) {
		try {
			return new ObjectId(id);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	class SWAPISearch {
		private int count;
		private SWAPIPlanet[] results;

		public int getCount() {
			return count;
		}

		public void setCount(int count) {
			this.count = count;
		}

		public SWAPIPlanet[] getResults() {
			return results;
		}

		public void setResults(SWAPIPlanet[] results) {
			this.results = results;
		}
	}

	class SWAPIPlanet {
		private Object[] films;

		public Object[] getFilms() {
			return films;
		}

		public void setFilms(Object[] films) {
			this.films = films;
		}
	}
}
