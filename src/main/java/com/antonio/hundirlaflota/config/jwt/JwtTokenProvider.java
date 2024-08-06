package com.antonio.hundirlaflota.config.jwt;

import com.antonio.hundirlaflota.Excepciones.AppException;
import com.antonio.hundirlaflota.Modelos.Partida;
import com.antonio.hundirlaflota.Modelos.Usuario;
import com.antonio.hundirlaflota.Repositorios.PartidaRepository;
import com.antonio.hundirlaflota.Repositorios.UsuarioRepository;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.Collections;
import java.util.Date;

@RequiredArgsConstructor
@Component
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String secretKey;
    @Getter
    private static final long LONGEXPIRATIONTIME = 1000 * 60 * 60 * 24 * 10;
    @Getter
    private static final long SHORTEXPIRATIONTIME = 1000 * 5;
    @Getter
    private static final long MIDDLEEXPIRATIONTIME = 1000 * 60 * 15;
    private final UsuarioRepository usuarioRepository;
    private final PartidaRepository partidaRepository;

    @PostConstruct
    protected void init() {
        // this is to avoid having the raw secret key available in the JVM
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    public String generateToken(String email, long expirationTime) {
        Date now = new Date();
        Date expirationDate = new Date(now.getTime() + expirationTime);

        return JWT.create()
                .withSubject(email)
                .withIssuedAt(now)
                .withExpiresAt(expirationDate)
                .sign(Algorithm.HMAC512(secretKey));
    }


    public Authentication validateToken(String token) {
        Algorithm algorithm = Algorithm.HMAC512(secretKey);

        JWTVerifier verifier = JWT.require(algorithm)
                .build();

        DecodedJWT decoded = verifier.verify(token);
        Usuario user = usuarioRepository.findByEmail(decoded.getSubject()).orElseThrow(() -> new AppException("User not found", HttpStatus.NOT_FOUND));
        return new UsernamePasswordAuthenticationToken(user, null, Collections.emptyList());
    }

    public Partida validateTokenPartida(String token) {
        Algorithm algorithm = Algorithm.HMAC512(secretKey);

        JWTVerifier verifier = JWT.require(algorithm)
                .build();

        DecodedJWT decoded = verifier.verify(token);
        return partidaRepository.findById(Integer.parseInt(decoded.getSubject()));
    }
}
