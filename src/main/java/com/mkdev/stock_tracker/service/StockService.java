package com.mkdev.stock_tracker.service;

import com.mkdev.stock_tracker.client.StockClient;
import com.mkdev.stock_tracker.dto.*;
import com.mkdev.stock_tracker.entity.FavoriteStock;
import com.mkdev.stock_tracker.exception.FavoriteAlreadyExistsException;
import com.mkdev.stock_tracker.repository.FavoriteStockRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class StockService {
    private final StockClient stockClient;
    private final FavoriteStockRepository favoriteRepository;

    @Cacheable(value = "stocks", key = "#stockSymbol")
    public StockResponse getStockForSymbol(final String stockSymbol) {
        log.info("getting Favorite stock: {}", stockSymbol);
        final AlphaVantageResponse response = stockClient.getStockQuote(stockSymbol);
        log.info("the response for Favorite stocks: {}", response);

        return StockResponse.builder()
                .symbol(response.globalQuote().symbol())
                .price(Double.parseDouble(response.globalQuote().price()))
                .lastUpdated(response.globalQuote().lastTradingDay())
                .build();
    }

    public StockOverviewResponse getStockOverviewForSymbol(final String stockSymbol) {
        return stockClient.getStockOverview(stockSymbol);
    }

    public List<DailyStockResponse> getStockHistoryForSymbol(final String symbol, int days) {
        StockHistoryResponse response = stockClient.getStockHistory(symbol);

        return response.timeSeries().entrySet().stream()
                .limit(days)
                .map(entry-> {
                    var date = entry.getKey();
                    var daily = entry.getValue();
                    return new DailyStockResponse(
                            date,
                            Double.parseDouble(daily.open()),
                            Double.parseDouble(daily.close()),
                            Double.parseDouble(daily.high()),
                            Double.parseDouble(daily.low()),
                            Long.parseLong(daily.volume())
                    );
                })
                .collect(Collectors.toList());

    }
    @Transactional
    public FavoriteStock addFavorite(String symbol) {
        if (favoriteRepository.existsBySymbol(symbol)){
            throw new FavoriteAlreadyExistsException(symbol);
        }
        FavoriteStock favorite = FavoriteStock.builder()
                .symbol(symbol.toUpperCase())
                .build();
        return favoriteRepository.save(favorite);
    }

    public List<FavoriteStock> getFavoriteStocks() {
        return favoriteRepository.findAll();
    }

    public List<StockResponse> getFavoriteStocksWithLivePrices() {
        List<FavoriteStock> favoriteStocks = favoriteRepository.findAll();

        log.info("Favorite stocks: {}", favoriteStocks);

        return favoriteStocks.stream()
                .map(favoriteStock -> getStockForSymbol(favoriteStock.getSymbol()))
                .collect(Collectors.toList());
    }
}
