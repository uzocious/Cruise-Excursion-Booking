package application.model;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class BookingsProperties {
	
	private SimpleStringProperty _bookingID;
	private SimpleStringProperty _excursionName;
	private SimpleStringProperty _excursionDate;
	private SimpleStringProperty _portID;
	private SimpleIntegerProperty _noOfSeatsAvailable;
	private SimpleStringProperty _coachID;
	
	public BookingsProperties(String bookingID, String excursionName, String excursionDate, String portID, int noOfSeatsAvailable, String coachID)
	{
		super();
		this._bookingID = new SimpleStringProperty(bookingID);
		this._excursionName = new SimpleStringProperty(excursionName);
		this._excursionDate = new SimpleStringProperty(excursionDate);
		this._portID = new SimpleStringProperty(portID);
		this._noOfSeatsAvailable = new SimpleIntegerProperty(noOfSeatsAvailable);
		this._coachID = new SimpleStringProperty(coachID);
	}

	public String getBookingID() {
		return _bookingID.get();
	}

	public String getExcursionName() {
		return _excursionName.get();
	}

	public String getExcursionDate() {
		return _excursionDate.get();
	}

	public String getPortID() {
		return _portID.get();
	}

	public int getNoOfSeatsAvailable() {
		return _noOfSeatsAvailable.get();
	}

	public String getCoachID() {
		return _coachID.get();
	}
	
}
