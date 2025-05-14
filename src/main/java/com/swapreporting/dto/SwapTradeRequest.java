package com.swapreporting.dto;

import java.math.BigDecimal;

public class SwapTradeRequest {
    private String uti;
    private String assetClass;
    private BigDecimal notionalAmount;

    // Getters and setters

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