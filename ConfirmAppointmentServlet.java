package com.login;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/ConfirmAppointmentServlet")
public class ConfirmAppointmentServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        HttpSession session = request.getSession();
        String patientName = (String) session.getAttribute("username");
        String doctorName = request.getParameter("doctorName");
        String specialization = request.getParameter("specialization");
        String appointmentDate = request.getParameter("appointmentDate"); // Format: YYYY-MM-DD
        String appointmentTime = request.getParameter("appointmentTime");
        

        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            ServletContext context = getServletContext();
            String driver = context.getInitParameter("jdbc.driver");
            String url = context.getInitParameter("jdbc.url");
            String username = context.getInitParameter("jdbc.username");
            String password = context.getInitParameter("jdbc.password");

            Class.forName(driver);
            conn = DriverManager.getConnection(url, username, password);

            String sql = "INSERT INTO appointments (patient_name, doctor_name, specialization, appointment_date, appointment_time) VALUES (?, ?, ?, TO_DATE(?, 'YYYY-MM-DD'), TO_TIMESTAMP(?, 'HH24:MI:SS'))";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, patientName);
            pstmt.setString(2, doctorName);
            pstmt.setString(3, specialization);
            pstmt.setString(4, appointmentDate);
            pstmt.setString(5, appointmentTime);

            int rowsInserted = pstmt.executeUpdate();
            if (rowsInserted > 0) {
                out.println("<html>");
                out.println("<head>");
                out.println("<style>");
                out.println("body {");
                out.println("    font-family: Arial, sans-serif;");
                out.println("    text-align: center;");
                out.println("    background-image: url('img1.jpg');");
                out.println("    background-size: cover;");
                out.println("    color: white;");
                out.println("}");
                out.println("div.container {");
                out.println("    background: rgba(0, 0, 0, 0.7);");
                out.println("    padding: 20px;");
                out.println("    margin: 50px auto;");
                out.println("    border-radius: 10px;");
                out.println("    width: 50%;");
                out.println("    box-shadow: 0px 4px 8px rgba(0, 0, 0, 0.2);");
                out.println("}");
                out.println("h1 {");
                out.println("    margin-bottom: 20px;");
                out.println("}");
                out.println("p {");
                out.println("    font-size: 18px;");
                out.println("    margin: 10px 0;");
                out.println("}");
                out.println("a {");
                out.println("    color: #00c3ff;");
                out.println("    text-decoration: none;");
                out.println("    font-weight: bold;");
                out.println("}");
                out.println("a:hover {");
                out.println("    text-decoration: underline;");
                out.println("}");
                out.println("</style>");
                out.println("</head>");
                out.println("<body>");
                out.println("<div class='container'>");
                out.println("<h1>Appointment Confirmed Successfully!</h1>");
                out.println("<p><strong>Doctor:</strong> " + doctorName + "</p>");
                out.println("<p><strong>Specialization:</strong> " + specialization + "</p>");
                out.println("<p><strong>Date:</strong> " + appointmentDate + "</p>");
                out.println("<p><strong>Time:</strong> " + appointmentTime + "</p>");
                out.println("<a href='profile.html'>Go to Profile</a> | <a href='index.html'>Home</a>");
                out.println("</div>");
                out.println("</body>");
                out.println("</html>");
            } else {
                out.println("<html>");
                out.println("<head>");
                out.println("<style>");
                out.println("body {");
                out.println("    font-family: Arial, sans-serif;");
                out.println("    text-align: center;");
                out.println("    background-image: url('background.jpg');");
                out.println("    background-size: cover;");
                out.println("    color: white;");
                out.println("}");
                out.println("div.container {");
                out.println("    background: rgba(255, 0, 0, 0.7);");
                out.println("    padding: 20px;");
                out.println("    margin: 50px auto;");
                out.println("    border-radius: 10px;");
                out.println("    width: 50%;");
                out.println("    box-shadow: 0px 4px 8px rgba(0, 0, 0, 0.2);");
                out.println("}");
                out.println("h1 {");
                out.println("    margin-bottom: 20px;");
                out.println("}");
                out.println("a {");
                out.println("    color: #00c3ff;");
                out.println("    text-decoration: none;");
                out.println("    font-weight: bold;");
                out.println("}");
                out.println("a:hover {");
                out.println("    text-decoration: underline;");
                out.println("}");
                out.println("</style>");
                out.println("</head>");
                out.println("<body>");
                out.println("<div class='container'>");
                out.println("<h1>Error: Unable to book appointment.</h1>");
                out.println("<a href='profile.html'>Go to Profile</a> | <a href='index.html'>Home</a>");
                out.println("</div>");
                out.println("</body>");
                out.println("</html>");
            }

        } catch (Exception e) {
            e.printStackTrace(out);
        } finally {
            try {
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (Exception e) {
                e.printStackTrace(out);
            }
        }
    }
}
