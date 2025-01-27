package com.payaut.onlinegrocerystore.discount;
import com.payaut.onlinegrocerystore.dto.ItemDTO;

public class BeerDiscountStrategy implements DiscountStrategy {

    @Override
    public double calculateCost(ItemDTO itemDTO) {
        double unitPrice = itemDTO.getUnitPrice();
        int quantity = itemDTO.getQuantity();
        String type = itemDTO.getDetails().toLowerCase();

        double packCost;
        if (type.contains("belgian")) {
            packCost = 3.00;
        } else if (type.contains("dutch")) {
            packCost = 2.00;
        } else if (type.contains("german")) {
            packCost = 4.00;
        } else {
            packCost = 3.00;
        }

        int packs = quantity / 6;
        int leftover = quantity % 6;
        double totalPackCost = packs * packCost;
        double leftoverCost = leftover * unitPrice;

        return totalPackCost + leftoverCost;
    }
}
