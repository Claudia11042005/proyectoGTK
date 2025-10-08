package com.example.ordersnormalizer.model;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public class Item {
    @NotBlank private String sku;
    @Min(1) private int qty;
    @DecimalMin("0.0") private BigDecimal price;
    private String category;

    // getters & setters
    public String getSku() { return sku; }
    public void setSku(String sku) { this.sku = sku; }
    public int getQty() { return qty; }
    public void setQty(int qty) { this.qty = qty; }
    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
}
