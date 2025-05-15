package com.project.gatewayApi.filter;

import com.project.gatewayApi.exception.HeaderNotValid;
import com.project.gatewayApi.exception.NotAuthorize;
import com.project.gatewayApi.exception.UserToAuhtNotFound;
import com.project.gatewayApi.util.JWTService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Objects;

@Component
public class JwtAuthFilter extends AbstractGatewayFilterFactory<JwtAuthFilter.Config> {

    private final RouteValidator routeValidator;
    private final JWTService jwtService;
    private final WebClient webClient;

    @Autowired
    public JwtAuthFilter(RouteValidator routeValidator, JWTService jwtService, WebClient webClient) {
        super(Config.class);
        this.routeValidator = routeValidator;
        this.jwtService = jwtService;
        this.webClient = webClient;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {
            String username = "";
            String authHeader = "";
            if (routeValidator.isSecured.test(exchange.getRequest())) {
                if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                    throw new HeaderNotValid("Incorrect token header");
                }
                authHeader = Objects.requireNonNull(exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION)).getFirst();
                if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                    throw new HeaderNotValid("Incorrect token header");
                }
                String jwToken = authHeader.substring(7);
                username = jwtService.extractUsername(jwToken);
                if (username == null || jwtService.isTokenExpired(jwToken)) {
                    throw new NotAuthorize("Access not authorized");
                }
            }
            return webClient.get().uri("/verifyUser/" + username)
                    .header("Authorization", authHeader)
                    .retrieve()
                    .bodyToMono(Boolean.class)
                    .flatMap(flag -> {
                        if (Boolean.FALSE.equals(flag)) {
                            throw new NotAuthorize("Access not authorized");
                        }
                        return chain.filter(exchange);
                    }).onErrorResume(_ -> {
                        throw new UserToAuhtNotFound("User to authenticate not found");
                    });
        });
    }

    public static class Config {

    }
}
