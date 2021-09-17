package com.company.task.security;

import com.company.task.model.enums.Constant;
import com.company.task.model.view.Response;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Charsets;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

import static com.company.task.model.enums.ResponseEnum.ACCESS_DENIED;

@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void handle(HttpServletRequest httpServletRequest,
                       HttpServletResponse httpServletResponse,
                       AccessDeniedException exc) throws IOException {
        final String content = objectMapper.writeValueAsString(
                Response.builder()
                        .status(ACCESS_DENIED.getStatus())
                        .message(ACCESS_DENIED.getMessage())
                        .data(Constant.AUTHORIZATION)
                        .build()
        );
        PrintWriter writer = httpServletResponse.getWriter();
        httpServletResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
        httpServletResponse.setCharacterEncoding(Charsets.UTF_8.name());
        writer.write(content);
        writer.flush();
    }

}
