package com.example.demo1.servlet;

import com.example.demo1.Dish;
import com.example.demo1.Order;
import com.example.demo1.OrderDetail;
import com.example.demo1.dao.DishDAO;
import com.example.demo1.dao.OrderDAO;
import com.example.demo1.dao.OrderDetailDAO;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet("/order-detail")
public class OrderDetailServlet extends HttpServlet {
    private OrderDAO orderDAO = new OrderDAO();
    private OrderDetailDAO orderDetailDAO = new OrderDetailDAO();
    private DishDAO dishDAO = new DishDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Get orderId parameter
        String orderIdParam = request.getParameter("orderId");
        
        if (orderIdParam == null || orderIdParam.trim().isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Order ID is required");
            return;
        }
        
        int orderId;
        try {
            orderId = Integer.parseInt(orderIdParam);
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid Order ID");
            return;
        }
        
        // Get order
        Order order = orderDAO.getOrderById(orderId);
        
        if (order == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Order not found");
            return;
        }
        
        // Get ALL order details for this order
        // Always show all dishes in the order, regardless of where the request comes from
        List<OrderDetail> orderDetails = orderDetailDAO.getDetailsByOrderId(orderId);
        
        // Calculate total amount from order details
        double totalAmount = orderDetails.stream()
                .mapToDouble(OrderDetail::getAmount)
                .sum();
        
        // Set calculated total amount to order object
        order.setTotalAmount(totalAmount);
        
        // Enrich with dish names
        List<OrderDetailWithDish> enrichedDetails = orderDetails.stream()
                .map(detail -> {
                    Dish dish = dishDAO.getDishById(detail.getDishId());
                    String dishName = (dish != null) ? dish.getDishName() : "Unknown Dish";
                    return new OrderDetailWithDish(detail, dishName);
                })
                .collect(Collectors.toList());
        
        // Set attributes
        request.setAttribute("order", order);
        request.setAttribute("orderDetails", enrichedDetails);
        
        // Forward to JSP
        request.getRequestDispatcher("/WEB-INF/views/order-detail.jsp").forward(request, response);
    }
    
    // Helper class to combine OrderDetail with Dish name
    public static class OrderDetailWithDish {
        private OrderDetail orderDetail;
        private String dishName;
        
        public OrderDetailWithDish(OrderDetail orderDetail, String dishName) {
            this.orderDetail = orderDetail;
            this.dishName = dishName;
        }
        
        public OrderDetail getOrderDetail() {
            return orderDetail;
        }
        
        public String getDishName() {
            return dishName;
        }
    }
}

