package application.model;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class OrderProperties {
	private SimpleStringProperty _orderID;
	private SimpleStringProperty _orderDate;
	private SimpleStringProperty _customerID;
	private SimpleStringProperty _bookingID;
	private SimpleIntegerProperty _noOfSeatsBooked;
	private SimpleStringProperty _cabinNo;
	
	public OrderProperties(String orderID, String orderDate, String customerID, String bookingID, int noOfSeatsBooked, String cabinNo)
	{
		super();
		this._orderID = new SimpleStringProperty(orderID);
		this._orderDate = new SimpleStringProperty(orderDate);
		this._customerID = new SimpleStringProperty(customerID);
		this._bookingID = new SimpleStringProperty(bookingID);
		this._noOfSeatsBooked = new SimpleIntegerProperty(noOfSeatsBooked);
		this._cabinNo = new SimpleStringProperty(cabinNo);
	}

	public String getOrderID() {
		return _orderID.get();
	}

	public String getOrderDate() {
		return _orderDate.get();
	}

	public String getCustomerID() {
		return _customerID.get();
	}

	public String getBookingID() {
		return _bookingID.get();
	}

	public int getNoOfSeatsBooked() {
		return _noOfSeatsBooked.get();
	}

	public String getCabinNo() {
		return _cabinNo.get();
	}

	
}
