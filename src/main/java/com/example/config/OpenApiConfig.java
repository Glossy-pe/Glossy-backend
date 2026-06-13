package com.example.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Glossy API",
                version = "1.0",
                description = "Glossy backend API documentation",
                contact = @Contact(name = "Glossy Team", email = "support@example.com")
        ),
        servers = {
                @Server(url = "/api", description = "API base path")
        }
)
public class OpenApiConfig {

    @Bean
    public OpenAPI glossyOpenAPI() {
        return new OpenAPI()
                .info(new io.swagger.v3.oas.models.info.Info()
                        .title("Glossy API")
                        .version("1.0")
                        .description("Glossy backend API documentation")
                        .contact(new io.swagger.v3.oas.models.info.Contact()
                                .name("Glossy Team")
                                .email("support@example.com")
                        )
                        .license(new License().name("Apache 2.0").url("http://www.apache.org/licenses/LICENSE-2.0.html"))
                );
    }
}
