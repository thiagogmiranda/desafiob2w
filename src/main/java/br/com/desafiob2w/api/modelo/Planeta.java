package br.com.desafiob2w.api.modelo;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

public class Planeta {
	@Id
	private ObjectId _id;

	private String nome;
	private String clima;
	private String terreno;

	public Planeta() {
	}

	public Planeta(String nome, String clima, String terreno) {
		super();
		this.nome = nome;
		this.clima = clima;
		this.terreno = terreno;
	}

	public String get_id() {
		if(_id == null)
			return null;
		
		return _id.toHexString();
	}

	public void set_id(ObjectId _id) {
		this._id = _id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getClima() {
		return clima;
	}

	public void setClima(String clima) {
		this.clima = clima;
	}

	public String getTerreno() {
		return terreno;
	}

	public void setTerreno(String terreno) {
		this.terreno = terreno;
	}
}
