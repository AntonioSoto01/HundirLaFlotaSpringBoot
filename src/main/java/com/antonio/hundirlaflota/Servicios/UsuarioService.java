package com.antonio.hundirlaflota.Servicios;

import com.antonio.hundirlaflota.Modelos.Usuario;
import com.antonio.hundirlaflota.dto.UserDto;
import com.antonio.hundirlaflota.mappers.JuegoMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class UsuarioService {
    private final SecurityContextRepository securityContextRepository = new HttpSessionSecurityContextRepository();
    private final JuegoMapper juegoMapper;

    public void saveContext(Usuario user, HttpServletRequest request, HttpServletResponse response) {
        SecurityContext context = SecurityContextHolder.getContext();
        UserDto userDto = juegoMapper.usuarioToUserDto(user);
        context.setAuthentication(new UsernamePasswordAuthenticationToken(userDto, null, Collections.emptyList()));
        securityContextRepository.saveContext(context, request, response);
    }
}
