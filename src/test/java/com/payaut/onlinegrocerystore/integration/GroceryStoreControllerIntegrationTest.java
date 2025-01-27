package com.payaut.onlinegrocerystore.integration;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.payaut.onlinegrocerystore.dto.ApiError;
import com.payaut.onlinegrocerystore.dto.ItemDTO;
import com.payaut.onlinegrocerystore.entity.ItemType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GroceryStoreControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private ObjectMapper objectMapper; // For JSON serialization/deserialization

    private final RestTemplate restTemplate = new RestTemplate();

    private String getBaseUrl() {
        return "http://localhost:" + port + "/api/v1/online/grocery";
    }

    @Test
    void testGetDiscountRules() {
        String url = getBaseUrl() + "/discounts";
        ResponseEntity<String[]> response = restTemplate.getForEntity(url, String[].class);

        Assertions.assertEquals(200, response.getStatusCodeValue());
        Assertions.assertTrue(response.getBody().length > 0);
        Assertions.assertTrue(response.getBody()[0].contains("BREAD"));
    }

    @Test
    void testGetPrices() {
        String url = getBaseUrl() + "/prices";
        ResponseEntity<ItemDTO[]> response = restTemplate.getForEntity(url, ItemDTO[].class);

        Assertions.assertEquals(200, response.getStatusCodeValue());
        Assertions.assertTrue(response.getBody().length > 0);
        Assertions.assertTrue(response.getBody()[0].getName().equalsIgnoreCase("Bread"));
    }

    @Test
    void testCalculateOrder() throws Exception {
        String url = getBaseUrl() + "/orders";

        // Build request JSON
        List<ItemDTO> order = List.of(
                ItemDTO.builder()
                        .withItemType(ItemType.BREAD)
                        .withName("Bread")
                        .withUnitPrice(1.00)
                        .withQuantity(3)
                        .withDetails("3 days old")
                        .build(),
                ItemDTO.builder()
                        .withItemType(ItemType.VEGETABLE)
                        .withName("Vegetables")
                        .withUnitPrice(0.01)
                        .withQuantity(200)
                        .withDetails("")
                        .build(),
                ItemDTO.builder()
                        .withItemType(ItemType.BEER)
                        .withName("Beer")
                        .withUnitPrice(0.50)
                        .withQuantity(6)
                        .withDetails("Dutch Beer")
                        .build()
        );

        // Convert to JSON
        String jsonOrder = objectMapper.writeValueAsString(order);

        // Send POST request
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        HttpEntity<String> request = new HttpEntity<>(jsonOrder, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);

        // Validate response
        Assertions.assertEquals(200, response.getStatusCodeValue());
        Assertions.assertTrue(response.getBody().contains("\"total\":5.86"));
        Assertions.assertTrue(response.getBody().contains("3 x Bread (3 days old): €2.00"));
        Assertions.assertTrue(response.getBody().contains("200g Vegetables: €1.86"));
        Assertions.assertTrue(response.getBody().contains("6 x Beer (Dutch Beer): €2.00"));
    }


    @Test
    void testBreadOlderThan6Days() throws Exception {
        String url = getBaseUrl() + "/orders";

        List<ItemDTO> order = List.of(
                ItemDTO.builder()
                        .withItemType(ItemType.BREAD)
                        .withName("Bread")
                        .withUnitPrice(1.00)
                        .withQuantity(2)
                        .withDetails("older than 6 days")
                        .build()
        );

        String jsonOrder = objectMapper.writeValueAsString(order);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        HttpEntity<String> request = new HttpEntity<>(jsonOrder, headers);

        HttpClientErrorException exception = Assertions.assertThrows(
                HttpClientErrorException.class,
                () -> restTemplate.postForEntity(url, request, String.class)
        );

        Assertions.assertEquals(400, exception.getRawStatusCode());

        // Deserialize the ApiError response
        ApiError apiError = objectMapper.readValue(exception.getResponseBodyAsString(), ApiError.class);
        Assertions.assertEquals(400, apiError.getStatus());
        Assertions.assertEquals("Bad Request", apiError.getError());
        Assertions.assertTrue(apiError.getMessage().contains("Bread older than 6 days cannot be added"));
    }


//    // Negative Case 2: Invalid Item Type
//    @Test
//    void testInvalidItemType() throws Exception {
//        String url = getBaseUrl() + "/orders";
//
//        String invalidOrderJson = """
//        [
//            {
//                "itemType": "INVALID_ITEM",
//                "name": "Invalid",
//                "unitPrice": 1.00,
//                "quantity": 1,
//                "details": ""
//            }
//        ]
//        """;
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Content-Type", "application/json");
//        HttpEntity<String> request = new HttpEntity<>(invalidOrderJson, headers);
//
//        HttpClientErrorException exception = Assertions.assertThrows(
//                HttpClientErrorException.class,
//                () -> restTemplate.postForEntity(url, request, String.class)
//        );
//
//        Assertions.assertEquals(400, exception.getRawStatusCode());
//
//        ApiError apiError = objectMapper.readValue(exception.getResponseBodyAsString(), ApiError.class);
//        Assertions.assertEquals(400, apiError.getStatus());
//        Assertions.assertEquals("Bad Request", apiError.getError());
//        Assertions.assertTrue(apiError.getMessage().contains("Unsupported item type"));
//    }
//
//    // Negative Case 3: Empty Order
//    @Test
//    void testEmptyOrder() throws Exception {
//        String url = getBaseUrl() + "/orders";
//
//        String emptyOrderJson = "[]";
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Content-Type", "application/json");
//        HttpEntity<String> request = new HttpEntity<>(emptyOrderJson, headers);
//
//        ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
//
//        Assertions.assertEquals(200, response.getStatusCodeValue());
//        Assertions.assertTrue(response.getBody().contains("\"total\":0.00"));
//        Assertions.assertTrue(response.getBody().contains("\"breakdown\":[]"));
//    }

}