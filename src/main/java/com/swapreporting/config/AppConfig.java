package com.swapreporting.config;

import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.annotations.media.Schema;

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

@Configuration
public class AppConfig {
    // Configuration beans or settings
}

@Schema(description = "Request payload for ingesting a swap trade")
public class SwapTradeRequest {

    @Schema(description = "Unique transaction identifier", example = "UTI123ABC456")
    private String uti;

    @Schema(description = "Asset class type", example = "interest-rate")
    private String assetClass;

    @Schema(description = "Notional amount of the trade", example = "1000000.00")
    private BigDecimal notionalAmount;

	public String getUti() {
		return uti;
	}

	public void setUti(String uti) {
		this.uti = uti;
	}

	public String getAssetClass() {
		return assetClass;
	}

	public void setAssetClass(String assetClass) {
		this.assetClass = assetClass;
	}

	public BigDecimal getNotionalAmount() {
		return notionalAmount;
	}

	public void setNotionalAmount(BigDecimal notionalAmount) {
		this.notionalAmount = notionalAmount;
	}
    
}