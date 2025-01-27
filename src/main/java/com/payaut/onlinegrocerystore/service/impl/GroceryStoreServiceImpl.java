package com.payaut.onlinegrocerystore.service.impl;

import com.payaut.onlinegrocerystore.dto.ItemDTO;
import com.payaut.onlinegrocerystore.dto.OrderDTO;
import com.payaut.onlinegrocerystore.dto.ReceiptDTO;
import com.payaut.onlinegrocerystore.entity.DiscountRule;
import com.payaut.onlinegrocerystore.repository.DiscountRuleRepository;
import com.payaut.onlinegrocerystore.repository.ItemRepository;
import com.payaut.onlinegrocerystore.service.GroceryStoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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
    private final DiscountRuleRepository discountRuleRepository;

    public List<String> getDiscountRules() {
        return discountRuleRepository.findAll()
                .stream()
                .map(DiscountRule::getDescription)
                .collect(Collectors.toList());
    }

    public List<ItemDTO> getPrices() {
        return itemRepository.findAll()
                .stream()
                .map(item -> ItemDTO.builder()
                        .withName(item.getName())
                        .withPrice(item.getPrice())
                        .build())
                .collect(Collectors.toList());
    }

    public ReceiptDTO calculateOrder(OrderDTO orderDTO) {
        double total = 0;
        List<String> breakdown = new ArrayList<>();

        for (ItemDTO itemDTO : orderDTO.getItems()) {
            String itemName = itemDTO.getName().toLowerCase();

            switch (itemName) {
                case "bread":
                    // Bread logic: Quantity represents the number of loaves
                    double breadPrice = calculateBreadPrice(itemDTO);
                    breakdown.add(itemDTO.getQuantity() + " x Bread: €" + String.format("%.2f", breadPrice));
                    total += breadPrice;
                    break;

                case "vegetables":
                    // Vegetables logic: Quantity represents the weight in grams
                    double vegPrice = calculateVegetablePrice(itemDTO);
                    breakdown.add(itemDTO.getQuantity() + "g Vegetables: €" + String.format("%.2f", vegPrice));
                    total += vegPrice;
                    break;

                case "beer":
                    // Beer logic: Quantity represents the number of bottles
                    double beerPrice = calculateBeerPrice(itemDTO);
                    breakdown.add(itemDTO.getQuantity() + " x Beer: €" + String.format("%.2f", beerPrice));
                    total += beerPrice;
                    break;

                default:
                    throw new IllegalArgumentException("Unsupported item: " + itemDTO.getName());
            }
        }

        return ReceiptDTO.builder()
                .withTotal(Double.parseDouble(String.format("%.2f", total)))
                .withBreakdown(breakdown)
                .build();
    }

    private double calculateBreadPrice(ItemDTO itemDTO) {
        double unitPrice = 1.00;
        int quantity = itemDTO.getQuantity();

        if ("3 days old".equalsIgnoreCase(itemDTO.getDetails())) {
            return (quantity / 2) * unitPrice + (quantity % 2) * unitPrice; // Buy 1 Take 2
        } else if ("6 days old".equalsIgnoreCase(itemDTO.getDetails())) {
            return (quantity / 3) * unitPrice + (quantity % 3) * unitPrice; // Pay 1 Take 3
        } else {
            return quantity * unitPrice; // No discount
        }
    }

    private double calculateVegetablePrice(ItemDTO itemDTO) {
        double pricePerGram = 0.01;
        int weight = itemDTO.getQuantity();

        double discount;
        if (weight <= 100) {
            discount = 0.05; // 5% discount
        } else if (weight <= 500) {
            discount = 0.07; // 7% discount
        } else {
            discount = 0.10; // 10% discount
        }

        return weight * pricePerGram * (1 - discount);
    }

    private double calculateBeerPrice(ItemDTO itemDTO) {
        double unitPrice = 0.50;
        int quantity = itemDTO.getQuantity();

        if (quantity >= 6) {
            String type = itemDTO.getDetails().toLowerCase();
            double discount = switch (type) {
                case "belgian beer" -> 3.00;
                case "dutch beer" -> 2.00;
                case "german beer" -> 4.00;
                default -> 0.00; // No discount
            };
            return (quantity / 6) * (6 * unitPrice - discount) + (quantity % 6) * unitPrice;
        }

        return quantity * unitPrice; // No discount for single bottles
    }

//    private final ItemRepository itemRepository;
//
//    private final DiscountRuleRepository discountRuleRepository;
//
//    @Autowired
//    public GroceryStoreServiceImpl(ItemRepository itemRepository, DiscountRuleRepository discountRuleRepository) {
//        this.itemRepository = itemRepository;
//        this.discountRuleRepository = discountRuleRepository;
//    }
//
//    @Override
//    public List<String> getDiscountRules() {
//        return discountRuleRepository.findAll()
//                .stream()
//                .map(DiscountRule::getDescription)
//                .collect(Collectors.toList());
//    }
//
//    @Override
//    public List<ItemDTO> getPrices() {
//        return itemRepository.findAll()
//                .stream()
//                .map(item -> ItemDTO.builder()
//                        .withName(item.getName())
//                        .withQuantity(item.getQuantity())
//                        .withDetails(item.getDetails())
//                        .build())
//                .collect(Collectors.toList());
//    }
//
//    @Override
//    public ReceiptDTO calculateOrder(OrderDTO orderDTO) {
//        List<String> breakdown = new ArrayList<>();
//
//        // Use reduce to calculate the total
//        double total = orderDTO.getItems().stream()
//                .mapToDouble(itemDTO -> {
//                    double itemTotal = 1.00 * itemDTO.getQuantity();
//                    breakdown.add(itemDTO.getQuantity() + " x " + itemDTO.getName() + ": €" + itemTotal);
//                    return itemTotal;
//                })
//                .reduce(0, Double::sum);
//
//        return ReceiptDTO.builder()
//                .withTotal(total)
//                .withBreakdown(breakdown)
//                .build();
//    }
}
