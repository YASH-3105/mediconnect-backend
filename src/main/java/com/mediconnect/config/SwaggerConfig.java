package com.mediconnect.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI mediConnectOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("MediConnect API")
                        .description("Healthcare Appointment & Management Platform")
                        .version("v1.0"));
    }
}