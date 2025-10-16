package com.openclassrooms.chatop.api.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration for Swagger/OpenAPI documentation.
 * Sets up API documentation with JWT authentication support.
 */
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI chatopAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("ChâTop API")
                        .description("REST API for ChâTop rental property portal")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("ChâTop Team")
                                .email("contact@chatop.com")))
                .components(new Components()
                        .addSecuritySchemes("bearerAuth",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .description("JWT authentication token")));
    }
}
