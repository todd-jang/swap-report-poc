package com.swapreporting.dto;

import io.swagger.v3.oas.annotations.media.Schema;


@Schema(description = "Response returned after processing a trade")
public class SwapTradeResponse {

    @Schema(description = "Unique Trade Identifier", example = "UTI123ABC456")
    private String uti;

    @Schema(description = "Status or message for the operation", example = "Ingested successfully")
    private String message;

    public SwapTradeResponse(String uti, String message) {
        this.uti = uti;
        this.message = message;
    }

    public String getUti() {
        return uti;
    }

    public String getMessage() {
        return message;
    }
}