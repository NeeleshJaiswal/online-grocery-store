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
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class GroceryStoreServiceImpl implements GroceryStoreService {
    private final ItemRepository itemRepository;

    @Override
    public ReceiptDTO calculateOrder(List<ItemDTO> items) {
        log.info("Received order with {} items", items.size());
        double total = 0;
        List<String> breakdown = new ArrayList<>();

        for (ItemDTO dto : items) {
            Item dbItem = itemRepository.findByItemType(dto.getItemType())
                    .orElseThrow(() -> new IllegalArgumentException("Item type not found: " + dto.getItemType()));
            dto.setUnitPrice(dbItem.getUnitPrice());
            DiscountStrategy strategy = pickStrategy(dto.getItemType());
            double cost = strategy.calculateCost(dto);
            breakdown.add(buildLabel(dto) + ": €" + String.format("%.2f", cost));
            total = total+cost;
        }
        log.info("Order processed successfully. Total: €{}", total);
        return ReceiptDTO.builder()
                .withTotal(Double.parseDouble(String.format("%.2f", total)))
                .withBreakdown(breakdown)
                .build();
    }

    @Override
    public List<String> getDiscountRules() {
        log.info("Fetching discount rules");
        List<String> rules = new ArrayList<>();
        rules.add("BREAD: 0–2 days old => no discount, 3–5 days old => Buy 1 Take 2, 6 days old=> Pay 1 Take 3, more than 6 days old => not allowed.");
        rules.add("VEGETABLE: weight ≤100 grams => 5% off, 101 grams ≤ 500 grams => 7%, >500 grams => 10% off.");
        rules.add("BEER: 6 bottles => discount pack, Belgian=€3, Dutch=€2, German=€4, single unit bottle => normal price.");
        return rules;
    }

    @Override
    public List<ItemDTO> getPrices() {
        log.info("Fetching item prices from the database");
        List<ItemDTO> prices = itemRepository.findAll()
                .stream()
                .map(this::toDTO)
                .toList();
        log.info("Prices fetched successfully. Items: {}", prices);
        return prices;
    }

    private DiscountStrategy pickStrategy(ItemType type) {
        log.debug("Determining discount strategy for item type: {}", type);
        return switch (type) {
            case BREAD -> new BreadDiscountStrategy();
            case VEGETABLE -> new VegetableDiscountStrategy();
            case BEER -> new BeerDiscountStrategy();
            default -> throw new IllegalArgumentException("Unsupported item type: " + type);
        };
    }

    private String buildLabel(ItemDTO dto) {
        log.debug("Building label for item: {}", dto);
        return switch (dto.getItemType()) {
            case BREAD -> dto.getQuantity() + " x Bread (" + dto.getDetails() + ")";
            case VEGETABLE -> dto.getQuantity() + "g Vegetables";
            case BEER -> dto.getQuantity() + " x Beer (" + dto.getDetails() + ")";
        };
    }

    private ItemDTO toDTO(Item entity) {
        log.debug("Mapping Item entity to DTO: {}", entity);
        return ItemDTO.builder()
                .withItemType(entity.getItemType())
                .withName(entity.getName())
                .withUnitPrice(entity.getUnitPrice())
                .withQuantity(entity.getQuantity())
                .withDetails(entity.getDetails())
                .build();
    }
}