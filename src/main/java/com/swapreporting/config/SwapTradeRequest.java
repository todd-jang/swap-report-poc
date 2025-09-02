package com.swapreporting.config;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;

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
