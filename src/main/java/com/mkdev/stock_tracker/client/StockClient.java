package com.mkdev.stock_tracker.client;

import com.mkdev.stock_tracker.config.WebClientConfig;
import com.mkdev.stock_tracker.dto.AlphaVantageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
public class StockClient {

    private final WebClient webClient;
    @Value("${alpha.vantage.api.key}")
    private String apiKey;

    public AlphaVantageResponse getStockQuote(String symbol){
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("function", "GLOBAL_QUOTE")
                        .queryParam("symbol", symbol)
                        .queryParam("apikey", apiKey)
                        .build()
                ).retrieve()
                .bodyToMono(AlphaVantageResponse.class)
                .block();
    }

}
