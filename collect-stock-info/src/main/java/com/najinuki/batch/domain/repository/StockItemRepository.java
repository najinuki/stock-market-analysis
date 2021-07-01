package com.najinuki.batch.domain.repository;

import com.najinuki.batch.domain.entity.StockItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StockItemRepository extends JpaRepository<StockItem, String> {
    List<StockItem> findAllByOrderByCodeAsc();
}
