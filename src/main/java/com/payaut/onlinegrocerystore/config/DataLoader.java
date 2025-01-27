package com.payaut.onlinegrocerystore.config;

import com.payaut.onlinegrocerystore.entity.DiscountRule;
import com.payaut.onlinegrocerystore.entity.Item;
import com.payaut.onlinegrocerystore.repository.DiscountRuleRepository;
import com.payaut.onlinegrocerystore.repository.ItemRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataLoader {

    @Bean
    CommandLineRunner loadData(ItemRepository itemRepository, DiscountRuleRepository discountRuleRepository) {
        return args -> {
            itemRepository.save(Item.builder().withName("Bread").withPrice(1.00).build());
            itemRepository.save(Item.builder().withName("Vegetables").withPrice(1.00).build());
            itemRepository.save(Item.builder().withName("Beer").withPrice(1.00).build());

            discountRuleRepository.save(DiscountRule.builder().withItemType("Bread").withDescription("Buy 1 Take 2 if 3 days old.").build());
            discountRuleRepository.save(DiscountRule.builder().withItemType("Bread").withDescription("Buy 1 Take 3 if 6 days old.").build());
            discountRuleRepository.save(DiscountRule.builder().withItemType("Vegetables").withDescription("5% discount for 0-100g.").build());
            discountRuleRepository.save(DiscountRule.builder().withItemType("Vegetables").withDescription("7% discount for 100-500g.").build());
            discountRuleRepository.save(DiscountRule.builder().withItemType("Vegetables").withDescription("10% discount for 500g+").build());
            discountRuleRepository.save(DiscountRule.builder().withItemType("Beer").withDescription("€3 discount for Belgian pack of 6.").build());
            discountRuleRepository.save(DiscountRule.builder().withItemType("Beer").withDescription("€2 discount for Dutch pack of 6.").build());
            discountRuleRepository.save(DiscountRule.builder().withItemType("Beer").withDescription("€4 discount for German pack of 6.").build());
        };
    }
}
