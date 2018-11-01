package br.com.desafiob2w.api;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.desafiob2w.api.modelo.Planeta;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.apache.commons.lang3.RandomStringUtils;
import org.bson.types.ObjectId;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(properties = { "spring.data.mongodb.database=desafiob2wtest" })
public class PlanetsApiApplicationTests {

	@Autowired
	private MockMvc mvc;

	private ObjectMapper mapper = new ObjectMapper();

	@Test
	public void deveAdicionarUmPlanetaERetornarOJson() throws Exception {
		Planeta planeta = new Planeta(getRandom(), getRandom(), getRandom());

		invokeAdicionar(planeta).andExpect(status().isCreated())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("_id", is(notNullValue()))).andExpect(jsonPath("nome", is(planeta.getNome())))
				.andExpect(jsonPath("clima", is(planeta.getClima())))
				.andExpect(jsonPath("terreno", is(planeta.getTerreno())));
	}

	@Test
	public void deveRetornarTodosPlanetasCadastrados() throws Exception {
		mvc.perform(get("/api/planetas/").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
	}

	@Test
	public void deveBuscarERetornarUmPlanetaPeloNome() throws Exception {
		Planeta planeta = new Planeta(getRandom(), getRandom(), getRandom());

		invokeAdicionar(planeta).andExpect(status().isCreated());

		mvc.perform(get("/api/planetas/buscar/" + planeta.getNome())).andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("nome", is(planeta.getNome()))).andExpect(jsonPath("clima", is(planeta.getClima())))
				.andExpect(jsonPath("terreno", is(planeta.getTerreno())));
	}
	
	@Test
	public void deveRetornarStatusCodeNotFoundAoBuscarUmPlanetaPeloNome() throws Exception {
		mvc.perform(get("/api/planetas/buscar/" + getRandom())).andExpect(status().isNotFound());
	}

	@Test
	public void deveBuscarERetornarUmPlanetaPeloId() throws Exception {
		Planeta planeta = new Planeta(getRandom(), getRandom(), getRandom());
		Planeta salvo = getJsonResult(invokeAdicionar(planeta).andExpect(status().isCreated()).andReturn());

		mvc.perform(get("/api/planetas/" + salvo.get_id())).andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("nome", is(planeta.getNome()))).andExpect(jsonPath("clima", is(planeta.getClima())))
				.andExpect(jsonPath("terreno", is(planeta.getTerreno())));
	}
	
	@Test
	public void deveRetornarStatusCodeNotFoundAoBuscarUmPlanetaPeloId() throws Exception {
		mvc.perform(get("/api/planetas/" + ObjectId.get())).andExpect(status().isNotFound());
	}

	@Test
	public void deveRemoverUmPlaneta() throws Exception {
		Planeta planeta = new Planeta(getRandom(), getRandom(), getRandom());
		Planeta salvo = getJsonResult(invokeAdicionar(planeta).andExpect(status().isCreated()).andReturn());

		mvc.perform(get(String.format("/api/planetas/%s/remover", salvo.get_id()))).andExpect(status().isOk());
		mvc.perform(get("/api/planetas/" + salvo.get_id())).andExpect(status().isNotFound());
	}
	
	@Test
	public void deveRetornarStatusCodeNotFoundAoTentarRemoverUmPlanetaInexistente() throws Exception {
		mvc.perform(get(String.format("/api/planetas/%s/remover", getRandom()))).andExpect(status().isNotFound());
	}

	private Planeta getJsonResult(MvcResult mvcResult) throws Exception {
		return (Planeta) mapper.readerFor(Planeta.class).readValue(mvcResult.getResponse().getContentAsByteArray());
	}

	private ResultActions invokeAdicionar(Planeta planeta) throws Exception {
		return mvc.perform(
				post("/api/planetas/adicionar").content(toJson(planeta)).contentType(MediaType.APPLICATION_JSON));
	}

	private byte[] toJson(Object obj) throws JsonProcessingException {
		return mapper.writeValueAsString(obj).getBytes();
	}

	private String getRandom() {
		return RandomStringUtils.randomAlphabetic(10);
	}
}
