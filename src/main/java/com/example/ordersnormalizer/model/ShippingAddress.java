package com.example.ordersnormalizer.model;

import jakarta.validation.constraints.NotBlank;

public class ShippingAddress {
    @NotBlank private String line1;
    @NotBlank private String city;
    @NotBlank private String postalCode;
    @NotBlank private String country;

    // getters & setters
    public String getLine1() { return line1; }
    public void setLine1(String line1) { this.line1 = line1; }
    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }
    public String getPostalCode() { return postalCode; }
    public void setPostalCode(String postalCode) { this.postalCode = postalCode; }
    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }
}
