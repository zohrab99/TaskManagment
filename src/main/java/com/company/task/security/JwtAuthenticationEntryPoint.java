package com.company.task.security;

import com.company.task.model.enums.Constant;
import com.company.task.model.view.Response;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Charsets;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

import static com.company.task.model.enums.ResponseEnum.*;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException exc) throws IOException, ServletException {
        final String expired = (String) httpServletRequest.getAttribute("expired");
        final String illegalArgument = (String) httpServletRequest.getAttribute("illegalArgument");
        final String invalidJWT = (String) httpServletRequest.getAttribute("invalidJWT");
        final String nonVerified = (String) httpServletRequest.getAttribute("nonVerified");
        final String invalidAlgorithm = (String) httpServletRequest.getAttribute("invalidAlgorithm");
        final String content;
        PrintWriter writer = httpServletResponse.getWriter();
        httpServletResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
        httpServletResponse.setCharacterEncoding(Charsets.UTF_8.name());

        if (expired != null){
            content = objectMapper.writeValueAsString(
                    Response.builder()
                            .status(AUTH_TOKEN_EXPIRED.getStatus())
                            .message(AUTH_TOKEN_EXPIRED.getMessage())
                            .data(Constant.AUTHENTICATION)
                            .build()
            );
            httpServletResponse.setStatus(AUTH_TOKEN_EXPIRED.getStatus());
        } else if (illegalArgument != null || invalidJWT != null || nonVerified != null || invalidAlgorithm != null){
            content = objectMapper.writeValueAsString(
                    Response.builder()
                            .status(INVALID_JWT_TOKEN.getStatus())
                            .message(INVALID_JWT_TOKEN.getMessage())
                            .data(Constant.AUTHENTICATION)
                            .build()
            );
            httpServletResponse.setStatus(INVALID_JWT_TOKEN.getStatus());
        } else {
            content = objectMapper.writeValueAsString(
                    Response.builder()
                            .status(AUTHENTICATION_REQUIRED_EXCEPTION.getStatus())
                            .message(AUTHENTICATION_REQUIRED_EXCEPTION.getMessage())
                            .data(Constant.AUTHENTICATION)
                            .build()
            );
            httpServletResponse.setStatus(AUTHENTICATION_REQUIRED_EXCEPTION.getStatus());
        }
        writer.write(content);
        writer.flush();
    }

}
