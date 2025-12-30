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

@WebServlet(name = "DishDetailServlet", value = "/dish-detail")
public class DishDetailServlet extends HttpServlet {
    private DishDAO dishDAO;

    @Override
    public void init() throws ServletException {
        dishDAO = new DishDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String idParam = request.getParameter("id");
        
        if (idParam == null || idParam.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/find-dishes");
            return;
        }

        try {
            int dishId = Integer.parseInt(idParam);
            Dish dish = dishDAO.getDishById(dishId);
            
            if (dish == null) {
                response.sendRedirect(request.getContextPath() + "/find-dishes");
                return;
            }

            request.setAttribute("dish", dish);
            RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/dish-detail.jsp");
            dispatcher.forward(request, response);
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/find-dishes");
        }
    }
}

