package com.example.demo1.dao;

import com.example.demo1.Order;
import com.example.demo1.utils.DBUtil;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO (Data Access Object) cho việc truy cập dữ liệu đơn hàng (Order)
 * Cung cấp các phương thức để thao tác với bảng tbl_order trong database
 */
public class OrderDAO {

    /**
     * Lấy tất cả các đơn hàng đã thanh toán từ database
     * @return Danh sách tất cả các đơn hàng có trạng thái PAID
     */
    public List<Order> getAllOrders() {
        List<Order> list = new ArrayList<>();

        // Chỉ lấy các đơn hàng đã thanh toán (PAID), loại trừ PENDING và CANCELLED
        String sql = "SELECT order_id, customer_id, staff_id, table_id, order_date, status FROM tbl_order WHERE status = 'PAID'";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            // Duyệt qua kết quả và tạo đối tượng Order cho mỗi bản ghi
            while (rs.next()) {
                Order order = new Order();
                order.setOrderId(rs.getInt("order_id"));
                order.setCustomerId(rs.getInt("customer_id"));
                order.setStaffId(rs.getInt("staff_id"));
                order.setTableId(rs.getInt("table_id"));
                order.setOrderDate(rs.getDate("order_date").toLocalDate());
                order.setStatus(rs.getString("status"));
                order.setTotalAmount(0.0); // Sẽ được tính từ chi tiết đơn hàng
                order.setNote(null);
                list.add(order);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    /**
     * Lấy các đơn hàng trong khoảng thời gian chỉ định
     * @param start Ngày bắt đầu
     * @param end Ngày kết thúc
     * @return Danh sách các đơn hàng đã thanh toán trong khoảng thời gian từ start đến end
     */
    public List<Order> getOrdersBetween(LocalDate start, LocalDate end) {
        List<Order> list = new ArrayList<>();

        // Chỉ lấy các đơn hàng đã thanh toán (PAID), loại trừ PENDING và CANCELLED
        String sql = "SELECT order_id, customer_id, staff_id, table_id, order_date, status FROM tbl_order WHERE order_date BETWEEN ? AND ? AND status = 'PAID'";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            // Thiết lập tham số cho câu truy vấn
            ps.setDate(1, java.sql.Date.valueOf(start));
            ps.setDate(2, java.sql.Date.valueOf(end));

            ResultSet rs = ps.executeQuery();

            // Duyệt qua kết quả và tạo đối tượng Order cho mỗi bản ghi
            while (rs.next()) {
                Order order = new Order();
                order.setOrderId(rs.getInt("order_id"));
                order.setCustomerId(rs.getInt("customer_id"));
                order.setStaffId(rs.getInt("staff_id"));
                order.setTableId(rs.getInt("table_id"));
                order.setOrderDate(rs.getDate("order_date").toLocalDate());
                order.setStatus(rs.getString("status"));
                order.setTotalAmount(0.0); // Sẽ được tính từ chi tiết đơn hàng
                order.setNote(null);
                list.add(order);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    /**
     * Lấy thông tin đơn hàng theo ID
     * @param orderId ID của đơn hàng cần tìm
     * @return Đối tượng Order nếu tìm thấy, null nếu không tìm thấy
     */
    public Order getOrderById(int orderId) {
        String sql = "SELECT order_id, customer_id, staff_id, table_id, order_date, status FROM tbl_order WHERE order_id = ?";
        Order order = null;

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, orderId);
            ResultSet rs = ps.executeQuery();

            // Nếu tìm thấy bản ghi, tạo đối tượng Order
            if (rs.next()) {
                order = new Order();
                order.setOrderId(rs.getInt("order_id"));
                order.setCustomerId(rs.getInt("customer_id"));
                order.setStaffId(rs.getInt("staff_id"));
                order.setTableId(rs.getInt("table_id"));
                order.setOrderDate(rs.getDate("order_date").toLocalDate());
                order.setStatus(rs.getString("status"));
                order.setTotalAmount(0.0); // Sẽ được tính từ chi tiết đơn hàng
                order.setNote(null);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return order;
    }
}
