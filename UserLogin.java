package com.login;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletContext;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import javax.servlet.http.HttpSession;
import java.sql.*;

@WebServlet("/UserLogin")
public class UserLogin extends HttpServlet {

    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        res.setContentType("text/html");
        PrintWriter out = res.getWriter();

        // Get user input from login form
        String UserName = req.getParameter("username");
        String Password = req.getParameter("password");

        // Set session attributes
        HttpSession session = req.getSession();
        session.setAttribute("username", UserName);
        session.setAttribute("password", Password);

        // Retrieve database connection parameters
        ServletContext context = getServletContext();
        String driver = context.getInitParameter("jdbc.driver");
        String url = context.getInitParameter("jdbc.url");
        String dbUsername = context.getInitParameter("jdbc.username");
        String dbPassword = context.getInitParameter("jdbc.password");

        Connection conn = null;
        try {
            // Load the JDBC driver
            Class.forName(driver);

            // Establish database connection
            conn = DriverManager.getConnection(url, dbUsername, dbPassword);

            // Prepare SQL query to validate user
            String sql = "SELECT * FROM userlogin WHERE name = ? AND password = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, UserName);
            pstmt.setString(2, Password);

            ResultSet rs = pstmt.executeQuery();

            // Check if a user with the given credentials exists
            if (rs.next()) {
                // Valid user, redirect to profile page
                req.getRequestDispatcher("profile.html").include(req, res);
            } else {
                // Invalid user, show login page again with error message
                out.println("<h3 style='color:red;'>Invalid Username or Password!</h3>");
                req.getRequestDispatcher("userlogin.html").include(req, res);
            }
        } catch (Exception e) {
            e.printStackTrace();
            out.println("<h3 style='color:red;'>Error occurred while logging in. Please try again later.</h3>");
            req.getRequestDispatcher("userlogin.html").include(req, res);
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
