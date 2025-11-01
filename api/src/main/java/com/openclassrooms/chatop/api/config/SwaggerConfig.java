package com.openclassrooms.chatop.api.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration for Swagger/OpenAPI documentation.
 * Sets up API documentation with JWT authentication support.
 */
@Configuration
@OpenAPIDefinition(
        security = @SecurityRequirement(name = "bearerAuth") // appliqué globalement
)
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT"
)
public class SwaggerConfig {

    @Bean
    public OpenAPI chatopAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("ChâTop API")
                        .description("REST API for ChâTop rental property portal")
                        .version("1.0.0")
                        .contact(new Contact().name("ChâTop Team"))
                );
    }
}
