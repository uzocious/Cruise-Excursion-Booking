package application.model;

import java.util.Random;

import org.apache.commons.lang3.StringUtils;

public class Main_Model {
	
	// Local variables
	private String customerID = null;
	private String adminID = null;
	
	private int userID = 0;
	
	private final int min = 10000000;
	private final int max = 99999999;
	
	private int generatedNoForCustomerID = 0;
	private int generatedNoForUserID = 0;
	
	Database db = new Database();
	
	// Constructor
	public Main_Model(){}
	
	
	//Sign in
	public boolean signIn(String username, String password)
	{
		db.DBConnection();
				
		boolean confirmation = false;
				 
		try {
					
			// Executes SQL query for customer users
			String query = String.format("SELECT user_customer.Username, user_customer.Customer_ID, AES_DECRYPT(user_customer.Password, 'key0000')\r\n" + 
					"FROM user_customer  \r\n" + 
					"WHERE user_customer.Username = '%s' AND user_customer.Password = AES_ENCRYPT('%s', 'key0000')", username, password);
					 
			db.resultSet = db.statement.executeQuery(query);
									 
			// gets the query result from the table user_customer		 
			if(db.resultSet.next())
			{
				confirmation = true;
				customerID = db.resultSet.getString("user_customer.Customer_ID");
			}
					 
					 
			// Executes SQL query for administrator users
			String query2 = String.format("SELECT user_admin.Username, user_admin.Admin_ID, AES_DECRYPT(user_admin.Password, 'key0000')\r\n" + 
					"FROM user_admin\r\n" + 
					"WHERE user_admin.Username = '%s' AND user_admin.Password = AES_ENCRYPT('%s', 'key0000')", username, password);
					 
			db.resultSet2 = db.statement.executeQuery(query2);
									 
			// gets the query result from the table user_admin		 
			if(db.resultSet2.next())
			{
				confirmation = true;
				adminID = db.resultSet2.getString("user_admin.Admin_ID");
			}		 
			}	 
			catch(Exception e)
			{
				System.out.println(e);
			}
			finally
			{
				try {
					db.connection.close();
					db.statement.close();
					db.resultSet.close();
					db.resultSet2.close();
				}
				catch(Exception e )
				{
					System.out.println(e);
				}		
			}
				
			return confirmation;
	}
			
	
	// Forget Password
	public boolean forgetPassword(String email, String new_password, String re_enterPassword)
	{
		db.DBConnection();
		
		boolean confirmation = false;
		
		try {
					
			// Executes SQL query
			String query = String.format("Select customer.Customer_ID, customer.Email_Address\r\n" + 
					"FROM customer\r\n" + 
					"WHERE customer.Email_Address = '%s'", email);
			
			db.resultSet = db.statement.executeQuery(query);
			 
			// gets the query result from the table customer		 
			if(db.resultSet.next())
			{
				//gets customer ID from the database
				customerID = db.resultSet.getString("customer.Customer_ID");
				
				//Compares the new password with the re-entered password
				if(re_enterPassword.equals(new_password) && (!new_password.isEmpty() || !re_enterPassword.isEmpty())) 
				{
					// Executes SQL query
					String query1 = String.format("UPDATE user_customer\r\n" + 
							"SET user_customer.Password = AES_ENCRYPT('%s','key0000')\r\n" + 
							"WHERE user_customer.Customer_ID = '%s';", new_password, customerID);
					
					db.statement.execute(query1);
					confirmation = true;
				}
			}
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
		finally
		{
			try {
				db.connection.close();
				db.statement.close();
				db.resultSet.close();
			}
			catch(Exception e )
			{
				System.out.println(e);
			}
		}
		
		return confirmation;
				
	}
	
	
	// Register
	public boolean registration(String firstName, String lastName, String email, String cabinNo, String username, String password, String re_enterPassword)
	{
		db.DBConnection();
		
		boolean confirmation = false;
		
		firstName = StringUtils.capitalize(firstName);
		lastName = StringUtils.capitalize(lastName);
		email = email.toLowerCase();
		cabinNo = cabinNo.toUpperCase();
		username = username.toLowerCase();
		
		customerID = newCustomerID();
		userID = newUserID();
		
		if (!firstName.isEmpty() || !lastName.isEmpty() || !cabinNo.isEmpty())
		{
			if (isEmailValid(email) == true)
			{
				if (isCabinNumberValid(cabinNo) == true)
				{
					if(doesEmailExistsInDB(email) != true)
					{
						if(doesUsernameExistsInDB(username) != true)
						{
							if(re_enterPassword.equals(password) && (!password.isEmpty() || !re_enterPassword.isEmpty())) 
							{
								try {
									// Executes SQL query
									String query = String.format("INSERT INTO customer (customer.Customer_ID, customer.First_Name, customer.Last_Name, customer.Email_Address, customer.Cabin_No)\r\n" + 
											"VALUES ('%s','%s','%s','%s','%s');", customerID, firstName, lastName, email, cabinNo);
									
									db.statement.execute(query);
									
									String query2 = String.format("INSERT INTO user_customer(user_customer.User_ID, user_customer.Username, user_customer.Password, user_customer.Type, user_customer.Customer_ID)\r\n" + 
											"VALUES (%s, '%s', AES_ENCRYPT('%s','key0000'), 'Customer','%s')", userID, username, password, customerID);
									
									db.statement.execute(query2);
									
									confirmation = true;
								}
								catch(Exception e)
								{
									System.out.println(e);
								}
								finally
								{
									try {
										db.connection.close();
										db.statement.close();
									}
									catch(Exception e )
									{
										System.out.println(e);
									}
								}
							}
							else { confirmation = false;}
						}
						else{ confirmation = false;}
					}
					else { confirmation = false;}
				}
				else { confirmation = false;}
			}
			else { confirmation = false;}
		}
		else { confirmation = false;}
		
		
		return confirmation;
	}
	
	
	// Gets Customer's ID
	public String getCustomerID() {
		return customerID;
	}
			
			
	// Gets Administrator's ID
	public String getAdminID() {
		return adminID;
	}
	
	
	// Checks email validity
	public boolean isEmailValid(String email) {
		boolean result = false;
		String pattern = "^.+@.+\\..+$";
		
		if(email.matches(pattern)){
			result = true;
		}		
		return result;
	}
	
	
	// Checks cabin number validity
	public boolean isCabinNumberValid(String cabinNo){
		boolean result = false;
	    String pattern = "^[a-zA-Z0-9]*$";
	    
	    if(isAlphabet(cabinNo) == true){
	    	result = false;
	    }
	    else
	    {
	    	if(isNumber(cabinNo) == true){
	    		result = false;
	    	}
	    	else
	    	{
	    		if(cabinNo.matches(pattern) && cabinNo.length() == 7)
			    {
			      result = true;
			    }
	    	}
	    }
	    
		return result;
	}
	
	
	// Checks if string contains only alphabets
	private boolean isAlphabet(String cabinNo){
		boolean result = false;
		
		String pattern = "[a-zA-Z]+";
		
		if(cabinNo.matches(pattern)){
			result = true;
		}
		
		return result;
	}
	
	
	// Checks if string contains only numbers
	private boolean isNumber(String cabinNo){
		boolean result = false;
		
		String pattern = "[0-9]+";
		
		if(cabinNo.matches(pattern)){
			result = true;
		}
		
		return result;
	}
	
	
	// Checks if email already exists in database
	public boolean doesEmailExistsInDB(String email){
		db.DBConnection();
		
		boolean result = false;
		
		try {
			// Executes SQL query for customer
			String query = String.format("SELECT customer.Email_Address\r\n" + 
					"FROM customer\r\n" + 
					"WHERE customer.Email_Address = '%s'", email);
					 
			db.resultSet = db.statement.executeQuery(query);

			// gets the query result from the table customer		 
			if(db.resultSet.next())
			{
				result = true;
			}
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
		
		return result;
	}
	
	
	// Checks if username already exists in database
	public boolean doesUsernameExistsInDB(String username){
		db.DBConnection();
		
		boolean result = false;
		
		try {
			// Executes SQL query for customer
			String query = String.format("SELECT user_customer.Username\r\n" + 
					"FROM user_customer\r\n" + 
					"WHERE user_customer.Username = '%s'", username);
					 
			db.resultSet = db.statement.executeQuery(query);

			// gets the query result from the table customer		 
			if(db.resultSet.next())
			{
				result = true;
			}
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
		
		return result;
	}

	
	// Creates new customer ID
	private String newCustomerID(){
		String result;
		
		boolean itr = generateNumberForCustomerID();
		
		while(itr != true) {
			generateNumberForCustomerID();
		}

		result = String.format("CM%s", generatedNoForCustomerID);
		return result;
	}
	
	
	// Generates number for customer id
	private boolean generateNumberForCustomerID(){
		db.DBConnection();
		
		boolean result = false;
		
		int randomNumber = 0;
		Random random = new Random();
		randomNumber = random.nextInt(max - min) + min;
		
		try {
			// Executes SQL query for customer
			String query = String.format("SELECT customer.Customer_ID \r\n" + 
					"FROM customer\r\n" + 
					"WHERE customer.Customer_ID = 'CM%s'", randomNumber);
					 
			db.resultSet = db.statement.executeQuery(query);

			// gets the query result from the table customer		 
			if(db.resultSet.next())
			{
				result = false;
			}
			else
			{
				generatedNoForCustomerID = randomNumber;
				result = true;
			}
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
		finally
		{
			try {
				db.connection.close();
				db.statement.close();
				db.resultSet.close();
			}
			catch(Exception e )
			{
				System.out.println(e);
			}	
		}
		
		return result;
	}
	

	// Creates new user ID
	private int newUserID(){
		boolean itr = generateNumberForUserID();
		
		while(itr != true) {
			generateNumberForUserID();
		}
		
		return generatedNoForUserID;
	}
	
	
	// Generates number for customer's user id
	private boolean generateNumberForUserID(){
		db.DBConnection();
		
		boolean result = false;
		
		final int min = 100000;
		final int max = 999999;
		
		int randomNumber = 0;
		Random random = new Random();
		randomNumber = random.nextInt(max - min) + min;
		
		try {
			// Executes SQL query for customer
			String query = String.format("SELECT user_customer.User_ID\r\n" + 
					"FROM user_customer\r\n" + 
					"WHERE user_customer.User_ID = %s", randomNumber);
					 
			db.resultSet = db.statement.executeQuery(query);

			// gets the query result from the table customer		 
			if(db.resultSet.next())
			{
				result = false;
			}
			else
			{
				generatedNoForUserID = randomNumber;
				result = true;
			}
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
		finally
		{
			try {
				db.connection.close();
				db.statement.close();
				db.resultSet.close();
			}
			catch(Exception e )
			{
				System.out.println(e);
			}	
		}
		
		return result;
	}

	
}
