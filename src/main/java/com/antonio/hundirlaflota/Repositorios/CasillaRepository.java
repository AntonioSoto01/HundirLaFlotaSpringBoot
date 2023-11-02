package com.antonio.hundirlaflota.Repositorios;


import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.antonio.hundirlaflota.Modelos.Casilla;

import java.util.List;
import java.util.Optional;


@Repository
public interface CasillaRepository extends CrudRepository<Casilla,Long>{


}