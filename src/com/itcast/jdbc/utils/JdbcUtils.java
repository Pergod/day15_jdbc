package com.itcast.jdbc.utils;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class JdbcUtils {
	
	private static Properties properties=new Properties();
	
	static{
		try {
			properties.load(JdbcUtils.class.getClassLoader().getResourceAsStream("db.properties"));
			Class.forName(properties.getProperty("driver"));
		} catch (ClassNotFoundException | IOException e) {
			throw new ExceptionInInitializerError();
		}
	}
	
	public static Connection getConnection() throws SQLException {
		Connection connection=DriverManager.getConnection(
					properties.getProperty("url"),
					properties.getProperty("user"),
					properties.getProperty("password"));
		return connection;
	}
	public static void release(ResultSet results,Statement statement,Connection connection) {
		if (results!=null) {
			try {
				results.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if (statement!=null) {
			try {
				statement.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
		}
		if (connection!=null) {
			try {
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}
