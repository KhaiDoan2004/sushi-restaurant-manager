package com.example.demo1.servlet;

import com.example.demo1.Dish;
import com.example.demo1.dao.DishDAO;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "DishServlet", value = "/find-dishes")
public class DishServlet extends HttpServlet {
    private DishDAO dishDAO;

    @Override
    public void init() throws ServletException {
        dishDAO = new DishDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Get search keyword from request parameter
        String keyword = request.getParameter("q");
        
        List<Dish> dishes;
        if (keyword == null || keyword.trim().isEmpty()) {
            // Get all dishes if no search keyword
            dishes = dishDAO.getAllDishes();
        } else {
            // Search by name
            dishes = dishDAO.searchByName(keyword);
        }

        // Set dishes as request attribute
        request.setAttribute("dishes", dishes);
        request.setAttribute("searchKeyword", keyword != null ? keyword : "");

        // Forward to findDish.jsp
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/find-dish.jsp");
        dispatcher.forward(request, response);
    }
}

