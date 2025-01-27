package com.payaut.onlinegrocerystore.discount;

import com.payaut.onlinegrocerystore.dto.ItemDTO;

public class VegetableDiscountStrategy implements DiscountStrategy {

    @Override
    public double calculateCost(ItemDTO itemDTO) {
        double unitPrice = itemDTO.getUnitPrice();
        int weightInGrams = itemDTO.getQuantity();
        double discount;
        if (weightInGrams <= 100) {
            discount = 0.05; // 5%
        } else if (weightInGrams <= 500) {
            discount = 0.07; // 7%
        } else {
            discount = 0.10; // 10%
        }

        double baseCost = weightInGrams * unitPrice;
        return baseCost * (1 - discount);
    }
}