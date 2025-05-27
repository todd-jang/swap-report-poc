package com.example.swapreport.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI swapReportingOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("Swap Reporting POC API")
                .description("헥사고날 구조 기반 Swap Reporting API")
                .version("1.0.0"));
    }
}
