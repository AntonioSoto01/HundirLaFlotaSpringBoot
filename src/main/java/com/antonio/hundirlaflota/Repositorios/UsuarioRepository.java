package com.antonio.hundirlaflota.Repositorios;

import com.antonio.hundirlaflota.Modelos.Usuario;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends CrudRepository<Usuario,Long> {


}