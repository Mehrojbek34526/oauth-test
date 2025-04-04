package uz.pdp.oauthexample.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 Created by: Mehrojbek
 DateTime: 14/02/25 21:51
 **/
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customizeOpenAPI() {
        final String securitySchemeName = "bearerAuth";
        return new OpenAPI()
                .addSecurityItem(new SecurityRequirement()
                        .addList(securitySchemeName))
                .components(new Components()
                        .addSecuritySchemes(securitySchemeName, new SecurityScheme()
                                .name(securitySchemeName)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")));
    }

//    @Bean
//    public OpenAPI openAPI() {
//        OpenAPI openAPI = new OpenAPI();
//
//        Info info = new Info();
//        info.setTitle("Rest Security Test");
//        info.setVersion("v1");
//        info.setDescription("Ushbu project rest Security Test uchun yaratilgan");
//
//        Contact contact = new Contact();
//        contact.setEmail("mehrojbekdeveloper@gmail.com");
//        contact.setName("Mehrojbek");
//
//        info.setContact(contact);
//
//        License license = new License();
//        license.setName("Apache 2.0");
//
//        info.setLicense(license);
//
//        openAPI.setInfo(info);
//
//        Server testServer = new Server();
//        testServer.setDescription("Test server");
//        testServer.setUrl("http://localhost:8080/");
//
//        Server prodServer = new Server();
//        prodServer.setDescription("Prod server");
//        prodServer.setUrl("http://localhost:9090/");
//
//        openAPI.setServers(List.of(testServer, prodServer));
//
////        openAPI.setSecurity();
//
//        final String securitySchemeName = "bearerAuth";
//
//        openAPI.addSecurityItem(new SecurityRequirement()
//                .addList(securitySchemeName));
//
//        Components components = new Components();
//
//        SecurityScheme securitySchemesItem = new SecurityScheme();
//        securitySchemesItem.setName(securitySchemeName);
//        securitySchemesItem.setBearerFormat("JWT");
//        securitySchemesItem.setScheme("bearer");
//
//        securitySchemesItem.setType(SecurityScheme.Type.HTTP);
//
//        components.addSecuritySchemes("Authorization", securitySchemesItem);
//
//        openAPI.setComponents(components);
//
//        return openAPI;
//    }

}
