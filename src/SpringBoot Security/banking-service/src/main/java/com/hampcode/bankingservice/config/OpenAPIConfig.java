package com.hampcode.bankingservice.config;

import java.util.List;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;


//http://localhost:8080/api/v1/swagger-ui/index.html
@Configuration
public class OpenAPIConfig {

    @Value("${hampcode.openapi.dev-url}")
    private String devUrl;

    @Bean
    public OpenAPI myOpenAPI() {
        // Definir el servidor de desarrollo
        Server devServer = new Server();
        devServer.setUrl(devUrl);
        devServer.setDescription("Server URL in Development environment");

        // Información de contacto
        Contact contact = new Contact();
        contact.setEmail("h4ampcode@gmail.com");
        contact.setName("HampCode");
        contact.setUrl("https://www.hampcode.com");

        // Licencia
        License mitLicense = new License().name("MIT License").url("https://choosealicense.com/licenses/mit/");

        // Información general de la API
        Info info = new Info()
                .title("Banking API")
                .version("1.0")
                .contact(contact)
                .description("This API exposes endpoints.")
                .termsOfService("https://www.hampcode.com/terms")
                .license(mitLicense);

        // Configuración de seguridad JWT
        SecurityScheme securityScheme = new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
                .name("JWT Authentication");
        Components components = new Components()
                .addSecuritySchemes("bearerAuth", securityScheme);

        // Requerimiento de seguridad para utilizar en las operaciones
        SecurityRequirement securityRequirement = new SecurityRequirement().addList("bearerAuth");


        return new OpenAPI()
                .info(info)
                .servers(List.of(devServer))
                .addSecurityItem(securityRequirement)
                .components(components);
    }
}

