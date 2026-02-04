package com.internal.enumation;

public enum TransactionType {
    STOCK_IN,           // Adding stock (purchase, return from customer)
    STOCK_OUT,          // Removing stock (sale, damaged, expired)
    ADJUSTMENT,         // Manual stock adjustment (inventory count correction)
    TRANSFER,           // Transfer between locations
    RETURN_TO_SUPPLIER, // Returning defective items to supplier
    PRODUCTION,         // Used in production/manufacturing
    SAMPLE,             // Given as sample
    DAMAGED,            // Marked as damaged/destroyed
    EXPIRED             // Marked as expired
}