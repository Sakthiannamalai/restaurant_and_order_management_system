package com.restaurant.gateway.filters;

import com.restaurant.JwtUtils;
import io.jsonwebtoken.Claims;
import jakarta.ws.rs.core.HttpHeaders;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class AuthGlobalFilter implements GlobalFilter, Ordered {

    private JwtUtils jwtUtils;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getURI().getPath();
        System.out.println("Incoming request path: " + path);

        if (path.startsWith("/api/auth/login") || path.startsWith("/api/auth/register")) {
            System.out.println("Skipping auth for public endpoint: " + path);
            return chain.filter(exchange);
        }

        String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            System.out.println("Missing or invalid Authorization header");
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        String token = authHeader.substring(7);
        System.out.println("JWT token received: " + token);

        try {
            if (jwtUtils.validateJwtToken(token)) {
                Claims claims = jwtUtils.getClaims(token);
                String username = claims.getSubject();
                Long userId = ((Number) claims.get("userId")).longValue();
                String role = claims.get("role", String.class);
                Long tenantId = ((Number) claims.get("tenantId")).longValue();

                System.out.println("Authentication successful: " + username);

                ServerHttpRequest mutatedRequest = exchange.getRequest().mutate()
                        .header("X-User-Id", userId.toString())
                        .header("X-Username", username)
                        .header("X-Role", role)
                        .header("X-Tenant-Id", tenantId.toString())
                        .build();

                return chain.filter(exchange.mutate().request(mutatedRequest).build());
            } else {
                System.out.println("JWT token validation failed");
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }
        } catch (Exception e) {
            System.out.println("Exception while validating JWT token: " + e.getMessage());
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }
    }

    @Override
    public int getOrder() {
        return -1; // run early
    }
}
