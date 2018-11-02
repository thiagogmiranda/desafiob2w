package br.com.desafiob2w.api.servicos;

import java.util.Collections;

import javax.annotation.ManagedBean;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

@ManagedBean
public class StarWarsApiServico {	
	public int obterNumeroDeAparicoesEmFilmes(String planeta) {
		RestTemplate rest = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
	    headers.setContentType(MediaType.APPLICATION_JSON);
		headers.add("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:63.0) Gecko/20100101 Firefox/63.0");
		
		HttpEntity<StarWarsApiSearch> entity = new HttpEntity<>(headers);
		String url = String.format("https://swapi.co/api/planets/?search=%s&format=json", planeta);
		
		HttpEntity<StarWarsApiSearch> response = rest.exchange(url, HttpMethod.GET, entity, StarWarsApiSearch.class);

		StarWarsApiSearch search = response.getBody();
		
		if (search != null && search.getCount() > 0) {
				return search.getResults()[0].getFilms().length;
		}

		return 0;
	}
}
