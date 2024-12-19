package com.login;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.servlet.ServletContext;

public class AdminLoginDao {
	
	public static boolean validate(ServletContext context, String name, String password) {
		boolean status = false;
		try {
			String driver = context.getInitParameter("jdbc.driver");
			String url = context.getInitParameter("jdbc.url");
			String OracleUsername = context.getInitParameter("jdbc.username");
			String Oraclepassword = context.getInitParameter("jdbc.password");
			
			Class.forName(driver);
			Connection conn = DriverManager.getConnection(url,OracleUsername,Oraclepassword);
			
			String sql = "select * from userlogin where name=? and password=?";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, name);
			pstmt.setString(2, password);
			
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()) {
				status = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return status;
	}
}
