package com.payaut.onlinegrocerystore.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true, setterPrefix = "with")
public class ItemDTO {
    private String name;       // Name of the item (e.g., "Bread", "Vegetables", "Beer")
    private Integer quantity;  // Quantity or weight (in grams for vegetables)
    private String details;    // Additional details (e.g., "3 days old" for bread, "Dutch Beer" for beer)
    private Double price;      // Price per unit (for output only, optional in input)
}
