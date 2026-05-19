package com.techchallenge.billing.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI billingServiceOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("Billing Service API")
                .description("Orçamentos e Pagamentos — FIAP Tech Challenge Fase 4")
                .version("1.0.0"));
    }
}
