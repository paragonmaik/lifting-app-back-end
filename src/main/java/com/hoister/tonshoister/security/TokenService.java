package com.hoister.tonshoister.security;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.hoister.tonshoister.models.User;

@Service
public class TokenService {
  @Value("${api.security.token.secret}")
  private String secret;

  public String generateToken(User user) {
    try {
      Algorithm algo = Algorithm.HMAC256(secret);
      String token = JWT.create()
          .withIssuer("ton-app")
          .withSubject(user.getLogin())
          .withExpiresAt(tokenExpirationDate())
          .sign(algo);

      return token;
    } catch (JWTCreationException e) {
      throw new RuntimeException("Error while generating token", e);
    }
  }

  public String validateToken(String token) {
    try {
      Algorithm algo = Algorithm.HMAC256(secret);
      return JWT.require(algo)
          .withIssuer("ton-app")
          .build()
          .verify(token)
          .getSubject();
    } catch (Exception e) {
      return "";
    }
  }

  private Instant tokenExpirationDate() {
    return LocalDateTime.now().plusHours(168).toInstant(ZoneOffset.of("-03:00"));
  }
}
