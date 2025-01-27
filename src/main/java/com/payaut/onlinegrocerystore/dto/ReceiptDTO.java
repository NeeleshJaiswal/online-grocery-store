package com.payaut.onlinegrocerystore.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder(toBuilder = true, setterPrefix = "with")
public class ReceiptDTO {
    private Double total;         // Total cost after discounts
    private List<String> breakdown; // Breakdown of prices
}
