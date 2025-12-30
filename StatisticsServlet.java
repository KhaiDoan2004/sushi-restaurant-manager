package com.example.demo1.servlet;

import com.example.demo1.dto.DishRevenueDTO;
import com.example.demo1.service.StatisticsService;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

@WebServlet("/statistics")
public class StatisticsServlet extends HttpServlet {
    private StatisticsService service;

    @Override
    public void init() throws ServletException {
        super.init();
        service = new StatisticsService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Check if type parameter is present
        String type = request.getParameter("type");
        
        // If type=dish and no dates provided, show the form
        if ("dish".equals(type)) {
            String startDateParam = request.getParameter("startDate");
            String endDateParam = request.getParameter("endDate");
            
            // If dates are not provided, show the form
            if (startDateParam == null || startDateParam.trim().isEmpty() ||
                endDateParam == null || endDateParam.trim().isEmpty()) {
                request.getRequestDispatcher("/WEB-INF/views/dish-statistics-form.jsp").forward(request, response);
                return;
            }
        }
        
        // Otherwise, process the statistics request
        LocalDate start;
        LocalDate end;
        
        // Read optional parameters
        String startDateParam = request.getParameter("startDate");
        String endDateParam = request.getParameter("endDate");
        
        // Parse startDate or use default
        if (startDateParam != null && !startDateParam.trim().isEmpty()) {
            try {
                start = LocalDate.parse(startDateParam);
            } catch (DateTimeParseException e) {
                start = LocalDate.of(2025, 1, 1);
            }
        } else {
            start = LocalDate.of(2025, 1, 1);
        }
        
        // Parse endDate or use default
        if (endDateParam != null && !endDateParam.trim().isEmpty()) {
            try {
                end = LocalDate.parse(endDateParam);
            } catch (DateTimeParseException e) {
                end = LocalDate.of(2025, 12, 31);
            }
        } else {
            end = LocalDate.of(2025, 12, 31);
        }
        
        // Call StatisticsService
        List<DishRevenueDTO> data = service.getRevenueByDish(start, end);
        
        // Store results
        request.setAttribute("results", data);
        request.setAttribute("startDate", start);
        request.setAttribute("endDate", end);
        
        // Forward to JSP
        request.getRequestDispatcher("/WEB-INF/views/dish-revenue-report.jsp").forward(request, response);
    }
}

