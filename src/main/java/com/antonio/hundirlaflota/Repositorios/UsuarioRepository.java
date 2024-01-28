package com.antonio.hundirlaflota.Repositorios;

import com.antonio.hundirlaflota.Modelos.Usuario;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends CrudRepository<Usuario, Long> {


    Optional<Usuario> findByEmail(String email);
}