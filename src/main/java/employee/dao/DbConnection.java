package employee.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import employee.exception.DbException;

public class DbConnection {

	private static final String SCHEMA = "employee";
	private static final String USER = "employee";
	private static final String PASSWORD = "employee";
	private static final String HOST = "localhost";
	private static final int PORT = 3306;

	public static Connection getConnection() {
		String url = String.format("jdbc:mysql://%s:%d/%s?user=%s&password=%s&USESSL=FALSE",HOST,PORT,SCHEMA,USER,PASSWORD);
		//System.out.println("Connection with url="+url);
		
			Connection conn;
			try {
				conn = DriverManager.getConnection(url);
				//System.out.println("Success");

				return conn;

			} catch (SQLException e) {
				throw new DbException(e);
			}


		
		
	}
}
