package com.payaut.onlinegrocerystore.config;

import com.payaut.onlinegrocerystore.entity.Item;
import com.payaut.onlinegrocerystore.entity.ItemType;
import com.payaut.onlinegrocerystore.repository.ItemRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataLoader {

    @Bean
    CommandLineRunner loadData(ItemRepository itemRepository) {
        return args -> {
            itemRepository.save(Item.builder()
                    .withItemType(ItemType.BREAD)
                    .withName("Bread")
                    .withUnitPrice(1.00)
                    .withQuantity(1)
                    .withDetails("Price per loaf")
                    .build());

            itemRepository.save(Item.builder()
                    .withItemType(ItemType.VEGETABLE)
                    .withName("Vegetables")
                    .withUnitPrice(0.01)
                    .withQuantity(1)
                    .withDetails("Price per gram")
                    .build());

            itemRepository.save(Item.builder()
                    .withItemType(ItemType.BEER)
                    .withName("Beer")
                    .withUnitPrice(0.50)
                    .withQuantity(1)
                    .withDetails("Price per unit")
                    .build());
        };
    }
}
