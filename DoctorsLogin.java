package com.login;

import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import javax.servlet.annotation.WebServlet;
import javax.servlet.ServletException;
import javax.servlet.ServletContext;

@WebServlet("/DoctorsLogin")
public class DoctorsLogin extends HttpServlet {
	protected void doPost(HttpServletRequest req,HttpServletResponse res) throws ServletException,IOException{
		res.setContentType("text/html");
		PrintWriter out = res.getWriter();
		
		String UserName = req.getParameter("doctor_name");
		String Password = req.getParameter("doctor_password");
		
		ServletContext context = getServletContext();
		String driver = context.getInitParameter("jdbc.driver");
		String url = context.getInitParameter("jdbc.url");
		String dbusername = context.getInitParameter("jdbc.username");
		String dbpassword = context.getInitParameter("jdbc.password");
		
		Connection conn=null;
		
		try {
			
			Class.forName(driver);
			conn = DriverManager.getConnection(url,dbusername,dbpassword);
			String sql = "select * from doctorlogin where name=? and password=?";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, UserName);
			pstmt.setString(2, Password);
			
			ResultSet rs = pstmt.executeQuery();
			
			if(rs.next()) {
				req.getRequestDispatcher("PatientHistory").include(req, res);
			}
			else {
				out.println("<h3> style='color:red;'>Error occured while logging in. Please try again.</h3>");
				req.getRequestDispatcher("DoctorsLogin.html").include(req, res);
			}
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
