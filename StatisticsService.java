package com.example.demo1.service;

import com.example.demo1.Dish;
import com.example.demo1.Order;
import com.example.demo1.OrderDetail;
import com.example.demo1.dao.DishDAO;
import com.example.demo1.dao.OrderDAO;
import com.example.demo1.dao.OrderDetailDAO;
import com.example.demo1.dto.DishRevenueDTO;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class StatisticsService {
    private DishDAO dishDAO = new DishDAO();
    private OrderDAO orderDAO = new OrderDAO();
    private OrderDetailDAO orderDetailDAO = new OrderDetailDAO();

    public List<DishRevenueDTO> getRevenueByDish(LocalDate start, LocalDate end) {
        // 1. Lấy tất cả các đơn hàng trong khoảng thời gian từ start đến end
        List<Order> ordersInRange = orderDAO.getOrdersBetween(start, end);

        // 2. Thu thập tất cả các orderId từ những đơn hàng đó
        Set<Integer> orderIds = ordersInRange.stream()
                .map(Order::getOrderId)
                .collect(Collectors.toSet());

        // 3. Từ OrderDetailDAO, lấy các chi tiết đơn hàng khớp với các ID này
        List<OrderDetail> relevantDetails = orderDetailDAO.getAllDetails().stream()
                .filter(detail -> orderIds.contains(detail.getOrderId()))
                .collect(Collectors.toList());

        // 4. Nhóm theo dishId
        Map<Integer, List<OrderDetail>> detailsByDishId = relevantDetails.stream()
                .collect(Collectors.groupingBy(OrderDetail::getDishId));

        // 5. Tính tổng totalQuantity và totalRevenue, sau đó kết hợp với tên món ăn
        List<DishRevenueDTO> result = detailsByDishId.entrySet().stream()
                .map(entry -> {
                    int dishId = entry.getKey();
                    List<OrderDetail> details = entry.getValue();

                    // Tính tổng số lượng
                    int totalQuantity = details.stream()
                            .mapToInt(OrderDetail::getQuantity)
                            .sum();

                    // Tính tổng số tiền (doanh thu)
                    double totalRevenue = details.stream()
                            .mapToDouble(OrderDetail::getAmount)
                            .sum();

                    // 6. Kết hợp với tên món ăn thông qua DishDAO
                    Dish dish = dishDAO.getDishById(dishId);
                    String dishName = (dish != null) ? dish.getDishName() : "Unknown Dish";

                    // 7. Ánh xạ sang DishRevenueDTO
                    return new DishRevenueDTO(dishId, dishName, totalQuantity, totalRevenue);
                })
                // 8. Sắp xếp theo totalRevenue giảm dần
                .sorted(Comparator.comparing(DishRevenueDTO::getTotalRevenue).reversed())
                // 9. Trả về danh sách
                .collect(Collectors.toList());

        return result;
    }
}

