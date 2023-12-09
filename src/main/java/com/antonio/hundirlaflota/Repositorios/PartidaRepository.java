package com.antonio.hundirlaflota.Repositorios;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.antonio.hundirlaflota.Modelos.Jugador;
import com.antonio.hundirlaflota.Modelos.Partida;



@Repository
public interface PartidaRepository extends CrudRepository<Partida,Long>{
Partida findById(long id);


}