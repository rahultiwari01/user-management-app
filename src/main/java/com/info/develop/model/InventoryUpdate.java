package com.info.develop.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InventoryUpdate {
    private String productId;
    private int quantityChange; // e.g., -1 for a sale, +10 for a restock
}