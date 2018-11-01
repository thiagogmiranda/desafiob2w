package br.com.desafiob2w.api.infraestrutura.repositorios;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import br.com.desafiob2w.api.modelo.Planeta;

public interface PlanetaRepositorio extends MongoRepository<Planeta, String> {
	Planeta findBy_id(ObjectId _id);
	Planeta findBynome(String nome);
}
