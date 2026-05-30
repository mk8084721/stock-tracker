package com.mkdev.stock_tracker.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record DailyStockResponse(
        String date,
        double open,
        double close,
        double high,
        double low,
        long volume
) {
}
