package com.login;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

@WebServlet("/PatientHistory")
public class PatientHistory extends HttpServlet {
	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException{
		res.setContentType("text/html");
		PrintWriter out = res.getWriter();
		
		ServletContext context = req.getServletContext();
		
		String driver = context.getInitParameter("jdbc.driver");
		String url = context.getInitParameter("jdbc.url");
		String dbusername = context.getInitParameter("jdbc.username");
		String dbpassword = context.getInitParameter("jdbc.password");
		Connection conn = null;
		
		try {
			Class.forName(driver);
			conn = DriverManager.getConnection(url,dbusername,dbpassword);
			PreparedStatement psmt = conn.prepareStatement("select * from appointments");
			ResultSet rs = psmt.executeQuery();
			
			out.println("<html><head>");
		    out.println("<style>");
		    out.println("body {");
		    out.println("    background-image: url('img1.jpg');");
		    out.println("    background-size: cover;");
		    out.println("    font-family: Arial, sans-serif;");
		    out.println("    color: white;");
		    out.println("    text-align: center;");
		    out.println("}");
		    out.println("table {");
		    out.println("    width: 80%;");
		    out.println("    margin: 20px auto;");
		    out.println("    border-collapse: collapse;");
		    out.println("    background-color: rgba(0, 0, 0, 0.8);");
		    out.println("}");
		    out.println("th, td {");
		    out.println("    border: 1px solid #ddd;");
		    out.println("    padding: 8px;");
		    out.println("    color: white;");
		    out.println("}");
		    out.println("th {");
		    out.println("    background-color: #4CAF50;");
		    out.println("}");
		    out.println("td a {");
		    out.println("    color: #ff6666;");
		    out.println("    text-decoration: none;");
		    out.println("}");
		    out.println("td a:hover {");
		    out.println("    text-decoration: underline;");
		    out.println("}");
		    out.println("h1 {");
		    out.println("    margin-top: 20px;");
		    out.println("    color: white;");
		    out.println("}");
		    out.println("a {");
		    out.println("    color: darkblue;");
		    out.println("    text-decoration: none;");
		    out.println("    font-size: 16px;");
		    out.println("}");
		    out.println("a:hover {");
		    out.println("    text-decoration: underline;");
		    out.println("}");
		    out.println("</style>");
		    out.println("</head><body>");

		    out.println("<h1>Patient History</h1>");
		    out.println("<table>");
		    out.println("<tr><th>Patient Name</th><th>Doctor Name</th><th>Specialization</th><th>Date</th><th>Time</th><th>Action</th></tr>");
			
            while (rs.next()) {
                String patientName = rs.getString("patient_name");
                String doctorName = rs.getString("doctor_name");
                String specialization = rs.getString("specialization");
                Date appointmentDate = rs.getDate("appointment_date");
                Timestamp appointmentTime = rs.getTimestamp("appointment_time");
                
                out.println("<tr>");
                out.println("<td>" + patientName + "</td>");
                out.println("<td>" + doctorName + "</td>");
                out.println("<td>" + specialization + "</td>");
                out.println("<td>" + appointmentDate + "</td>");
                out.println("<td>" + appointmentTime + "</td>");
                out.println("<td><a href='DeletePatientServlet?patientName=" + patientName + "'>Delete</a></td>");
                out.println("</tr>");
            }
            out.println("</table>");
            out.println("<br><a href='index.html'>Go to Home</a>");
            out.println("</body></html>");
            
            
		} catch(Exception e) {
			e.printStackTrace();
			out.println("<h2>Error fetching patient history.</h2>");
		}
	}

}
