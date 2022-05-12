
package application.model;

import java.sql.*;

public class Database {
	
		// Database variables
		public Connection connection;		
		public Statement statement;
		public ResultSet resultSet;
		public ResultSet resultSet2;
		
		// Database connection details
		final String DRIVER = "com.mysql.jdbc.Driver"; // JDBC driver
		final String DATABASE = "cruise_excursion_booking"; // the database name
		final String HOST = "localhost"; // the database host
		
		// Disable SSL and suppress the SSL errors
		final String DisableSSL = "?autoReconnect=true&useSSL=false";
		
		// Database full URL
		final String DATABASE_URL = "jdbc:mysql://" + HOST + "/" + DATABASE + DisableSSL;
		
		// Database username and password
		final String USERNAME = "root"; // the database login username
		final String PASSWORD = ""; // the database login password 

		
		// Constructor
		public Database(){}
		
		
		// Database Connection
		public void DBConnection()

		{
			try{
				
				// Loads driver
				Class.forName(DRIVER);
				
				// Connects to Database
				connection = DriverManager.getConnection(DATABASE_URL,USERNAME,PASSWORD);
				
				// Creates SQL statement
				statement = connection.createStatement();
			
			}
			catch(Exception e)
			{
				System.out.println("Connection Denied!");
			}
		}

}
