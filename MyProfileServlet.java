package com.login;


import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.servlet.ServletContext;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/MyProfileServlet")
public class MyProfileServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        
        HttpSession session = request.getSession();
        String userName = (String) session.getAttribute("username");// Retrieve username from session
        System.out.println("UserName from session: " + userName); 

        if (userName == null) {
            response.sendRedirect("userlogin.html");
            
        }

        // HTML Header
        out.println("<!DOCTYPE html>");
        out.println("<html lang='en'>");
        out.println("<head>");
        out.println("<meta charset='UTF-8'>");
        out.println("<meta name='viewport' content='width=device-width, initial-scale=1.0'>");
        out.println("<title>My Profile</title>");
        out.println("<style>");
        out.println("body { font-family: Arial, sans-serif; text-align: center; background-image: url('img1.jpg'); background-size: cover; color:white; }");
        out.println(".container { margin: 20px auto; width: 60%; background: rgba(0, 0, 0, 0.7); padding: 20px; box-shadow: 0 2px 4px rgba(0,0,0,0.1); border-radius: 10px; }");
        out.println("h1 { text-align: center; color: white; }");
        out.println(".details { margin: 20px 0; }");
        out.println(".details p { font-size: 18px; margin: 10px 0; }");
        out.println("</style>");
        out.println("</head>");
        out.println("<body>");
        out.println("<div class='container'>");
        out.println("<h1>My Profile</h1>");

        try {
            ServletContext context = getServletContext();
            String driver = context.getInitParameter("jdbc.driver");
            String url = context.getInitParameter("jdbc.url");
            String username = context.getInitParameter("jdbc.username");
            String password = context.getInitParameter("jdbc.password");

            Class.forName(driver);
            Connection conn = DriverManager.getConnection(url, username, password);

            String query = "SELECT name, contact, email, country, state, zip_code FROM userlogin WHERE name = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, userName);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                out.println("<div class='details'>");
                out.println("<p><strong>Name:</strong> " + rs.getString("name") + "</p>");
                out.println("<p><strong>Contact:</strong> " + rs.getString("contact") + "</p>");
                out.println("<p><strong>Email:</strong> " + rs.getString("email") + "</p>");
                out.println("<p><strong>Country:</strong> " + rs.getString("country") + "</p>");
                out.println("<p><strong>State:</strong> " + rs.getString("state") + "</p>");
                out.println("<p><strong>Zip Code:</strong> " + rs.getString("zip_code") + "</p>");
                out.println("</div>");
            } else {
                out.println("<p style='color:red;'>No user details found!</p>");
            }

            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
            out.println("<p style='color:red;'>Error fetching user details.</p>");
        }

        out.println("<a href='profile.html'>Back to Profile</a>");
        out.println("</div>");
        out.println("</body>");
        out.println("</html>");
    }
}

