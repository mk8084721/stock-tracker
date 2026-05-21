package com.mkdev.stock_tracker.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record StockOverviewResponse(
        @JsonProperty("Symbol") String symbol,
        @JsonProperty("Name") String name,
        @JsonProperty("Description") String description,
        @JsonProperty("Industry") String industry,
        @JsonProperty("Sector") String sector,
        @JsonProperty("MarketCapitalization") String marketCap,
        @JsonProperty("PERatio") String peRatio,
        @JsonProperty("DividendYield") String dividendYield

){
}
