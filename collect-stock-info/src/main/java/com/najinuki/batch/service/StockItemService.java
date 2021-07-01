package com.najinuki.batch.service;

import com.najinuki.batch.domain.entity.StockItem;
import com.najinuki.batch.domain.repository.StockItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StockItemService {

    @Autowired
    private StockItemRepository stockItemRepository;

    public List<StockItem> getStockItemList() {
        return stockItemRepository.findAllByOrderByCodeAsc();
    }

}
