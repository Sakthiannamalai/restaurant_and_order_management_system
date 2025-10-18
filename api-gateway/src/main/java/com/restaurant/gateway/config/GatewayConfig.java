package com.restaurant.gateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class GatewayConfig {

//    @Bean
//    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
//        return builder.routes()
//                // Auth service (login/register are public, but other auth routes will still go through)
//                .route("auth-service", r -> r.path("/api/auth/**")
//                        .uri("http://restaurant-auth-service:8081"))
//
//                // Tenant Service
//                .route("tenant-service", r -> r.path("/api/tenants/**")
//                        .uri("http://restaurant-tenant-service:8082"))
//
//                // Menu Service
//                .route("menu-service", r -> r.path("/api/menu/**")
//                        .uri("http://restaurant-menu-service:8083"))
//
//                // Table Service
//                .route("table-service", r -> r.path("/api/tables/**")
//                        .uri("http://restaurant-table-service:8084"))
//
//                // Order Service
//                .route("order-service", r -> r.path("/api/orders/**")
//                        .uri("http://restaurant-order-service:8085"))
//
//                // Billing Service
//                .route("billing-service", r -> r.path("/api/billing/**")
//                        .uri("http://restaurant-billing-service:8086"))
//
//                // Kitchen Service
//                .route("kitchen-service", r -> r.path("/api/kitchen/**")
//                        .uri("http://restaurant-kitchen-service:8087"))
//
//                .build();
//    }

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()

                // Auth service
                .route("auth-service", r -> r.path("/api/auth/**")
                        .uri("http://localhost:8081"))

                // Tenant Service
                .route("tenant-service", r -> r.path("/api/tenants/**")
                        .uri("http://localhost:8082"))

                // Menu Service
                .route("menu-service", r -> r.path("/api/menu/**")
                        .uri("http://localhost:8083"))

                // Table Service
                .route("table-service", r -> r.path("/api/tables/**")
                        .uri("http://localhost:8084"))

                // Order Service
                .route("order-service", r -> r.path("/api/orders/**")
                        .uri("http://localhost:8085"))

                // Billing Service
                .route("billing-service", r -> r.path("/api/billing/**")
                        .uri("http://localhost:8086"))

                // Kitchen Service
                .route("kitchen-service", r -> r.path("/api/kitchen/**")
                        .uri("http://localhost:8087"))

                .build();
    }


}

