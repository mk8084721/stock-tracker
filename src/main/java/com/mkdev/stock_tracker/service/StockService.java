package com.mkdev.stock_tracker.service;

import com.mkdev.stock_tracker.client.StockClient;
import com.mkdev.stock_tracker.dto.*;
import com.mkdev.stock_tracker.entity.FavoriteStock;
import com.mkdev.stock_tracker.exception.FavoriteAlreadyExistsException;
import com.mkdev.stock_tracker.repository.FavoriteStockRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StockService {
    private final StockClient stockClient;
    private final FavoriteStockRepository favoriteRepository;

    public StockResponse getStockForSymbol(final String stockSymbol) {
        final AlphaVantageResponse response = stockClient.getStockQuote(stockSymbol);

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
        return favoriteStocks.stream()
                .map(favoriteStock -> getStockForSymbol(favoriteStock.getSymbol()))
                .collect(Collectors.toList());
    }
}
