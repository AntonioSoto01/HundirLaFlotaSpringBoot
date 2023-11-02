package com.antonio.hundirlaflota.Repositorios;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.antonio.hundirlaflota.Modelos.Jugador;

import java.util.List;
import java.util.Optional;


@Repository
public interface JugadorRepository extends CrudRepository<Jugador,Long>{
    Optional<Jugador> findByNombre(String nombre);

    Object getOne(long l);

}