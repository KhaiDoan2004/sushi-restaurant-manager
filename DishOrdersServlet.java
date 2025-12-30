package com.example.demo1.servlet;

import com.example.demo1.Dish;
import com.example.demo1.Order;
import com.example.demo1.OrderDetail;
import com.example.demo1.dao.DishDAO;
import com.example.demo1.dao.OrderDAO;
import com.example.demo1.dao.OrderDetailDAO;
import com.example.demo1.dto.DishOrderDTO;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@WebServlet("/dish-orders")
public class DishOrdersServlet extends HttpServlet {
    private OrderDAO orderDAO = new OrderDAO();
    private OrderDetailDAO orderDetailDAO = new OrderDetailDAO();
    private DishDAO dishDAO = new DishDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Get parameters
        String dishIdParam = request.getParameter("dishId");
        String startDateParam = request.getParameter("startDate");
        String endDateParam = request.getParameter("endDate");
        if (dishIdParam == null || dishIdParam.trim().isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Dish ID is required");
            return;
        }
        
        int dishId;
        try {
            dishId = Integer.parseInt(dishIdParam);
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid Dish ID");
            return;
        }
        
        // Parse dates
        LocalDate start = null;
        LocalDate end = null;
        
        if (startDateParam != null && !startDateParam.trim().isEmpty()) {
            try {
                start = LocalDate.parse(startDateParam);
            } catch (DateTimeParseException e) {
                // Use default if parsing fails
            }
        }
        
        if (endDateParam != null && !endDateParam.trim().isEmpty()) {
            try {
                end = LocalDate.parse(endDateParam);
            } catch (DateTimeParseException e) {
                // Use default if parsing fails
            }
        }
        
        // Get dish name
        Dish dish = dishDAO.getDishById(dishId);
        String dishName = (dish != null) ? dish.getDishName() : "Unknown Dish";
        
        // Get orders in date range
        List<Order> ordersInRange;
        if (start != null && end != null) {
            ordersInRange = orderDAO.getOrdersBetween(start, end);
        } else {
            ordersInRange = orderDAO.getAllOrders();
        }
        
        // Get order details for this dish
        List<OrderDetail> dishOrderDetails = orderDetailDAO.getOrderDetailsByDishId(dishId);
        
        // Filter by date range and group by orderId
        Map<Integer, List<OrderDetail>> detailsByOrderId = dishOrderDetails.stream()
                .filter(detail -> ordersInRange.stream()
                        .anyMatch(order -> order.getOrderId() == detail.getOrderId()))
                .collect(Collectors.groupingBy(OrderDetail::getOrderId));
        
        // Create DTOs
        List<DishOrderDTO> result = detailsByOrderId.entrySet().stream()
                .map(entry -> {
                    int orderId = entry.getKey();
                    List<OrderDetail> details = entry.getValue();
                    
                    // Sum quantity and revenue for this order
                    int totalQuantity = details.stream()
                            .mapToInt(OrderDetail::getQuantity)
                            .sum();
                    
                    double totalRevenue = details.stream()
                            .mapToDouble(OrderDetail::getAmount)
                            .sum();
                    
                    // Get order date
                    Order order = ordersInRange.stream()
                            .filter(o -> o.getOrderId() == orderId)
                            .findFirst()
                            .orElse(null);
                    
                    LocalDate orderDate = (order != null) ? order.getOrderDate() : null;
                    
                    return new DishOrderDTO(orderId, orderDate, totalQuantity, totalRevenue);
                })
                .sorted((a, b) -> {
                    // Sort by order date descending, then by order ID
                    if (a.getOrderDate() != null && b.getOrderDate() != null) {
                        int dateCompare = b.getOrderDate().compareTo(a.getOrderDate());
                        if (dateCompare != 0) return dateCompare;
                    }
                    return Integer.compare(b.getOrderId(), a.getOrderId());
                })
                .collect(Collectors.toList());
        
        // Set attributes
        request.setAttribute("dishId", dishId);
        request.setAttribute("dishName", dishName);
        request.setAttribute("orders", result);
        request.setAttribute("startDate", start);
        request.setAttribute("endDate", end);
        
        // Forward to JSP
        request.getRequestDispatcher("/WEB-INF/views/dish-orders.jsp").forward(request, response);
    }
}

