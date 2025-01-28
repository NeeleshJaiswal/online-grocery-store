package com.payaut.onlinegrocerystore.controller;

import com.payaut.onlinegrocerystore.dto.ReceiptDTO;
import com.payaut.onlinegrocerystore.dto.ItemDTO;
import com.payaut.onlinegrocerystore.service.GroceryStoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/online/grocery")
public class GroceryStoreController {


    private final GroceryStoreService groceryStoreService;

    public GroceryStoreController(GroceryStoreService groceryStoreService) {
        this.groceryStoreService = groceryStoreService;
    }

    @GetMapping("/discounts")
    public ResponseEntity<List<String>> getDiscountRules() {
        List<String> discountRules = groceryStoreService.getDiscountRules();
        return ResponseEntity.ok(discountRules);
    }

    @GetMapping("/prices")
    public ResponseEntity<List<ItemDTO>> getPrices() {
        List<ItemDTO> prices = groceryStoreService.getPrices();
        return ResponseEntity.ok(prices);
    }

    @PostMapping("/orders")
    public ResponseEntity<ReceiptDTO> calculateOrder(@RequestBody final List<ItemDTO> items) {
        ReceiptDTO receipt = groceryStoreService.calculateOrder(items);
        return ResponseEntity.ok(receipt);
    }
}
