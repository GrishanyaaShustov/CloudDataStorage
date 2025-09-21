package com.study.clouddatastorage.configuration.jwtConfiguration;

import jakarta.persistence.Column;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class LoggingFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String method = request.getMethod(); // GET, POST, etc.
        String uri = request.getRequestURI(); // путь запроса

        // Передаем запрос дальше по цепочке фильтров
        filterChain.doFilter(request, response);

        // Логируем статус ответа уже после обработки запроса
        int status = response.getStatus();
        System.out.println(method + " " + uri + " " + status);
    }
}
