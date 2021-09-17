package com.company.task.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.company.task.model.dto.LoginDTO;
import com.company.task.model.dto.LoginResponseDTO;
import com.company.task.model.enums.Constant;
import com.company.task.model.view.Response;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Charsets;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;

import static com.company.task.model.enums.ResponseEnum.*;

@Slf4j
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private final AuthenticationManager authenticationManager;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest req,
                                                HttpServletResponse res) throws AuthenticationException {
        try {
            log.debug("attemptAuthentication");
            LoginDTO loginRequest = objectMapper.readValue(req.getInputStream(), LoginDTO.class);
            log.info("loginDto: {}",loginRequest);
            return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword(),
                            new ArrayList<>())
            );
        } catch (IOException e) {
            throw new UsernameNotFoundException(INVALID_AUTHENTICATION.getMessage());
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest req,
                                            HttpServletResponse res,
                                            FilterChain chain,
                                            Authentication auth) throws IOException {
        log.debug("successfulAuthentication");
        UserDetails userPrincipal = (UserDetails) auth.getPrincipal();

        String token = JWT.create()
                .withSubject(userPrincipal.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + JwtProperties.JWT_TOKEN_EXPIRATION_TIME))
                .withClaim("username", userPrincipal.getUsername())
                .sign(Algorithm.HMAC512(JwtProperties.JWT_SECRET.getBytes()));
        final String content = objectMapper.writeValueAsString(
                Response.<LoginResponseDTO>builder()
                        .status(SUCCESS_AUTHENTICATION.getStatus())
                        .message(SUCCESS_AUTHENTICATION.getMessage())
                        .data(new LoginResponseDTO(token))
                        .build()
        );
        PrintWriter writer = res.getWriter();
        res.setContentType(MediaType.APPLICATION_JSON_VALUE);
        res.setCharacterEncoding(Charsets.UTF_8.name());
        writer.write(content);
        writer.flush();
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException {
        log.error("Security Error: {}", failed.getMessage());
        final String content;
        PrintWriter writer = response.getWriter();
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(Charsets.UTF_8.name());

        if(failed.getMessage().equals(INVALID_AUTHENTICATION.getMessage())){
            content = objectMapper.writeValueAsString(
                    Response.builder()
                            .status(INVALID_AUTHENTICATION.getStatus())
                            .message(INVALID_AUTHENTICATION.getMessage())
                            .data(Constant.AUTHENTICATION)
                            .build()
            );
            response.setStatus(INVALID_AUTHENTICATION.getStatus());
        } else {
            content = objectMapper.writeValueAsString(
                    Response.builder()
                            .status(ACCESS_DENIED.getStatus())
                            .message(ACCESS_DENIED.getMessage())
                            .data(Constant.AUTHORIZATION)
                            .build()
            );
            response.setStatus(ACCESS_DENIED.getStatus());
        }
        writer.write(content);
        writer.flush();
    }

}