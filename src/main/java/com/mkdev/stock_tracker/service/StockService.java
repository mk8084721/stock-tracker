package com.mkdev.stock_tracker.service;

import com.mkdev.stock_tracker.client.StockClient;
import com.mkdev.stock_tracker.dto.AlphaVantageResponse;
import com.mkdev.stock_tracker.dto.StockResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StockService {
    private final StockClient stockClient;
    public StockResponse getStockForSymbol(final String stockSymbol) {
        final AlphaVantageResponse response = stockClient.getStockQuote(stockSymbol);

        return StockResponse.builder()
                .symbol(response.globalQuote().symbol())
                .price(Double.parseDouble(response.globalQuote().price()))
                .lastUpdated(response.globalQuote().lastTradingDay())
                .build();
    }

}
