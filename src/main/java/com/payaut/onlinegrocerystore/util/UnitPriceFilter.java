package com.payaut.onlinegrocerystore.util;

public class UnitPriceFilter {

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Double) {
            return (Double) obj == 0.0; // Hide field if unitPrice is 0.0
        }
        return false;
    }
}