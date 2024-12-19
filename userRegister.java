package com.login;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import javax.servlet.ServletContext;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.ServletException;

@WebServlet("/userRegister")
public class userRegister extends HttpServlet {
	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException{
		
		String Username = req.getParameter("name");
		String UserPassword = req.getParameter("password");
		String confirmPassword = req.getParameter("confirm_password");
		String Contact = req.getParameter("contact");
		String Email = req.getParameter("email");
		String Country = req.getParameter("country");
		String State = req.getParameter("state");
		String Zip = req.getParameter("zip");
		
		Connection conn = null;
		res.setContentType("text/html");
		PrintWriter out = res.getWriter();
		
		 if (!UserPassword.equals(confirmPassword)) {
	            out.println("Passwords do not match!");
	            req.getRequestDispatcher("signup.html").include(req, res);
	            return;
	        }

		
		try {
			
			
			
			ServletContext context = getServletContext();
			String Driver = context.getInitParameter("jdbc.driver");
			String Url = context.getInitParameter("jdbc.url");
			String OracleUsername = context.getInitParameter("jdbc.username");
			String OraclePassword = context.getInitParameter("jdbc.password");
			
			Class.forName(Driver);
			conn = DriverManager.getConnection(Url,OracleUsername,OraclePassword);
			
			
			
			String query = "insert into userlogin(name,password,contact,email,country,state,zip_code) values(?,?,?,?,?,?,?)";
			PreparedStatement pstmt = conn.prepareStatement(query);
			pstmt.setString(1,Username);
			pstmt.setString(2,UserPassword);
			pstmt.setString(3, Contact);
			pstmt.setString(4, Email);
			pstmt.setString(5, Country);
			pstmt.setString(6, State);
			pstmt.setString(7, Zip);
			
			
			
			int rowsInserted = pstmt.executeUpdate();
			if(rowsInserted>0) {
				System.out.println("success");
				req.getRequestDispatcher("regsuccess.html").include(req, res);
			} else {
				req.getRequestDispatcher("signup.html").include(req, res);
			}
			
			
			
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			try {
				 conn.close();
				
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		
	}
}
