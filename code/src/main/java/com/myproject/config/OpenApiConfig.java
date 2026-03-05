package com.myproject.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * OpenAPI/Swagger configuration
 */
@Configuration
public class OpenApiConfig {
    
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Cart Coupon Management API")
                        .version("1.0.0")
                        .description("API for managing cart coupons - apply, remove, and validate coupons"))
                .servers(List.of(
                        new Server().url("http://localhost:8080/api").description("Local server")
                ));
    }
}
