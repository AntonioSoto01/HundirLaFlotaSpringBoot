package com.antonio.hundirlaflota.mappers;

import com.antonio.hundirlaflota.Modelos.Usuario;
import com.antonio.hundirlaflota.dto.LoginDto;
import com.antonio.hundirlaflota.dto.UsuarioDto;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface JuegoMapper {

    Usuario loginToUsuario(LoginDto login);

    Usuario usarioDtoToUsuario(UsuarioDto usuarioDto);
}


