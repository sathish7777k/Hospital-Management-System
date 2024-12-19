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

@WebServlet("/DoctorListServlet")
public class DoctorListServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        // HTML header
        out.println("<!DOCTYPE html>");
        out.println("<html lang='en'>");
        out.println("<head>");
        out.println("<meta charset='UTF-8'>");
        out.println("<meta name='viewport' content='width=device-width, initial-scale=1.0'>");
        out.println("<title>Doctor List</title>");
        out.println("<style>");
        out.println("body { font-family: Arial, sans-serif; background: #f4f4f9; margin: 0; padding: 20px; }");
        out.println("body::before{content: \"\";\n"
        		+ "    position: absolute;\n"
        		+ "    top: 0;\n"
        		+ "    left: 0;\n"
        		+ "    width: 100%;\n"
        		+ "    height: 100%;\n"
        		+ "    background: url('img1.jpg') no-repeat center center/cover; \n"
        		+ "    opacity: 0.5; \n"
        		+ "    z-index: -1; }");
        out.println(".container { margin: 20px auto; width: 80%; background: white; padding: 20px; box-shadow: 0 2px 4px rgba(0,0,0,0.1); border-radius: 10px; }");
        out.println("h1 { text-align: center; color: #333; }");
        out.println("table { width: 100%; border-collapse: collapse; margin: 20px 0; }");
        out.println("table, th, td { border: 1px solid #ddd; }");
        out.println("th, td { padding: 10px; text-align: left; }");
        out.println("th { background: #f2f2f2; }");
        out.println(".book-btn { background-color: #4CAF50; color: white; padding: 5px 10px; border: none; border-radius: 3px; cursor: pointer; }");
        out.println(".book-btn:hover { background-color: #45a049; }");
        out.println("</style>");
        out.println("</head>");
        out.println("<body>");
        out.println("<div class='container'>");
        out.println("<h1>Available Doctors</h1>");
        out.println("<table>");
        out.println("<tr><th>Name</th><th>Specialization</th><th>Action</th></tr>");

        // Database connection
        try {
            ServletContext context = getServletContext();
            String driver = context.getInitParameter("jdbc.driver");
            String url = context.getInitParameter("jdbc.url");
            String username = context.getInitParameter("jdbc.username");
            String password = context.getInitParameter("jdbc.password");

            Class.forName(driver);
            Connection conn = DriverManager.getConnection(url, username, password);

            // Query to fetch doctors
            String query = "SELECT name, specialization FROM doctors";
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            // Dynamically populate table rows
            while (rs.next()) {
                String doctorName = rs.getString("name");
                String specialization = rs.getString("specialization");

                out.println("<tr>");
                out.println("<td>" + doctorName + "</td>");
                out.println("<td>" + specialization + "</td>");
                out.println("<td><form action='AppointmentBookingServlet' method='post'>");
                out.println("<input type='hidden' name='doctorName' value='" + doctorName + "'>");
                out.println("<input type='hidden' name='specialization' value='" + specialization + "'>");
                out.println("<input type='submit' class='book-btn' value='Book'></form></td>");
                out.println("</tr>");
            }

            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
            out.println("<p style='color:red;'>Error fetching doctor list.</p>");
        }

        // HTML footer
        out.println("</table>");
        out.println("<a href='profile.html'>Back to Profile</a>");
        out.println("</div>");
        out.println("</body>");
        out.println("</html>");
    }
}
