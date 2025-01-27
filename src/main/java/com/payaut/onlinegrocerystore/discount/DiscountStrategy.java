package com.payaut.onlinegrocerystore.discount;

import com.payaut.onlinegrocerystore.dto.ItemDTO;

public interface DiscountStrategy {
    double calculateCost(ItemDTO itemDTO);
}