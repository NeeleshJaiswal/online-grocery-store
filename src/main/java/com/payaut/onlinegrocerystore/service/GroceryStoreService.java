package com.payaut.onlinegrocerystore.service;

import com.payaut.onlinegrocerystore.dto.ItemDTO;
import com.payaut.onlinegrocerystore.dto.ReceiptDTO;

import java.util.List;

public interface GroceryStoreService {

    ReceiptDTO calculateOrder(List<ItemDTO> items);

    List<String> getDiscountRules();

    List<ItemDTO> getPrices();
}
