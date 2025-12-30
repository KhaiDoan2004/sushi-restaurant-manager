package com.example.demo1;

import java.time.LocalDate;

public class Order {
    private int orderId;
    private int customerId;
    private int staffId;
    private int tableId;
    private LocalDate orderDate;
    private String status;        // PAID | PENDING
    private double totalAmount;
    private String note;

    public Order() {}

    public Order(int orderId, int customerId, int staffId, int tableId,
                 LocalDate orderDate, String status, double totalAmount, String note) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.staffId = staffId;
        this.tableId = tableId;
        this.orderDate = orderDate;
        this.status = status;
        this.totalAmount = totalAmount;
        this.note = note;
    }

    // Constructor dùng khi INSERT
    public Order(int customerId, int staffId, int tableId,
                 LocalDate orderDate, String status, double totalAmount, String note) {
        this.customerId = customerId;
        this.staffId = staffId;
        this.tableId = tableId;
        this.orderDate = orderDate;
        this.status = status;
        this.totalAmount = totalAmount;
        this.note = note;
    }

    // GETTER / SETTER
    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public int getStaffId() {
        return staffId;
    }

    public void setStaffId(int staffId) {
        this.staffId = staffId;
    }

    public int getTableId() {
        return tableId;
    }

    public void setTableId(int tableId) {
        this.tableId = tableId;
    }

    public LocalDate getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDate orderDate) {
        this.orderDate = orderDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
