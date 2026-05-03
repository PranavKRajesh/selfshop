package com.billing.billingsystem;

public class CartItem {
    private String productName;
    private double price;
    private int qty;

    public CartItem(String productName, double price, int qty) {
        this.productName = productName;
        this.price = price;
        this.qty = qty;
    }

    public String getProductName() { return productName; }
    public double getPrice() { return price; }
    public int getQty() { return qty; }
    public void setQty(int qty) { this.qty = qty; }
    public double getTotal() { return price * qty; }

    public String getFormattedPrice() { return String.format("%.2f", price); }
    public String getFormattedTotal() { return String.format("%.2f", getTotal()); }
}