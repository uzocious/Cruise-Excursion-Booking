package application.model;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import org.apache.commons.lang3.StringUtils;
import application.main.MainController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


public class Booking {
	
	// Local variables
	private String _adminID = MainController._adminID;
	private String _customerID = MainController._customerID;
	
	private String _adminName = null;
	private String _customerName = null;
	
	private String _excursionID = null;
	private String _portID = null;
	private String _coachID = null;
	
	// min and max numbers to generated number for booking id
	private final int min = 100000;
	private final int max = 999999;
	
	private int generatedNoForBookingID = 0;
	
	// min and max numbers to generated number for order id and waiting list id
	private final int longMin = 100000000;
	private final int longMax = 999999999;
	
	private int generatedNoForOrderID = 0;
	private int generatedNoForWaitingListID = 0;
	
	// Local Arrays
	public ArrayList<String> _excursionNames = new ArrayList<String>();
	public ArrayList<String> _coachNames = new ArrayList<String>();
	
	public ObservableList<BookingsProperties> _allBookings = FXCollections.observableArrayList();
	public ObservableList<OrderProperties> _allOrders = FXCollections.observableArrayList();
	public ObservableList<OrderProperties> _customerOrders = FXCollections.observableArrayList();
	public ObservableList<WaitingListProperties> _allWaitingList = FXCollections.observableArrayList();
	public ObservableList<WaitingListProperties> _customerWaitingList = FXCollections.observableArrayList();
	
	// Database object
	Database db = new Database();
	
