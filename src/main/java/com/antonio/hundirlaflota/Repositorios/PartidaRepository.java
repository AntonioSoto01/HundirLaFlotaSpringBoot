package com.antonio.hundirlaflota.Repositorios;

import com.antonio.hundirlaflota.Modelos.Partida;
import com.antonio.hundirlaflota.Modelos.Usuario;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface PartidaRepository extends CrudRepository<Partida, Long> {
    Partida findById(long id);

    Optional<Partida> findByTokenPartida(String ip);

    List<Partida> findByUsuarios(Usuario usuario);

}