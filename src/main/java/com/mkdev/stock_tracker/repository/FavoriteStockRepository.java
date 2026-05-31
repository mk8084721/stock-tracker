package com.mkdev.stock_tracker.repository;

import com.mkdev.stock_tracker.entity.FavoriteStock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FavoriteStockRepository extends JpaRepository<FavoriteStock,Long> {
    boolean existsBySymbol(String symbol);
}
