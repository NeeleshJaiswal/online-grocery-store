package com.payaut.onlinegrocerystore.service;

import com.payaut.onlinegrocerystore.dto.ItemDTO;
import com.payaut.onlinegrocerystore.dto.OrderDTO;
import com.payaut.onlinegrocerystore.dto.ReceiptDTO;

import java.util.List;

public interface GroceryStoreService {

    List<String> getDiscountRules();

    List<ItemDTO> getPrices();

    ReceiptDTO calculateOrder(OrderDTO orderDTO);
}
