package com.example.demo1.dao;

import com.example.demo1.OrderDetail;
import com.example.demo1.utils.DBUtil;

import java.sql.*;
import java.util.*;

/**
 * DAO (Data Access Object) cho việc truy cập dữ liệu chi tiết đơn hàng (OrderDetail)
 * Cung cấp các phương thức để thao tác với bảng tbl_order_detail trong database
 */
public class OrderDetailDAO {

    /**
     * Lấy tất cả chi tiết đơn hàng theo ID đơn hàng
     * @param orderId ID của đơn hàng
     * @return Danh sách các chi tiết đơn hàng của đơn hàng đó
     */
    public List<OrderDetail> getDetailsByOrderId(int orderId) {
        List<OrderDetail> list = new ArrayList<>();

        String sql = """
            SELECT * FROM tbl_order_detail 
            WHERE order_id = ?
        """;

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, orderId);
            ResultSet rs = ps.executeQuery();

            // Duyệt qua kết quả và tạo đối tượng OrderDetail cho mỗi bản ghi
            while (rs.next()) {
                list.add(new OrderDetail(
                        rs.getInt("order_detail_id"),
                        rs.getInt("order_id"),
                        rs.getInt("dish_id"),
                        rs.getInt("quantity"),
                        rs.getDouble("unit_price"),
                        rs.getDouble("amount")
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    /**
     * Tính tổng doanh thu của một món ăn
     * @param dishId ID của món ăn
     * @return Tổng doanh thu (tổng amount) của món ăn đó
     */
    public double getRevenueByDishId(int dishId) {
        String sql = "SELECT SUM(amount) as qty FROM tbl_order_detail WHERE dish_id = ?";
        double rev = 0;

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, dishId);
            ResultSet rs = ps.executeQuery();

            // Lấy kết quả tổng doanh thu từ câu truy vấn SUM
            if (rs.next()) rev = rs.getDouble(1);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return rev;
    }

    /**
     * Lấy tất cả chi tiết đơn hàng của một món ăn
     * @param dishId ID của món ăn
     * @return Danh sách các chi tiết đơn hàng chứa món ăn đó
     */
    public List<OrderDetail> getOrderDetailsByDishId(int dishId) {
        List<OrderDetail> list = new ArrayList<>();

        String sql = "SELECT * FROM tbl_order_detail WHERE dish_id = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, dishId);
            ResultSet rs = ps.executeQuery();

            // Duyệt qua kết quả và tạo đối tượng OrderDetail cho mỗi bản ghi
            while (rs.next()) {
                list.add(new OrderDetail(
                        rs.getInt("order_detail_id"),
                        rs.getInt("order_id"),
                        rs.getInt("dish_id"),
                        rs.getInt("quantity"),
                        rs.getDouble("unit_price"),
                        rs.getDouble("amount")
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    /**
     * Lấy tất cả chi tiết đơn hàng từ database
     * @return Danh sách tất cả các chi tiết đơn hàng
     */
    public List<OrderDetail> getAllDetails() {
        List<OrderDetail> list = new ArrayList<>();

        String sql = "SELECT * FROM tbl_order_detail";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            // Duyệt qua kết quả và tạo đối tượng OrderDetail cho mỗi bản ghi
            while (rs.next()) {
                list.add(new OrderDetail(
                        rs.getInt("order_detail_id"),
                        rs.getInt("order_id"),
                        rs.getInt("dish_id"),
                        rs.getInt("quantity"),
                        rs.getDouble("unit_price"),
                        rs.getDouble("amount")
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
}
