package com.cocoaromas.api.infrastructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI cocoaromasOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("Cocoaromas API")
                        .description("Base backend ecommerce alineada con frontend existente")
                        .version("v1")
                        .contact(new Contact().name("Cocoaromas Team")));
    }
}
