package com.payaut.onlinegrocerystore.service.impl;

import com.payaut.onlinegrocerystore.discount.BeerDiscountStrategy;
import com.payaut.onlinegrocerystore.discount.BreadDiscountStrategy;
import com.payaut.onlinegrocerystore.discount.DiscountStrategy;
import com.payaut.onlinegrocerystore.discount.VegetableDiscountStrategy;
import com.payaut.onlinegrocerystore.dto.ItemDTO;
import com.payaut.onlinegrocerystore.dto.ReceiptDTO;
import com.payaut.onlinegrocerystore.entity.Item;
import com.payaut.onlinegrocerystore.entity.ItemType;
import com.payaut.onlinegrocerystore.repository.ItemRepository;
import com.payaut.onlinegrocerystore.service.GroceryStoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class GroceryStoreServiceImpl implements GroceryStoreService {
    private final ItemRepository itemRepository;

    @Override
    public ReceiptDTO calculateOrder(List<ItemDTO> items) {
        double total = 0;
        List<String> breakdown = new ArrayList<>();

        for (ItemDTO dto : items) {
            // Fetch the unit price based on itemType instead of name
            Item dbItem = itemRepository.findByItemType(dto.getItemType())
                    .orElseThrow(() -> new IllegalArgumentException("Item type not found: " + dto.getItemType()));

            // Set the unit price in the DTO
            dto.setUnitPrice(dbItem.getUnitPrice());

            // Apply the appropriate discount strategy
            DiscountStrategy strategy = pickStrategy(dto.getItemType());
            double cost = strategy.calculateCost(dto);

            // Build the breakdown line
            breakdown.add(buildLabel(dto) + ": €" + String.format("%.2f", cost));
            total += cost;
        }

        return ReceiptDTO.builder()
                .withTotal(Double.parseDouble(String.format("%.2f", total)))
                .withBreakdown(breakdown)
                .build();
    }

    @Override
    public List<String> getDiscountRules() {
        // Hard-coded or can be stored in DB, whichever you prefer
        List<String> rules = new ArrayList<>();
        rules.add("BREAD: 0–2 days old => no discount, 3–5 days old => Buy 1 Take 2, 6 days old=> Pay 1 Take 3, more than 6 days old => not allowed.");
        rules.add("VEGETABLE: weight ≤100 grams => 5% off, 101 grams ≤ 500 grams => 7%, >500 grams => 10% off.");
        rules.add("BEER: 6 bottles => discount pack, Belgian=€3, Dutch=€2, German=€4, single unit bottle => normal price.");
        return rules;
    }

    @Override
    public List<ItemDTO> getPrices() {
        // Retrieve items from DB and convert to DTO
        // These items might be "price definitions" rather than actual orders
        return itemRepository.findAll()
                .stream()
                .map(this::toDTO) // convert entity -> DTO
                .collect(Collectors.toList());
    }

    // Helper: pick discount strategy
    private DiscountStrategy pickStrategy(ItemType type) {
        return switch (type) {
            case BREAD -> new BreadDiscountStrategy();
            case VEGETABLE -> new VegetableDiscountStrategy();
            case BEER -> new BeerDiscountStrategy();
            default -> throw new IllegalArgumentException("Unsupported item type: " + type);
        };
    }


    // Helper: build a user-friendly label for breakdown
    private String buildLabel(ItemDTO dto) {
        return switch (dto.getItemType()) {
            case BREAD -> dto.getQuantity() + " x Bread (" + dto.getDetails() + ")";
            case VEGETABLE -> dto.getQuantity() + "g Vegetables";
            case BEER -> dto.getQuantity() + " x Beer (" + dto.getDetails() + ")";
        };
    }

    // Helper: convert entity -> DTO
    private ItemDTO toDTO(Item entity) {
        return ItemDTO.builder()
                .withItemType(entity.getItemType())
                .withName(entity.getName())
                .withUnitPrice(entity.getUnitPrice())
                .withQuantity(entity.getQuantity())
                .withDetails(entity.getDetails())
                .build();
    }
}