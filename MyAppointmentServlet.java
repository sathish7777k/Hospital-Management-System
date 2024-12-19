package com.login;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/MyAppointmentServlet")
public class MyAppointmentServlet extends HttpServlet {

    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        res.setContentType("text/html");
        PrintWriter out = res.getWriter();
        HttpSession session = req.getSession();
        String userName = (String) session.getAttribute("username");

        if (userName == null) {
            res.sendRedirect("userlogin.html"); // Redirect if user is not logged in
            return;
        }

        Connection conn = null;
        try {
            // Get database connection from context
            ServletContext context = getServletContext();
            String driver = context.getInitParameter("jdbc.driver");
            String url = context.getInitParameter("jdbc.url");
            String dbUsername = context.getInitParameter("jdbc.username");
            String dbPassword = context.getInitParameter("jdbc.password");

            Class.forName(driver);
            conn = DriverManager.getConnection(url, dbUsername, dbPassword);

            // Fetch appointments for the logged-in user
            String query = "SELECT doctor_name, specialization, appointment_date, appointment_time FROM appointments WHERE patient_name = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, userName);
            ResultSet rs = pstmt.executeQuery();

            // Generate the HTML response
            out.println("<!DOCTYPE html>");
            out.println("<html lang='en'><head><meta charset='UTF-8'><title>My Appointments</title>");
            out.println("<style>");
            out.println("body {font-family: Arial, sans-serif; background-image: url('img1.jpg'); background-size: cover;}");
            out.println(".container {max-width: 800px; margin: 50px auto; background: rgba(255, 255, 255, 0.9); padding: 20px; border-radius: 10px; box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);}");
            out.println("h1 {text-align: center; color: #333;}");
            out.println("table {width: 100%; border-collapse: collapse; margin-top: 20px;}");
            out.println("th, td {padding: 12px; text-align: left; border: 1px solid #ddd;}");
            out.println("th {background-color: #f4f4f4;}");
            out.println("a {display: inline-block; margin-top: 20px; text-decoration: none; color: #0066cc; font-weight: bold;}");
            out.println("a:hover {color: #004099;}");
            out.println("</style></head><body>");
            out.println("<div class='container'><h1>My Appointments</h1>");
            out.println("<table><thead><tr><th>Doctor Name</th><th>Specialization</th><th>Date</th><th>Time</th></tr></thead><tbody>");

            while (rs.next()) {
                out.println("<tr><td>" + rs.getString("doctor_name") + "</td>");
                out.println("<td>" + rs.getString("specialization") + "</td>");
                out.println("<td>" + rs.getString("appointment_date") + "</td>");
                out.println("<td>" + rs.getString("appointment_time") + "</td></tr>");
            }

            out.println("</tbody></table>");
            out.println("<a href='profile.html'>Back to Profile</a> | <a href='index.html'>Home</a>");
            out.println("</div></body></html>");

        } catch (Exception e) {
            e.printStackTrace();
            out.println("<p>Error retrieving appointments. Please try again later.</p>");
        } finally {
            try {
                if (conn != null) conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
