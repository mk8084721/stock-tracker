package com.mkdev.stock_tracker.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public record StockHistoryResponse(
        @JsonProperty("Meta Data") MetaData metaData,
        @JsonProperty("Time Series (Daily)") Map<String, DailyPrice> timeSeries
) {
    public record MetaData(
            @JsonProperty("1. Information") String information,
            @JsonProperty("2. Symbol") String symbol,
            @JsonProperty("3. Last Refreshed") String lastRefresh,
            @JsonProperty("4. Output Size") String outputSize,
            @JsonProperty("5. Time Zone") String timeZone
    ){}
    public record DailyPrice(
            @JsonProperty("1. open") String open,
            @JsonProperty("2. high") String high,
            @JsonProperty("3. low") String low,
            @JsonProperty("4. close") String close,
            @JsonProperty("5. volume") String volume
    ){}
}

