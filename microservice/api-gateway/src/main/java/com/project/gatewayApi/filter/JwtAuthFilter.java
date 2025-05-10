package com.project.gatewayApi.filter;

import com.project.gatewayApi.handler.HeaderNotValid;
import com.project.gatewayApi.handler.NotAuthorize;
import com.project.gatewayApi.util.JWTService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class JwtAuthFilter extends AbstractGatewayFilterFactory<JwtAuthFilter.Config> {

    @Autowired
    private RouteValidator routeValidator;

    @Autowired
    private JWTService jwtService;

    @Autowired
    public JwtAuthFilter(){
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {
            if (routeValidator.isSecured.test(exchange.getRequest())) {
                if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                    throw new HeaderNotValid("Incorrect token header");
                }
                String authHeader = Objects.requireNonNull(exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION)).getFirst();
                if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                    throw new HeaderNotValid("Incorrect token header");
                }
                authHeader = authHeader.substring(7);
                try {
                    jwtService.validateToken(authHeader);
                } catch (Exception e) {
                    throw new NotAuthorize("Access not authorized");
                }
            }
            return chain.filter(exchange);
        });
    }

    public static class Config {

    }
}
