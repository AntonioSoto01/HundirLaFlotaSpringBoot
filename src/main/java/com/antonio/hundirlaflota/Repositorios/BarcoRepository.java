package com.antonio.hundirlaflota.Repositorios;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.antonio.hundirlaflota.Modelos.Barco;

import java.util.List;


@Repository
public interface BarcoRepository extends CrudRepository<Barco,Long>{

List<Barco> findByPosicion(Long id);
Barco findById(long id);

}