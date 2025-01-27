package com.payaut.onlinegrocerystore.dto;

import com.payaut.onlinegrocerystore.entity.ItemType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true, setterPrefix = "with")
public class ItemDTO {

    @NotNull(message = "Item type cannot be null")
    private ItemType itemType;

    private String name;

    private Double unitPrice;

    @NotNull(message = "Quantity cannot be null")
    @DecimalMin(value = "1", message = "Quantity must be at least 1")
    private Integer quantity;

    private String details;
}
