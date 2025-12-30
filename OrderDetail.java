package com.example.demo1;

public class OrderDetail {
    private int orderDetailId;
    private int orderId;
    private int dishId;
    private int quantity;
    private double unitPrice;
    private double amount;

    public OrderDetail() {
    }

    public OrderDetail(int orderDetailId, int orderId, int dishId, int quantity, double unitPrice, double amount) {
        this.orderDetailId = orderDetailId;
        this.orderId = orderId;
        this.dishId = dishId;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.amount = amount;
    }

    public int getOrderDetailId() {
        return orderDetailId;
    }

    public void setOrderDetailId(int orderDetailId) {
        this.orderDetailId = orderDetailId;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getDishId() {
        return dishId;
    }

    public void setDishId(int dishId) {
        this.dishId = dishId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
