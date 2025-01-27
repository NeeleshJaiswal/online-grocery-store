package com.payaut.onlinegrocerystore.dto;


import lombok.*;

import java.util.List;

@Data
@Builder(toBuilder = true, setterPrefix = "with")
public class ReceiptDTO {
    private double total;
    private List<String> breakdown;
}
