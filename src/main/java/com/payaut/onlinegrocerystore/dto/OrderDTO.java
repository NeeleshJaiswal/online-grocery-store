package com.payaut.onlinegrocerystore.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder(toBuilder = true, setterPrefix = "with")
public class OrderDTO {
    private List<ItemDTO> items; // List of items in the order
}
