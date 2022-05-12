package application.model;

import org.apache.commons.lang3.StringUtils;
import application.main.MainController;



public class Profile {
	
	// Local fields
	private String _customerID = MainController._customerID;
	
	private String _customerFirstName;
	private String _customerLastName;
	private String _customerEmail;
	private String _customerCabinNumber;
	private String _customerUserName;
	
	// Database object
	Database db = new Database();
	
	// Main model object
	Main_Model mainModel = new Main_Model();
	
	
	// Constructor
	public Profile() {}
	
	
	// Gets customer's details
	public void getCustomerDetails() 
	{
		db.DBConnection();
		 
		try {		
			// Executes SQL query
			String query = String.format("SELECT customer.First_Name, customer.Last_Name, customer.Email_Address, customer.Cabin_No, user_customer.Username\r\n" + 
					"FROM customer\r\n" + 
					"JOIN user_customer ON user_customer.Customer_ID = customer.Customer_ID\r\n" + 
					"WHERE customer.Customer_ID = '%s'", _customerID);
					 
			db.resultSet = db.statement.executeQuery(query);
									 
			// gets the query result	 
			if(db.resultSet.next())
			{
				_customerFirstName = db.resultSet.getString("customer.First_Name");
				_customerLastName = db.resultSet.getString("customer.Last_Name");
				_customerEmail = db.resultSet.getString("customer.Email_Address");
				_customerCabinNumber = db.resultSet.getString("customer.Cabin_No");
				_customerUserName = db.resultSet.getString("user_customer.Username");
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
	}
	
	
	// Updates customer's details
	public boolean updateCustomerDetails(String firstName, String lastName, String email, String cabinNo)
	{
		db.DBConnection();
		
		boolean confirmation = false;
		
		firstName = StringUtils.capitalize(firstName);
		lastName = StringUtils.capitalize(lastName);
		email = email.toLowerCase();
		cabinNo = cabinNo.toUpperCase();
		
		
		if (!firstName.isEmpty() || !lastName.isEmpty() || !cabinNo.isEmpty())
		{
			if (mainModel.isEmailValid(email) == true)
			{
				if (mainModel.isCabinNumberValid(cabinNo) == true)
				{
					if (_customerEmail.equals(email))
					{
						try {
							// Executes SQL query
							String query = String.format("UPDATE customer\r\n" + 
									"SET First_Name = '%s', Last_Name = '%s', Email_Address = '%s', Cabin_No = '%s'\r\n" + 
									"WHERE customer.Customer_ID = '%s';", firstName, lastName, email, cabinNo, _customerID);
							
							db.statement.execute(query);
							
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
					else if(mainModel.doesEmailExistsInDB(email) != true)
					{
						try {
							// Executes SQL query
							String query = String.format("UPDATE customer\r\n" + 
									"SET First_Name = '%s', Last_Name = '%s', Email_Address = '%s', Cabin_No = '%s'\r\n" + 
									"WHERE customer.Customer_ID = '%s';", firstName, lastName, email, cabinNo, _customerID);
							
							db.statement.execute(query);
							
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
					else {confirmation = false;}
				}
				else {confirmation = false;}
			}
			else {confirmation = false;}
		}
		else {confirmation = false;}
			
		
		return confirmation;
	}
	
	
	// Updates customer's password
	public boolean updateCustomerPassword(String newPassword, String reEnterPassword)
	{
		db.DBConnection();
		
		boolean confirmation = false;
		
		try {
			//Compares the new password with the re-entered password
			if(reEnterPassword.equals(newPassword) && (!newPassword.isEmpty() || !reEnterPassword.isEmpty())) 
			{
				// Executes SQL query
				String query = String.format("UPDATE user_customer\r\n" + 
						"SET user_customer.Password = AES_ENCRYPT('%s','key0000')\r\n" + 
						"WHERE user_customer.Customer_ID = '%s';", newPassword, _customerID);
				
				db.statement.execute(query);
				confirmation = true;
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
			}
			catch(Exception e )
			{
				System.out.println(e);
			}
		}
		
		return confirmation;
	}
	
	
	// Gets customer's ID
	public String getCustomerID() {
		return _customerID;
	}
	
	
	// Gets customer's first name
	public String getCustomerFirstName() {
		return _customerFirstName;
	}
	
	
	// Gets customer's last name
	public String getCustomerLastName() {
		return _customerLastName;
	}
	
	
	// Gets customer's email
	public String getCustomerEmail() {
		return _customerEmail;
	}
	
	
	// Gets customer's cabin number
	public String getCustomerCabinNumber() {
		return _customerCabinNumber;
	}
	
	
	// Gets customer's username
	public String getCustomerUserName() {
		return _customerUserName;
	}
	

}
