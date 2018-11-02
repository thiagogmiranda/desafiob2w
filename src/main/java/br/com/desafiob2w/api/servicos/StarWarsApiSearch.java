package br.com.desafiob2w.api.servicos;

public class StarWarsApiSearch {
	private int count;
	private StarWarsApiPlanet[] results;

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public StarWarsApiPlanet[] getResults() {
		return results;
	}

	public void setResults(StarWarsApiPlanet[] results) {
		this.results = results;
	}

}
