package com.example.demo1.servlet;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(name = "LoginServlet", value = "/login")
public class LoginServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Forward to login page in root of webapp
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/login.jsp");
        dispatcher.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String email = request.getParameter("email");
        // String password = request.getParameter("password"); // Not used in current logic

        // default role
        String role = "customer";

        // crude logic — replace with DB later
        if (email != null && (
                email.toLowerCase().contains("manager") ||
                        email.equalsIgnoreCase("admin@masuri.com") ||
                        email.endsWith("@manager.com")
        )) {
            role = "manager";
        }

        // Save session
        HttpSession session = request.getSession(true);
        session.setAttribute("email", email);
        session.setAttribute("role", role);

        // Redirect to homepage (same for both roles)
        response.sendRedirect(request.getContextPath() + "/index.jsp");
    }
}

