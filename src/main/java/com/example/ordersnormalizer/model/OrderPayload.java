package com.example.ordersnormalizer.model;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.util.List;

public class OrderPayload {
    @NotBlank private String orderId;
    @NotBlank private String customerId;
    @Pattern(regexp="^[A-Z]{3}$") private String currency;
    @DecimalMin("0.0") private BigDecimal amount;
    @NotEmpty private List<Item> items;
    @NotBlank private String paymentMethod;
    @Pattern(regexp="^[A-Z]{2}$") private String country;
    @NotBlank private String createdAt;
    @NotNull private ShippingAddress shippingAddress;

    // getters & setters
    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }
    public String getCustomerId() { return customerId; }
    public void setCustomerId(String customerId) { this.customerId = customerId; }
    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public List<Item> getItems() { return items; }
    public void setItems(List<Item> items) { this.items = items; }
    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }
    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
    public ShippingAddress getShippingAddress() { return shippingAddress; }
    public void setShippingAddress(ShippingAddress shippingAddress) { this.shippingAddress = shippingAddress; }
}
