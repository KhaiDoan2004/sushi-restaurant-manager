package com.example.demo1.dto;

import java.time.LocalDate;

/**
 * DTO (Data Transfer Object) cho thông tin đơn hàng của một món ăn
 * Chứa thông tin về số lượng và doanh thu của món ăn trong một đơn hàng cụ thể
 */
public class DishOrderDTO {
    private int orderId;             // ID của đơn hàng
    private LocalDate orderDate;     // Ngày đặt hàng
    private int quantity;            // Số lượng món ăn trong đơn hàng
    private double revenue;          // Doanh thu từ món ăn trong đơn hàng

    public DishOrderDTO() {
    }

    public DishOrderDTO(int orderId, LocalDate orderDate, int quantity, double revenue) {
        this.orderId = orderId;
        this.orderDate = orderDate;
        this.quantity = quantity;
        this.revenue = revenue;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public LocalDate getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDate orderDate) {
        this.orderDate = orderDate;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getRevenue() {
        return revenue;
    }

    public void setRevenue(double revenue) {
        this.revenue = revenue;
    }
}

