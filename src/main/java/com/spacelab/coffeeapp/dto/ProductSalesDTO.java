package com.spacelab.coffeeapp.dto;

public class ProductSalesDTO {
    private String productName;
    private int month;
    private int purchaseCount;
    public ProductSalesDTO(String productName, int month, int purchaseCount) {
        this.productName = productName;
        this.month = month;
        this.purchaseCount = purchaseCount;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getPurchaseCount() {
        return purchaseCount;
    }

    public void setPurchaseCount(int purchaseCount) {
        this.purchaseCount = purchaseCount;
    }
}