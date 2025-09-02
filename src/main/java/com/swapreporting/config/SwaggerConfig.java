package com.swapreporting.config;

import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@Configuration
@OpenAPIDefinition(
    info = @Info(
        title = "Swap Reporting API",
        version = "1.0",
        description = "Endpoints for swap ingestion, validation, reporting and corrections"
    )
)
public class SwaggerConfig {
    // Customizations if needed
}
