package com.login;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.DriverManager;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;

@WebServlet("/DeletePatientServlet")
public class DeletePatientHistory extends HttpServlet {
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String patientName = req.getParameter("patientName");

        res.setContentType("text/html");
        PrintWriter out = res.getWriter();
        
        ServletContext context = getServletContext();
        String driver = context.getInitParameter("jdbc.driver");
        String url = context.getInitParameter("jdbc.url");
        String dbUsername = context.getInitParameter("jdbc.username");
        String dbPassword = context.getInitParameter("jdbc.password");
        System.out.println("My name");
        Connection conn = null;

        try{
        	
        	Class.forName(driver);
        	conn = DriverManager.getConnection(url,dbUsername,dbPassword);
            PreparedStatement pst = conn.prepareStatement("DELETE FROM appointments WHERE patient_name = ?");
            pst.setString(1, patientName);

            int rows = pst.executeUpdate();
            
            out.println("<html>");
            out.println("<head>");
            out.println("<style>");
            out.println("body {");
            out.println("    font-family: Arial, sans-serif;");
            out.println("    background-image: url('background.jpg');");
            out.println("    background-size: cover;");
            out.println("    display: flex;");
            out.println("    justify-content: center;");
            out.println("    align-items: center;");
            out.println("    height: 100vh;");
            out.println("    margin: 0;");
            out.println("}");
            out.println(".container {");
            out.println("    background-color: rgba(255, 255, 255, 0.8);");
            out.println("    padding: 20px;");
            out.println("    border-radius: 10px;");
            out.println("    text-align: center;");
            out.println("    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);");
            out.println("}");
            out.println("a {");
            out.println("    display: inline-block;");
            out.println("    margin-top: 10px;");
            out.println("    text-decoration: none;");
            out.println("    padding: 10px 20px;");
            out.println("    background-color: #007BFF;");
            out.println("    color: white;");
            out.println("    border-radius: 5px;");
            out.println("}");
            out.println("a:hover {");
            out.println("    background-color: #0056b3;");
            out.println("}");
            out.println("</style>");
            out.println("</head>");
            out.println("<body>");
            out.println("<div class='container'>");
            
            if (rows > 0) {
                out.println("<h2>Patient history deleted successfully.</h2>");
            } else {
                out.println("<h2>Error deleting patient history.</h2>");
            }
            out.println("<a href='adminLogin.html'>Go Back</a>");
            out.println("</div>");
            out.println("</body>");
            out.println("</html>");

        } catch (Exception e) {
            e.printStackTrace();
            out.println("<h2>Error deleting patient history.</h2>");
        }
    }
}
