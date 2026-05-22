package com.dscb.reservas_restaurante.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI reservasOpenAPI() {

        return new OpenAPI()
                .info(new Info()
                        .title("Restaurant Reservation API")
                        .description("API REST para la gestión de reservas de restaurante")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Sebastián Cedano")
                                .url("https://github.com/sebascedano99")
                                .email("sebascedano99@gmail.com"))
                )
                .externalDocs(new ExternalDocumentation()
                        .description("Repositorio GitHub")
                        .url("https://github.com/sebascedano99/reservas-restaurante"));
    }
}
