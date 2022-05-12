package application.model;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class WaitingListProperties {
	private SimpleStringProperty _waitingListID;
	private SimpleStringProperty _bookingID;
	private SimpleStringProperty _customerID;
	private SimpleIntegerProperty _position;
	private SimpleStringProperty _dateStarted;
	private SimpleIntegerProperty _noOfSeats;
	
	public WaitingListProperties(String waitingListID, String bookingID, String customerID, int position, String dateStarted,  int noOfSeats)
	{
		super();
		this._waitingListID = new SimpleStringProperty(waitingListID);
		this._bookingID = new SimpleStringProperty(bookingID);
		this._customerID = new SimpleStringProperty(customerID);
		this._position = new SimpleIntegerProperty(position);
		this._dateStarted = new SimpleStringProperty(dateStarted);
		this._noOfSeats = new SimpleIntegerProperty(noOfSeats);
	}

	public String getWaitingListID() {
		return _waitingListID.get();
	}

	public String getBookingID() {
		return _bookingID.get();
	}

	public String getCustomerID() {
		return _customerID.get();
	}

	public int getPosition() {
		return _position.get();
	}
	
	public String getDateStarted() {
		return _dateStarted.get();
	}

	public int getNoOfSeats() {
		return _noOfSeats.get();
	}
}
