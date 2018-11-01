package br.com.desafiob2w.api;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.desafiob2w.api.modelo.Planeta;

import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class PlanetsApiApplicationTests {

	@Autowired
	private MockMvc mvc;

	private ObjectMapper mapper = new ObjectMapper();

	@Test
	public void deveAdicionarUmPlanetaERetornarOJson() throws Exception {
		Planeta planeta = new Planeta("Planeta", "Temperado", "Acidentado");

		mvc.perform(post("/api/planetas/adicionar").content(toJson(planeta)).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("nome", is(planeta.getNome()))).andExpect(jsonPath("nome", is(planeta.getNome())))
				.andExpect(jsonPath("nome", is(planeta.getNome())));
	}

	@Test
	public void deveRetornarTodosPlanetasCadastrados() throws Exception {
		mvc.perform(get("/api/planetas/").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
	}

	@Test
	public void deveBuscarUmPlanetaPeloNome() throws Exception {

	}

	@Test
	public void deveBuscarUmPlanetaPeloId() throws Exception {

	}

	@Test
	public void deveRemoverUmPlaneta() throws Exception {

	}

	private byte[] toJson(Object obj) throws JsonProcessingException {
		return mapper.writeValueAsString(obj).getBytes();
	}
}
