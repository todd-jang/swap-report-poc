package com.swapreporting.dto;

public class SwapTradeResponse {
    private String uti;
    private String message;

    public SwapTradeResponse(String uti, String message) {
        this.uti = uti;
        this.message = message;
    }

    public String getUti() {
        return uti;
    }

    public void setUti(String uti) {
        this.uti = uti;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
