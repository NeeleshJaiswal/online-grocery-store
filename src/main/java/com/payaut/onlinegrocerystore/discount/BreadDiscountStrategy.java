package com.payaut.onlinegrocerystore.discount;

import com.payaut.onlinegrocerystore.dto.ItemDTO;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BreadDiscountStrategy implements DiscountStrategy {

    @Override
    public double calculateCost(ItemDTO itemDTO) {
        double unitPrice = itemDTO.getUnitPrice();
        int quantity = itemDTO.getQuantity();
        int daysOld = parseDaysOld(itemDTO.getDetails());

        if (daysOld <= 2) {
            return quantity * unitPrice; // No discount
        } else if (daysOld <= 5) {
            int paidLoaves = (quantity / 2) + (quantity % 2); // "Buy 1 Take 2"
            return paidLoaves * unitPrice;
        } else if (daysOld == 6) {
            int paidLoaves = (quantity / 3) + (quantity % 3); // "Pay 1 Take 3"
            return paidLoaves * unitPrice;
        } else {
            throw new IllegalArgumentException("Bread older than 6 days cannot be added to orders.");
        }
    }

    private int parseDaysOld(String details) {
        if (details == null) return 0;

        if (details.toLowerCase().contains("older than 6")) {
            return 7;
        }

        Matcher matcher = Pattern.compile("(\\d+)").matcher(details);
        if (matcher.find()) {
            return Integer.parseInt(matcher.group(1));
        }
        return 0;
    }
}
