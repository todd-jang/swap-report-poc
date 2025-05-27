
package com.swapreporting.domain.alert.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Alert {

    private String tradeId;
    private String message;
    private AlertType type;

    public Alert() {} // 기본 생성자

    @JsonCreator
    public Alert(
            @JsonProperty("tradeId") String tradeId,
            @JsonProperty("message") String message,
            @JsonProperty("type") AlertType type) {
        this.tradeId = tradeId;
        this.message = message;
        this.type = type;
    }

    // Getters and Setters
    public String getTradeId() {
        return tradeId;
    }

    public void setTradeId(String tradeId) {
        this.tradeId = tradeId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public AlertType getType() {
        return type;
    }

    public void setType(AlertType type) {
        this.type = type;
    }
}
