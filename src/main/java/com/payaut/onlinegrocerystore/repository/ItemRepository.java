package com.payaut.onlinegrocerystore.repository;

import com.payaut.onlinegrocerystore.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
}
