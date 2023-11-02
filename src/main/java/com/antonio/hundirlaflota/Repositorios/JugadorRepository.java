package com.antonio.hundirlaflota.Repositorios;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.antonio.hundirlaflota.Modelos.Jugador;

import java.util.List;
import java.util.Optional;


@Repository
public interface JugadorRepository extends CrudRepository<Jugador,Long>{
   Jugador findByNombre(String nombre);
Jugador findById(long id);
    Object getOne(long l);

}