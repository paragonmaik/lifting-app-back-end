package com.hoister.tonshoister.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.hoister.tonshoister.models.User;
import com.hoister.tonshoister.repositories.UserRepository;
import com.hoister.tonshoister.services.PrincipalService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class SecurityFilter extends OncePerRequestFilter {

  @Autowired
  PrincipalService principalService;
  @Autowired
  TokenService tokenService;
  @Autowired
  UserRepository userRepository;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    String token = recoverToken(request);

    if (token != null) {
      String login = tokenService.validateToken(token);
      UserDetails user = userRepository.findByLogin(login);

      UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(user, null,
          user.getAuthorities());

      principalService.setAuthUser((User) user);
      SecurityContextHolder.getContext().setAuthentication(auth);
    }
    filterChain.doFilter(request, response);
  }

  private String recoverToken(HttpServletRequest request) {
    String authHeader = request.getHeader("Authorization");
    if (authHeader == null)
      return null;

    return authHeader.replace("Bearer ", "");
  }

}
