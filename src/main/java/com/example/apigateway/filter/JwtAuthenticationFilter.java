package com.example.apigateway.filter;

import com.example.apigateway.exceptions.CustomException;
import com.example.apigateway.util.JwtUtil;
import com.example.apigateway.validator.RouteValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class JwtAuthenticationFilter extends AbstractGatewayFilterFactory<JwtAuthenticationFilter.Config> {

    @Autowired
    private RouteValidator validator;

    @Autowired
    private JwtUtil jwtUtil;

    public JwtAuthenticationFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {
            System.out.println("Processing request in JwtAuthenticationFilter");

            // Kiểm tra nếu route cần được bảo mật
            if (validator.isSecured.test(exchange.getRequest())) {
                // Kiểm tra nếu không có Authorization header hoặc header rỗng
                if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                    throw new CustomException("Missing authorization header", HttpStatus.UNAUTHORIZED);
                }

                String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

                // Kiểm tra nếu authHeader null hoặc không bắt đầu bằng 'Bearer '
                if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                    throw new CustomException("Invalid authorization header", HttpStatus.UNAUTHORIZED);
                }

                // Tách lấy phần JWT từ Bearer token
                String jwtToken = authHeader.substring(7);
                System.out.println("JWT: " + jwtToken);

                try {
                    // Validate JWT token
                    jwtUtil.validateJwtToken(jwtToken);
                } catch (Exception e) {
                    System.out.println("Invalid access...!");
                    throw new CustomException("Unauthorized access to the application", HttpStatus.FORBIDDEN);
                }
            }
            return chain.filter(exchange);
        });
    }

    public static class Config {

    }
}