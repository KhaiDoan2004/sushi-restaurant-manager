package com.example.demo1.dao;

import com.example.demo1.Dish;
import com.example.demo1.utils.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO (Data Access Object) cho việc truy cập dữ liệu món ăn (Dish)
 * Cung cấp các phương thức để thao tác với bảng tbl_dish trong database
 */
public class DishDAO {

    /**
     * Lấy tất cả các món ăn từ database
     * @return Danh sách tất cả các món ăn
     */
    public List<Dish> getAllDishes() {
        List<Dish> dishes = new ArrayList<>();

        String sql = "SELECT dish_id, dish_name, price, status, description FROM tbl_dish";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            System.out.println("DishDAO: Successfully connected to database");
            System.out.println("DishDAO: Executing query: " + sql);

            // Duyệt qua kết quả và tạo đối tượng Dish cho mỗi bản ghi
            while (rs.next()) {
                Dish d = new Dish(
                        rs.getInt("dish_id"),
                        rs.getString("dish_name"),
                        rs.getDouble("price"),
                        rs.getString("status"),
                        rs.getString("description")
                );
                dishes.add(d);
            }

            System.out.println("DishDAO: Found " + dishes.size() + " dishes");

        } catch (Exception e) {
            System.err.println("DishDAO ERROR in getAllDishes(): " + e.getMessage());
            e.printStackTrace();
        }

        return dishes;
    }

    /**
     * Lấy thông tin món ăn theo ID
     * @param id ID của món ăn cần tìm
     * @return Đối tượng Dish nếu tìm thấy, null nếu không tìm thấy
     */
    public Dish getDishById(int id) {
        String sql = "SELECT * FROM tbl_dish WHERE dish_id = ?";
        Dish d = null;

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            // Nếu tìm thấy bản ghi, tạo đối tượng Dish
            if (rs.next()) {
                d = new Dish(
                        rs.getInt("dish_id"),
                        rs.getString("dish_name"),
                        rs.getDouble("price"),
                        rs.getString("status"),
                        rs.getString("description")
                );
            }
        } catch (Exception e) {
            System.err.println("DishDAO ERROR in getDishById(" + id + "): " + e.getMessage());
            e.printStackTrace();
        }

        return d;
    }

    /**
     * Tìm kiếm món ăn theo tên (tìm kiếm không phân biệt hoa thường, tìm kiếm một phần)
     * @param keyword Từ khóa tìm kiếm
     * @return Danh sách các món ăn có tên chứa từ khóa
     */
    public List<Dish> searchByName(String keyword) {
        List<Dish> dishes = new ArrayList<>();

        String sql = "SELECT dish_id, dish_name, price, status, description FROM tbl_dish WHERE dish_name LIKE ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            // Sử dụng LIKE với % để tìm kiếm một phần
            ps.setString(1, "%" + keyword + "%");
            ResultSet rs = ps.executeQuery();

            // Duyệt qua kết quả và thêm vào danh sách
            while (rs.next()) {
                Dish d = new Dish(
                        rs.getInt("dish_id"),
                        rs.getString("dish_name"),
                        rs.getDouble("price"),
                        rs.getString("status"),
                        rs.getString("description")
                );
                dishes.add(d);
            }

        } catch (Exception e) {
            System.err.println("DishDAO ERROR in searchByName(" + keyword + "): " + e.getMessage());
            e.printStackTrace();
        }

        return dishes;
    }
}