	// Main model object
	Main_Model main_model = new Main_Model();
	
	
	// Constructor
	public Booking() {}
	
	
	// Adds to booking (administrator)
	public boolean addBookings(String excursionName, String excursionDate, int noOfSeats, String coachName) 
	{
		db.DBConnection();
		
		boolean confirmation = false;
		
		String bookingID;

		excursionName = StringUtils.capitalize(excursionName);
		coachName = StringUtils.capitalize(coachName);
		
		bookingID = newBookingID();
		
		if (!excursionName.equals(null) || !excursionDate.equals("Date Empty") || !coachName.equals(null))
		{
			if (noOfSeats >=  0 && noOfSeats <= 40)
			{
				if(getExcursionID(excursionName) == true) 
				{
					if(getPortID(excursionName) == true) 
					{
						if(getCoachID(coachName) == true) 
						{
							try {
								// Executes SQL query
								String query = String.format("INSERT INTO booking (Booking_ID, Excursion_ID, Excursion_Name, PORT_ID, Excursion_Date, Total_No_Seats, No_of_Seats_Available, Coach_ID)\r\n" + 
										"VALUES ('%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s');",
										bookingID, _excursionID, excursionName, _portID, excursionDate, noOfSeats, noOfSeats, _coachID);
								
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
			else {confirmation = false;};	
		}
		else {confirmation = false;}
		
		return confirmation;
	}
	
	
	// Updates booking (administrator)
	public boolean updateBookings(String bookingID, String excursionDate, String coachName) 
	{
		db.DBConnection();
		
		boolean confirmation = false;

		coachName = StringUtils.capitalize(coachName);
		
		try {
			
			if (!bookingID.equals(null) || !excursionDate.equals("Date Empty") || !coachName.equals(null)) 
			{
				if(getCoachID(coachName) == true)
				{
					// Executes SQL query
					String query = String.format("UPDATE booking\r\n" + 
							"SET booking.Excursion_Date = '%s', booking.Coach_ID = '%s'\r\n" + 
							"WHERE booking.Booking_ID = '%s'",excursionDate, _coachID, bookingID);
					
					db.statement.execute(query);
					confirmation = true;
				}
				else {confirmation = false;}
			}
			else {confirmation = false;}
			
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
	
	
	// Deletes booking (administrator)
	public boolean deleteBookings(String bookingID) {
		
		db.DBConnection();
		
		boolean confirmation = false;
		
		try {
			// Executes SQL query
			String query = String.format("DELETE FROM wait_list\r\n" + 
					"WHERE wait_list.Booking_ID = '%s';", bookingID);
			
			String query1 = String.format("DELETE FROM `order`\r\n" + 
					"WHERE `order`.Booking_ID = '%s';", bookingID);
			
			String query2 = String.format("DELETE FROM booking\r\n" + 
					"WHERE booking.Booking_ID = '%s';", bookingID);
			
			db.statement.execute(query);
			db.statement.execute(query1);
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
		
		return confirmation;
	}
	
	
	// Cancels customer's order (administrator)
	public boolean cancelOrder(String orderID, String bookingID, int noOfSeatsBooked)
	{
		db.DBConnection();
		
		boolean confirmation = false;
		
		if (updateBookingNoOfSeatsAvailableBeforeOrderIsCanceled(bookingID, noOfSeatsBooked) == true) 
		{
			try {
				// Executes SQL query
				String query = String.format("DELETE from `order`\r\n" + 
						"WHERE `order`.`Order_ID` = '%s';", orderID);
				
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
		
		return confirmation;
	}
	
	
	// Gets all the records in the booking table from the database (administrator)
	public void getAllBookings() {
		db.DBConnection();
		
		String nBookingID;
		String nExcursionName;
		String nExcursionDate;
		String nPortID;
		int nNoOfSeatsAvailable;
		String nCoachID;
		 
		try {
					
			// Executes SQL query
			String query = String.format("SELECT Booking_ID, Excursion_Name, Excursion_Date, PORT_ID, No_of_Seats_Available, Coach_ID\r\n" + 
					"FROM booking\r\n" + 
					"GROUP by (Booking_ID)");
					 
			db.resultSet = db.statement.executeQuery(query);
									 
			// gets the query result	 
			while(db.resultSet.next())
			{
				nBookingID = db.resultSet.getString("Booking_ID");
				nExcursionName = db.resultSet.getString("Excursion_Name");
				nExcursionDate = db.resultSet.getString("Excursion_Date");
				nPortID = db.resultSet.getString("PORT_ID");
				nNoOfSeatsAvailable = db.resultSet.getInt("No_of_Seats_Available");
				nCoachID = db.resultSet.getString("Coach_ID");
				
				_allBookings.add(new BookingsProperties(nBookingID, nExcursionName, nExcursionDate, nPortID, nNoOfSeatsAvailable, nCoachID));
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
	
	
	// Gets all the records in the order table from the database (administrator)
	public void getAllOrders() 
	{
		db.DBConnection();
		
		String nOrderID;
		String nOrderDate;
		String nCustomerID;
		String nBookingID;
		int nNoOfSeatsBooked;
		String nCabinNo;
		 
		try {
					
			// Executes SQL query
			String query = String.format("SELECT Order_ID, Order_Date, Customer_ID, Booking_ID, No_of_Seats_Booked, Cabin_No\r\n" + 
					"FROM `order`\r\n" + 
					"GROUP BY (Order_ID)");
					 
			db.resultSet = db.statement.executeQuery(query);
									 
			// gets the query result	 
			while(db.resultSet.next())
			{
				nOrderID = db.resultSet.getString("Order_ID");
				nOrderDate = db.resultSet.getString("Order_Date");
				nCustomerID = db.resultSet.getString("Customer_ID");
				nBookingID = db.resultSet.getString("Booking_ID");
				nNoOfSeatsBooked = db.resultSet.getInt("No_of_Seats_Booked");
				nCabinNo = db.resultSet.getString("Cabin_No");
				
				_allOrders.add(new OrderProperties(nOrderID, nOrderDate, nCustomerID, nBookingID, nNoOfSeatsBooked, nCabinNo));
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
	
	
	// Gets all the records in the wait_list table from the database (administrator)
	public void getAllWaitingList() 
	{
		db.DBConnection();
		
		String nWaitingListID;
		String nBookingID;
		String nCustomerID;
		int nPosition;
		String nDateStarted;
		int nNoOfSeats;
		 
		try {
					
			// Executes SQL query
			String query = String.format("SELECT Wait_List_ID, Booking_ID, Customer_ID, Position, Date_Started, No_of_Seats\r\n" + 
					"FROM wait_list\r\n" + 
					"GROUP BY (Wait_List_ID)");
					 
			db.resultSet = db.statement.executeQuery(query);
									 
			// gets the query result	 
			while(db.resultSet.next())
			{
				nWaitingListID = db.resultSet.getString("Wait_List_ID");
				nBookingID = db.resultSet.getString("Booking_ID");
				nCustomerID = db.resultSet.getString("Customer_ID");
				nPosition = db.resultSet.getInt("Position");
				nDateStarted = db.resultSet.getString("Date_Started");
				nNoOfSeats = db.resultSet.getInt("No_of_Seats");
				
				_allWaitingList.add(new WaitingListProperties(nWaitingListID, nBookingID, nCustomerID, nPosition, nDateStarted, nNoOfSeats));
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
	
	
	// Adds customer's waiting list to order i.e. to customer's booking (administrator)
	public boolean addWaitingListToOrder(String customerID, String bookingID, int noOfSeats, String cabinNo)
	{
		db.DBConnection();
		
		boolean confirmation = false;
		cabinNo = cabinNo.toUpperCase();
		
		String orderID = newOrderID();
		String orderDate = getCurrentDate();
		
		if (hasCustomerPlacedOrder(customerID, bookingID) != true)
		{
			if (main_model.isCabinNumberValid(cabinNo) == true)
			{
				try {
					// Executes SQL query
					String query = String.format("INSERT INTO `order` (`order`.`Order_ID`, `order`.`Order_Date`, `order`.`Customer_ID`, `order`.`Booking_ID`, `order`.`No_of_Seats_Booked`, `order`.`Cabin_No`)\r\n" + 
							"VALUES('%s','%s','%s','%s','%s','%s');", orderID, orderDate, customerID, bookingID, noOfSeats, cabinNo);
					
					db.statement.execute(query);
					
					confirmation = true;
					updateBookingNoOfSeatsAvailable(bookingID, noOfSeats);
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
		
		
		return confirmation;
	}
	
	
	// Clears all waiting list (administrator)
	public boolean clearAllWaitingList()
	{
		db.DBConnection();
		
		boolean confirmation = false;
		
		try {
			// Executes SQL query
			String query = String.format("DELETE FROM wait_list;");
			
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
		
		return confirmation;
	}
	
	
	// Gets the list of excursion names from the database (administrator)
	public void getExcursionNames() {
		db.DBConnection();
				 
		try {
					
			// Executes SQL query
			String query = String.format("SELECT excursion.Excursion_Name\r\n" + 
					"FROM excursion\r\n" + 
					"GROUP BY(excursion.Excursion_Name)\r\n");
					 
			db.resultSet = db.statement.executeQuery(query);
									 
			// gets the query result	 
			while(db.resultSet.next())
			{
				_excursionNames.add(db.resultSet.getString("excursion.Excursion_Name"));
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
	
	
	// Gets the list of coach names from the database (administrator)
	public void getCoachs() {
		db.DBConnection();
		 
		try {		
			// Executes SQL query
			String query = String.format("SELECT coach.Coach_Name FROM coach WHERE coach.Status = 'Active'");
					 
			db.resultSet = db.statement.executeQuery(query);
									 
			// gets the query result	 
			while(db.resultSet.next())
			{
				_coachNames.add(db.resultSet.getString("coach.Coach_Name"));
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

	
	// Gets administrator's full name (administrator)
	public String getAdminName() {
		db.DBConnection();
		 
		try {		
			// Executes SQL query
			String query = String.format("SELECT administrator.First_Name, administrator.Last_Name \r\n" + 
					"from administrator\r\n" + 
					"WHERE administrator.Admin_ID = '%s'", _adminID);
					 
			db.resultSet = db.statement.executeQuery(query);
									 
			// gets the query result	 
			if(db.resultSet.next())
			{
				String fName = db.resultSet.getString("administrator.First_Name");
				String lName = db.resultSet.getString("administrator.Last_Name");
				_adminName = String.format("%s %s", fName, lName);
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
		
		
		return _adminName;
	}
	
	
	// Gets customer's cabinNo (administrator)
	public String getCustomerCabinNo(String customerID)
	{
		db.DBConnection();
		
		String result = "";
		 
		try {		
			// Executes SQL query
			String query = String.format("SELECT customer.Cabin_No\r\n" + 
					"FROM customer\r\n" + 
					"WHERE customer.Customer_ID = '%s'", customerID);
					 
			db.resultSet = db.statement.executeQuery(query);
									 
			// gets the query result	 
			if(db.resultSet.next())
			{
				result = db.resultSet.getString("customer.Cabin_No");
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
	
	
	
	
	
	
	// Removes customer from waiting list (administrator and customer)
	public boolean  removeCustomerFromWaitingList(String waitingListID) 
	{
		db.DBConnection();
		
		boolean confirmation = false;
		
		try {
			// Executes SQL query
			String query = String.format("DELETE FROM wait_list\r\n" + 
					"WHERE wait_list.Wait_List_ID = '%s';", waitingListID);
			
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
		
		return confirmation;
	}
	
	
	
	
	
	// Adds to order (customer)
	public boolean addOrder(String bookingID, int noOfSeats, String cabinNo) 
	{
		db.DBConnection();
		
		boolean confirmation = false;
		cabinNo = cabinNo.toUpperCase();
		
		String orderID = newOrderID();
		String orderDate = getCurrentDate();
		
		if (hasCustomerPlacedOrder(_customerID, bookingID) != true) 
		{
			if (main_model.isCabinNumberValid(cabinNo) == true)
			{
				try {
					// Executes SQL query
					String query = String.format("INSERT INTO `order` (`order`.`Order_ID`, `order`.`Order_Date`, `order`.`Customer_ID`, `order`.`Booking_ID`, `order`.`No_of_Seats_Booked`, `order`.`Cabin_No`)\r\n" + 
							"VALUES('%s','%s','%s','%s','%s','%s');", orderID, orderDate, _customerID, bookingID, noOfSeats, cabinNo);
					
					db.statement.execute(query);
					
					confirmation = true;
					updateCabinNumber(_customerID, cabinNo);
					updateBookingNoOfSeatsAvailable(bookingID, noOfSeats);
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
		
		
		return confirmation;
	}
	
	
	// Updates order (customer)
	public boolean updateOrder(String orderID, int noOfSeats, String cabinNo) 
	{
		db.DBConnection();
		
		boolean confirmation = false;
		cabinNo = cabinNo.toUpperCase();
		
		if (main_model.isCabinNumberValid(cabinNo) == true)
		{
			try {
				// Executes SQL query
				String query = String.format("UPDATE `order`\r\n" + 
						"SET `order`.`No_of_Seats_Booked` = '%s', `order`.`Cabin_No` = '%s'\r\n" + 
						"WHERE `order`.`Order_ID` = '%s'",noOfSeats, cabinNo, orderID);
				
				db.statement.execute(query);
				confirmation = true;
				updateCabinNumber(_customerID, cabinNo);
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
		
		return confirmation;
	}
	
	
	// Cancels customer's order (customer)
	public boolean cancelCustomerOrder(String orderID, String bookingID, int noOfSeatsBooked)
	{
		db.DBConnection();
		
		boolean confirmation = false;
		
		if (updateBookingNoOfSeatsAvailableBeforeOrderIsCanceled(bookingID, noOfSeatsBooked) == true) 
		{
			try {
				// Executes SQL query
				String query = String.format("DELETE from `order`\r\n" + 
						"WHERE `order`.`Order_ID` = '%s';", orderID);
				
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
		
		return confirmation;
	}
	
	
	// Gets list of customer's order (customer)
	public void getCustomerOrder() 
	{
		db.DBConnection();
		
		String nOrderID;
		String nOrderDate;
		String nCustomerID;
		String nBookingID;
		int nNoOfSeatsBooked;
		String nCabinNo;
		 
		try {
					
			// Executes SQL query
			String query = String.format("SELECT Order_ID, Order_Date, Customer_ID, Booking_ID, No_of_Seats_Booked, Cabin_No\r\n" + 
					"FROM `order`\r\n" + 
					"WHERE Customer_ID = '%s'\r\n" + 
					"GROUP BY (Order_ID)", _customerID);
					 
			db.resultSet = db.statement.executeQuery(query);
									 
			// gets the query result	 
			while(db.resultSet.next())
			{
				nOrderID = db.resultSet.getString("Order_ID");
				nOrderDate = db.resultSet.getString("Order_Date");
				nCustomerID = db.resultSet.getString("Customer_ID");
				nBookingID = db.resultSet.getString("Booking_ID");
				nNoOfSeatsBooked = db.resultSet.getInt("No_of_Seats_Booked");
				nCabinNo = db.resultSet.getString("Cabin_No");
				
				_customerOrders.add(new OrderProperties(nOrderID, nOrderDate, nCustomerID, nBookingID, nNoOfSeatsBooked, nCabinNo));
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
	
	
	// Gets a particular booking's number of seat available (customer)
	public int getBookingsNoOfSeatsAvailable(String bookingID)
	{
		db.DBConnection();
		
		int result = 0;
		
		try {		
			// Executes SQL query
			String query = String.format("SELECT booking.No_of_Seats_Available\r\n" + 
					"FROM booking\r\n" + 
					"WHERE booking.Booking_ID = '%s'", bookingID);
					 
			db.resultSet = db.statement.executeQuery(query);
									 
			// gets the query result	 
			if(db.resultSet.next())
			{
				result = db.resultSet.getInt("booking.No_of_Seats_Available");
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
	
	
	// Updates booking number of seats available when customer is updating order - addition (customer)
	public void updateBookingNoOfSeatsAvailableWhenUpdatingOrderAddition(String bookingID, int differenceInNo) 
	{
		db.DBConnection();
		
		int numberOfSeatsAvailable;
		
		try {
			
			// Executes SQL query
			String query = String.format("SELECT booking.No_of_Seats_Available\r\n" + 
					"FROM booking\r\n" + 
					"WHERE booking.Booking_ID = '%s'", bookingID);
					 
			db.resultSet = db.statement.executeQuery(query);

			// gets the query result		 
			if(db.resultSet.next())
			{
				numberOfSeatsAvailable = db.resultSet.getInt("booking.No_of_Seats_Available");
				
				numberOfSeatsAvailable = numberOfSeatsAvailable + differenceInNo;
				
				// Executes SQL query
				String query1 = String.format("UPDATE booking\r\n" + 
						"SET booking.No_of_Seats_Available = %s\r\n" + 
						"WHERE booking.Booking_ID = '%s'",numberOfSeatsAvailable, bookingID);
				
				db.statement.execute(query1);
				
			}
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
		
	}
	
	
	// Updates booking number of seats available when customer is updating order - subtraction (customer)
	public void updateBookingNoOfSeatsAvailableWhenUpdatingOrderSubtraction(String bookingID, int differenceInNo) 
	{
		db.DBConnection();
		
		int numberOfSeatsAvailable;
		
		try {
			
			// Executes SQL query
			String query = String.format("SELECT booking.No_of_Seats_Available\r\n" + 
					"FROM booking\r\n" + 
					"WHERE booking.Booking_ID = '%s'", bookingID);
					 
			db.resultSet = db.statement.executeQuery(query);

			// gets the query result		 
			if(db.resultSet.next())
			{
				numberOfSeatsAvailable = db.resultSet.getInt("booking.No_of_Seats_Available");
				
				numberOfSeatsAvailable = numberOfSeatsAvailable - differenceInNo;
				
				// Executes SQL query
				String query1 = String.format("UPDATE booking\r\n" + 
						"SET booking.No_of_Seats_Available = %s\r\n" + 
						"WHERE booking.Booking_ID = '%s'",numberOfSeatsAvailable, bookingID);
				
				db.statement.execute(query1);
				
			}
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
		
	}
	
	
	// Adds customer to waiting list (customer)
	public boolean addToWaitingList(String bookingID, int noOfSeats, String cabinNo) 
	{
		db.DBConnection();
		
		boolean confirmation = false;
		
		cabinNo = cabinNo.toUpperCase();
		
		String waitingList = newWaitingListID();
		String dateStarted = getCurrentDate();
		
		int position = Position(bookingID);
		
		if (main_model.isCabinNumberValid(cabinNo) == true)
		{
			try {
				// Executes SQL query
				String query = String.format("INSERT INTO wait_list (Wait_List_ID, Customer_ID, Booking_ID, Position, Date_Started, No_of_Seats)\r\n" + 
						"VALUES ('%s', '%s', '%s', '%s', '%s', '%s');", waitingList, _customerID, bookingID, position, dateStarted, noOfSeats);
				
				db.statement.execute(query);
				
				confirmation = true;
				updateCabinNumber(_customerID, cabinNo);
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
		
		
		return confirmation;
	}
	
	
	// Gets customer's waiting list (customer)
	public void getCustomerWaitingList() 
	{
		db.DBConnection();
		
		String nWaitingListID;
		String nBookingID;
		String nCustomerID;
		int nPosition;
		String nDateStarted;
		int nNoOfSeats;
		 
		try {
					
			// Executes SQL query
			String query = String.format("SELECT Wait_List_ID, Booking_ID, Customer_ID, Position, Date_Started, No_of_Seats\r\n" + 
					"FROM wait_list\r\n" + 
					"WHERE Customer_ID = '%s'\r\n" +
					"GROUP BY (Wait_List_ID)", _customerID);
					 
			db.resultSet = db.statement.executeQuery(query);
									 
			// gets the query result	 
			while(db.resultSet.next())
			{
				nWaitingListID = db.resultSet.getString("Wait_List_ID");
				nBookingID = db.resultSet.getString("Booking_ID");
				nCustomerID = db.resultSet.getString("Customer_ID");
				nPosition = db.resultSet.getInt("Position");
				nDateStarted = db.resultSet.getString("Date_Started");
				nNoOfSeats = db.resultSet.getInt("No_of_Seats");
				
				_customerWaitingList.add(new WaitingListProperties(nWaitingListID, nBookingID, nCustomerID, nPosition, nDateStarted, nNoOfSeats));
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
	
	
	// Gets customer's full name (customer)
	public String getCustomerName() 
	{
		db.DBConnection();
		 
		try {		
			// Executes SQL query
			String query = String.format("SELECT customer.First_Name, customer.Last_Name\r\n" + 
					"FROM customer\r\n" + 
					"WHERE customer.Customer_ID = '%s'", _customerID);
					 
			db.resultSet = db.statement.executeQuery(query);
									 
			// gets the query result	 
			if(db.resultSet.next())
			{
				String fName = db.resultSet.getString("customer.First_Name");
				String lName = db.resultSet.getString("customer.Last_Name");
				_customerName = String.format("%s %s", fName, lName);
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
		
		return _customerName;
	}
	
	
	// Checks if customer is already in waiting list (customer)
	public boolean isCustomerInWaitingList(String bookingID) 
	{
		db.DBConnection();
		
		boolean result = false;
		
		try {
			// Executes SQL query for customer
			String query = String.format("SELECT wait_list.Customer_ID, wait_list.Booking_ID \r\n" + 
					"FROM wait_list\r\n" + 
					"WHERE wait_list.Customer_ID = '%s' AND wait_list.Booking_ID = '%s'",
					_customerID, bookingID);
					 
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
	
	
	// Gets waiting list ID 
	public String getWaitingListID(String bookingID)
	{
		db.DBConnection();
		
		String result = "";
		
		try {
			// Executes SQL query for customer
			String query = String.format("SELECT wait_list.Wait_List_ID \r\n" + 
					"FROM wait_list\r\n" + 
					"WHERE wait_list.Customer_ID = '%s' AND wait_list.Booking_ID = '%s'",
					_customerID, bookingID);
					 
			db.resultSet = db.statement.executeQuery(query);

			// gets the query result from the table customer		 
			if(db.resultSet.next())
			{
				result = db.resultSet.getString("wait_list.Wait_List_ID");
			}
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
		
		return result;
	}
	
	
	// Checks if customer has placed order for a particular bookingID (customer)
	public boolean hasCustomerPlacedOrder(String customerID, String bookingID) 
	{
		db.DBConnection();
		
		boolean result = false;
		
		try {
			// Executes SQL query
			String query = String.format("SELECT `order`.`Customer_ID`, `order`.`Booking_ID`  \r\n" + 
					"FROM `order`\r\n" + 
					"WHERE `order`.`Customer_ID` = '%s' AND `order`.`Booking_ID` = '%s'",
					customerID, bookingID);
					 
			db.resultSet = db.statement.executeQuery(query);

			// gets the query result		 
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
		
	
	
	
	
	//----- Private Methods -----\\
	
	// Updates booking number of seats available before order is cancelled (administrator and customer)
	private boolean updateBookingNoOfSeatsAvailableBeforeOrderIsCanceled(String bookingID, int noOfSeatsBooked) 
	{
		db.DBConnection();
		
		boolean confirmation = false;
		
		int numberOfSeatsAvailable;
		
		try {
			
			// Executes SQL query
			String query = String.format("SELECT booking.No_of_Seats_Available\r\n" + 
					"FROM booking\r\n" + 
					"WHERE booking.Booking_ID = '%s'", bookingID);
					 
			db.resultSet = db.statement.executeQuery(query);

			// gets the query result		 
			if(db.resultSet.next())
			{
				numberOfSeatsAvailable = db.resultSet.getInt("booking.No_of_Seats_Available");
				
				numberOfSeatsAvailable = numberOfSeatsAvailable + noOfSeatsBooked;
				
				// Executes SQL query
				String query1 = String.format("UPDATE booking\r\n" + 
						"SET booking.No_of_Seats_Available = %s\r\n" + 
						"WHERE booking.Booking_ID = '%s'",numberOfSeatsAvailable, bookingID);
				
				db.statement.execute(query1);
				
				confirmation = true;
			}
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
		
		return confirmation;
	}
	
	
	// Creates new booking ID (administrator)
	private String newBookingID()
	{
		String result;
		
		boolean itr = generateNumberForBookingID();
		
		while(itr != true) {
			generateNumberForBookingID();
		}
	
		result = String.format("BK%s", generatedNoForBookingID);
		return result;
	}
	
	
	// Generates number for booking id (administrator)
	private boolean generateNumberForBookingID()
	{
		db.DBConnection();
		
		boolean result = false;
		
		int randomNumber = 0;
		Random random = new Random();
		randomNumber = random.nextInt(max - min) + min;
		
		try {
			// Executes SQL query
			String query = String.format("SELECT booking.Booking_ID \r\n" + 
					"FROM booking\r\n" + 
					"WHERE booking.Booking_ID = 'BK%s'", randomNumber);
					 
			db.resultSet = db.statement.executeQuery(query);
	
			// gets the query result		 
			if(db.resultSet.next())
			{
				result = false;
			}
			else
			{
				generatedNoForBookingID = randomNumber;
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
	
	
	// Gets a specific excursion ID from the database using excursion name (administrator)
	private boolean getExcursionID(String excursionName) 
	{
		db.DBConnection();
		
		boolean result = false;
		
		try {
			// Executes SQL query
			String query = String.format("SELECT excursion.Excursion_ID\r\n" + 
					"FROM excursion\r\n" + 
					"WHERE excursion.Excursion_Name = '%s'\r\n" + 
					"GROUP BY(excursion.Excursion_Name)", excursionName);
					 
			db.resultSet = db.statement.executeQuery(query);

			// gets the query result		 
			if(db.resultSet.next())
			{
				_excursionID = db.resultSet.getString("excursion.Excursion_ID");
				result = true;
			}
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
		
		return result;
	}
	
	
	// Gets a specific port ID from the database using excursion name (administrator)
	private boolean getPortID(String excursionName) 
	{
		db.DBConnection();
		
		boolean result = false;
		
		try {
			// Executes SQL query
			String query = String.format("SELECT excursion.PORT_ID\r\n" + 
					"FROM excursion\r\n" + 
					"WHERE excursion.Excursion_Name = '%s'\r\n" + 
					"GROUP BY(excursion.Excursion_Name)", excursionName);
					 
			db.resultSet = db.statement.executeQuery(query);

			// gets the query result		 
			if(db.resultSet.next())
			{
				_portID = db.resultSet.getString("excursion.PORT_ID");
				result = true;
			}
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
		
		return result;
	}
	
	
	// Gets a specific coach ID from the database using coach name (administrator)
	private boolean getCoachID(String coachName) 
	{
		db.DBConnection();
		
		boolean result = false;
		
		try {
			// Executes SQL query
			String query = String.format("SELECT coach.Coach_ID\r\n" + 
					"FROM coach\r\n" + 
					"WHERE coach.Coach_Name = '%s'", coachName);
					 
			db.resultSet = db.statement.executeQuery(query);

			// gets the query result		 
			if(db.resultSet.next())
			{
				_coachID = db.resultSet.getString("coach.Coach_ID");
				result = true;
			}
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
		
		return result;
	}
	
	
	// Creates new order ID (customer)
	private String newOrderID()
	{
		String result;
		
		boolean itr = generateNumberForOrderID();
		
		while(itr != true) {
			generateNumberForOrderID();
		}
	
		result = String.format("#%s", generatedNoForOrderID);
		return result;
	}
	
	
	// Generates number for order ID (customer)
	private boolean generateNumberForOrderID()
	{
		db.DBConnection();
		
		boolean result = false;
		
		int randomNumber = 0;
		Random random = new Random();
		randomNumber = random.nextInt(longMax - longMin) + longMin;
		
		try {
			// Executes SQL query
			String query = String.format("SELECT `order`.`Order_ID`\r\n" + 
					"FROM `order` \r\n" + 
					"WHERE `order`.`Order_ID` = '#%s'", randomNumber);
					 
			db.resultSet = db.statement.executeQuery(query);
	
			// gets the query result		 
			if(db.resultSet.next())
			{
				result = false;
			}
			else
			{
				generatedNoForOrderID = randomNumber;
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

	
	// Creates new waiting list ID (customer)
	private String newWaitingListID()
	{
		String result;
		
		boolean itr = generateNumberForWaitingListID();
		
		while(itr != true) {
			generateNumberForWaitingListID();
		}
	
		result = String.format("W%s", generatedNoForWaitingListID);
		return result;
	}
	
	
	// Generates number for waiting list ID (customer)
	private boolean generateNumberForWaitingListID()
	{
		db.DBConnection();
		
		boolean result = false;
		
		int randomNumber = 0;
		Random random = new Random();
		randomNumber = random.nextInt(longMax - longMin) + longMin;
		
		try {
			// Executes SQL query
			String query = String.format("SELECT wait_list.Wait_List_ID\r\n" + 
					"FROM wait_list\r\n" + 
					"WHERE wait_list.Wait_List_ID = 'W%s'", randomNumber);
					 
			db.resultSet = db.statement.executeQuery(query);
	
			// gets the query result		 
			if(db.resultSet.next())
			{
				result = false;
			}
			else
			{
				generatedNoForWaitingListID = randomNumber;
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
	
	
	// Gets current date (customer)
	private String getCurrentDate() 
	{
		String result = "";
		
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		result = sdf.format(date);
		
		return result;
	}
	
	
	// Updates customer's cabin number (customer)
	private void updateCabinNumber(String customerID, String newCabinNo) 
	{
		db.DBConnection();
		
		newCabinNo = newCabinNo.toUpperCase();
		
		try {
			
			// Executes SQL query
			String query = String.format("UPDATE customer\r\n" + 
					"SET customer.Cabin_No =  '%s'\r\n" + 
					"WHERE customer.Customer_ID = '%s'",newCabinNo, customerID);
			
			db.statement.execute(query);
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
	}
	
	
	// Updates booking number of seats available when ordered (customer)
	private void updateBookingNoOfSeatsAvailable(String bookingID, int noOfSeats) 
	{
		db.DBConnection();
		
		int numberOfSeatsAvailable;
		
		try {
			
			// Executes SQL query
			String query = String.format("SELECT booking.No_of_Seats_Available\r\n" + 
					"FROM booking\r\n" + 
					"WHERE booking.Booking_ID = '%s'", bookingID);
					 
			db.resultSet = db.statement.executeQuery(query);

			// gets the query result		 
			if(db.resultSet.next())
			{
				numberOfSeatsAvailable = db.resultSet.getInt("booking.No_of_Seats_Available");
				
				numberOfSeatsAvailable = numberOfSeatsAvailable - noOfSeats;
				
				// Executes SQL query
				String query1 = String.format("UPDATE booking\r\n" + 
						"SET booking.No_of_Seats_Available = %s\r\n" + 
						"WHERE booking.Booking_ID = '%s'",numberOfSeatsAvailable, bookingID);
				
				db.statement.execute(query1);
			}
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
	}
	
	
	// Gives position to customers in waiting list (customer)
	private int Position(String bookingID)
	{
		db.DBConnection();
		
		int result = 0;
		 
		try {
			// Executes SQL query
			String query = String.format("SELECT MAX(wait_list.Position) as Position\r\n" + 
					"FROM wait_list\r\n" + 
					"WHERE wait_list.Booking_ID = '%s'", bookingID);
					 
			db.resultSet = db.statement.executeQuery(query);
									 
			// gets the query result	 
			if(db.resultSet.next())
			{
				result = db.resultSet.getInt("Position");
				result = result + 1;
			}
			else 
			{
				result = 1;
			}
		}
		catch(Exception e) 
		{
			System.out.println(e);
		}
		
		return result;
	}
	

}
