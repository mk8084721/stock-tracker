package com.mkdev.stock_tracker.controller;

import com.mkdev.stock_tracker.dto.*;
import com.mkdev.stock_tracker.entity.FavoriteStock;
import com.mkdev.stock_tracker.service.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/stocks")
@RequiredArgsConstructor
public class StockController {

    private final StockService stockService;

    @GetMapping("/{stockSymbol}")
    public StockResponse getStock(@PathVariable String stockSymbol){
        return stockService.getStockForSymbol(stockSymbol.toUpperCase());
    }

    @GetMapping("/{stockSymbol}/overview")
    public StockOverviewResponse getOverview(@PathVariable String stockSymbol){
        return stockService.getStockOverviewForSymbol(stockSymbol.toUpperCase());
    }
    @GetMapping("/{symbol}/history")
    public List<DailyStockResponse> getHistory(@PathVariable String symbol, @RequestParam(defaultValue = "30") int days){
        return stockService.getStockHistoryForSymbol(symbol.toUpperCase(), days);
    }
    @PostMapping("/favorites")
    public ResponseEntity<FavoriteStock> saveFavoriteStock(@RequestBody FavoriteStockRequest request){
        final FavoriteStock saved = stockService.addFavorite(request.getSymbol());
        return ResponseEntity.ok(saved);
    }

    @GetMapping("/favorites")
    public List<StockResponse> getFavoriteStocks(){
        return stockService.getFavoriteStocksWithLivePrices();
    }

}
