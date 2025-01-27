package com.payaut.onlinegrocerystore.repository;

import com.payaut.onlinegrocerystore.entity.DiscountRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DiscountRuleRepository extends JpaRepository<DiscountRule, Long> {
}
