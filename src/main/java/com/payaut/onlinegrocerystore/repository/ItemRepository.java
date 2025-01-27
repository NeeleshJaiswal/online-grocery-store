package com.payaut.onlinegrocerystore.repository;

import com.payaut.onlinegrocerystore.entity.Item;
import com.payaut.onlinegrocerystore.entity.ItemType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
    Optional<Item> findByItemType(ItemType itemType);
}
