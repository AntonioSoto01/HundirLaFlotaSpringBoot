package com.antonio.hundirlaflota.Repositorios;

import com.antonio.hundirlaflota.Modelos.Partida;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface PartidaRepository extends CrudRepository<Partida, Long> {
    Partida findById(long id);

    Partida findByTokenPartida(String ip);


}