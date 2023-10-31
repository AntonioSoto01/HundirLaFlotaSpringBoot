package com.antonio.hundirlaflota;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;


@Repository
public interface JugadorRepository extends CrudRepository<Jugador,Long>{
    Optional<Jugador> findByNombre(String nombre);

    Object getOne(long l);

}