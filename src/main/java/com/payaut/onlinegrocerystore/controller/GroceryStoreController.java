package com.payaut.onlinegrocerystore.controller;

import com.payaut.onlinegrocerystore.dto.ReceiptDTO;
import com.payaut.onlinegrocerystore.dto.ItemDTO;
import com.payaut.onlinegrocerystore.service.GroceryStoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/online/grocery")
public class GroceryStoreController {

    @Autowired
    private GroceryStoreService groceryStoreService;

    @GetMapping("/discounts")
    public List<String> getDiscountRules() {
        return groceryStoreService.getDiscountRules();
    }

    @GetMapping("/prices")
    public List<ItemDTO> getPrices() {
        return groceryStoreService.getPrices();
    }

    @PostMapping("/orders")
    public ReceiptDTO calculateOrder(@RequestBody final List<ItemDTO> items) {
        return groceryStoreService.calculateOrder(items);
    }
}
