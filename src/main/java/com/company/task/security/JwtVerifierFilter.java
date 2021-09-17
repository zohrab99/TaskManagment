package com.company.task.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.AlgorithmMismatchException;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.company.task.service.UserPrincipalService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JwtVerifierFilter extends BasicAuthenticationFilter {

    private final UserPrincipalService userPrincipalService;

    public JwtVerifierFilter(AuthenticationManager authManager,
                             UserPrincipalService userPrincipalService) {
        super(authManager);
        this.userPrincipalService = userPrincipalService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req,
                                    HttpServletResponse res,
                                    FilterChain chain) throws IOException, ServletException {
        logger.debug("doFilterInternal");
        String header = req.getHeader(JwtProperties.HEADER_STRING);
        logger.info("header: "+header);
        if (header == null || !header.startsWith(JwtProperties.TOKEN_PREFIX)) {
            chain.doFilter(req, res);
            return;
        }
        UsernamePasswordAuthenticationToken authentication = getAuthentication(req);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(req, res);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
        logger.debug("getAuthentication");
        String token = request.getHeader(JwtProperties.HEADER_STRING);
        if (token != null) {
            try {
                final DecodedJWT decodedJWT = JWT
                        .require(Algorithm.HMAC512(JwtProperties.JWT_SECRET.getBytes()))
                        .build()
                        .verify(token.replace(JwtProperties.TOKEN_PREFIX, ""));

                final String username = decodedJWT.getClaim("username").asString();

                if(username == null) return null;
                String role=userPrincipalService.getRoleNameByUsername(username);
                final List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
                grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_" + role));
                return new UsernamePasswordAuthenticationToken(
                        username,
                        null,
                        grantedAuthorities
                );
            } catch (TokenExpiredException exc){
                request.setAttribute("expired",exc.getMessage());
            } catch (IllegalArgumentException exc){
                request.setAttribute("illegalArgument",exc.getMessage());
            } catch (JWTDecodeException exc){
                request.setAttribute("invalidJWT",exc.getMessage());
            } catch (SignatureVerificationException exc){
                request.setAttribute("nonVerified",exc.getMessage());
            } catch (AlgorithmMismatchException exc){
                request.setAttribute("invalidAlgorithm", exc.getMessage());
            }
        }
        return null;
    }

}