package com.payaut.onlinegrocerystore.service;

import com.payaut.onlinegrocerystore.dto.ItemDTO;
import com.payaut.onlinegrocerystore.dto.ReceiptDTO;
import com.payaut.onlinegrocerystore.entity.Item;
import com.payaut.onlinegrocerystore.entity.ItemType;
import com.payaut.onlinegrocerystore.repository.ItemRepository;
import com.payaut.onlinegrocerystore.service.impl.GroceryStoreServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;

class GroceryStoreServiceImplTest {

    private GroceryStoreServiceImpl groceryService;
    private ItemRepository itemRepository;

    @BeforeEach
    void setUp() {
        // Mock the repository
        itemRepository = Mockito.mock(ItemRepository.class);

        // Mock the database responses
        when(itemRepository.findByItemType(ItemType.BREAD))
                .thenReturn(Optional.of(Item.builder()
                        .withItemType(ItemType.BREAD)
                        .withName("Bread")
                        .withUnitPrice(1.00)
                        .build()));

        when(itemRepository.findByItemType(ItemType.VEGETABLE))
                .thenReturn(Optional.of(Item.builder()
                        .withItemType(ItemType.VEGETABLE)
                        .withName("Vegetables")
                        .withUnitPrice(0.01)
                        .build()));

        when(itemRepository.findByItemType(ItemType.BEER))
                .thenReturn(Optional.of(Item.builder()
                        .withItemType(ItemType.BEER)
                        .withName("Beer")
                        .withUnitPrice(0.50)
                        .build()));

        // Initialize the service with the mocked repository
        groceryService = new GroceryStoreServiceImpl(itemRepository);
    }

    @Test
    void testBread_3DaysOld() {
        // 3 x Bread (3 days old)
        // "Buy 1 Take 2": If user buys 3 loaves, they pay for 2
        ItemDTO breadDto = ItemDTO.builder()
                .withItemType(ItemType.BREAD)
                .withName("Bread")
                .withUnitPrice(1.00)
                .withQuantity(3)
                .withDetails("3 days old")
                .build();

        ReceiptDTO receipt = groceryService.calculateOrder(List.of(breadDto));
        Assertions.assertEquals(2.00, receipt.getTotal(), 0.001);
        Assertions.assertEquals(1, receipt.getBreakdown().size());
        Assertions.assertTrue(receipt.getBreakdown().get(0).contains("€2.00"));
    }

    @Test
    void testVegetables_200g() {
        // 200g Vegetables, unitPrice=0.01 => base cost = 2.00
        // 200g is ≤500 => 7% discount => 2.00 * (1 - 0.07) = 1.86
        ItemDTO vegDto = ItemDTO.builder()
                .withItemType(ItemType.VEGETABLE)
                .withName("Vegetables")
                .withUnitPrice(0.01)
                .withQuantity(200)
                .withDetails("")
                .build();

        ReceiptDTO receipt = groceryService.calculateOrder(List.of(vegDto));
        Assertions.assertEquals(1.86, receipt.getTotal(), 0.001);
        Assertions.assertTrue(receipt.getBreakdown().get(0).contains("€1.86"));
    }

    @Test
    void testBeer_6Dutch() {
        // 6 x Dutch Beer => fixed cost = €2.00
        ItemDTO beerDto = ItemDTO.builder()
                .withItemType(ItemType.BEER)
                .withName("Beer")
                .withUnitPrice(0.50)
                .withQuantity(6)
                .withDetails("Dutch Beer")
                .build();

        ReceiptDTO receipt = groceryService.calculateOrder(List.of(beerDto));
        Assertions.assertEquals(2.00, receipt.getTotal(), 0.001);
        Assertions.assertTrue(receipt.getBreakdown().get(0).contains("€2.00"));
    }

    @Test
    void testMixedOrder() {
        // Combine multiple items to ensure the total sums up correctly
        ItemDTO breadDto = ItemDTO.builder()
                .withItemType(ItemType.BREAD)
                .withName("Bread")
                .withUnitPrice(1.00)
                .withQuantity(3)
                .withDetails("3 days old")
                .build();

        ItemDTO vegDto = ItemDTO.builder()
                .withItemType(ItemType.VEGETABLE)
                .withName("Vegetables")
                .withUnitPrice(0.01)
                .withQuantity(200)
                .withDetails("")
                .build();

        ItemDTO beerDto = ItemDTO.builder()
                .withItemType(ItemType.BEER)
                .withName("Beer")
                .withUnitPrice(0.50)
                .withQuantity(6)
                .withDetails("Dutch Beer")
                .build();

        // Calculate combined order
        ReceiptDTO receipt = groceryService.calculateOrder(List.of(breadDto, vegDto, beerDto));

        // Expected total: 2.00 (bread) + 1.86 (veg) + 2.00 (beer) = 5.86
        Assertions.assertEquals(5.86, receipt.getTotal(), 0.001);
        Assertions.assertEquals(3, receipt.getBreakdown().size());

        // Check breakdown lines contain correct amounts
        Assertions.assertTrue(
            receipt.getBreakdown().stream().anyMatch(line -> line.contains("€2.00") && line.contains("Bread")));
        Assertions.assertTrue(
            receipt.getBreakdown().stream().anyMatch(line -> line.contains("€1.86") && line.contains("Vegetables")));
        Assertions.assertTrue(
            receipt.getBreakdown().stream().anyMatch(line -> line.contains("€2.00") && line.contains("Beer")));
    }

    @Test
    void testBread_OlderThan6Days() {
        // Expect an exception if bread is older than 6 days
        ItemDTO oldBread = ItemDTO.builder()
                .withItemType(ItemType.BREAD)
                .withName("Bread")
                .withUnitPrice(1.00)
                .withQuantity(2)
                .withDetails("7 days old")
                .build();

        // Make sure an exception is thrown
        Assertions.assertThrows(IllegalArgumentException.class,
            () -> groceryService.calculateOrder(List.of(oldBread)));
    }
}