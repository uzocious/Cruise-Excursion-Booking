package application.home.start;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import application.alert.AppAlert;
import application.model.Booking;
import application.model.BookingsProperties;
import application.model.Main_Model;
import application.model.Profile;
import javafx.fxml.Initializable;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import com.jfoenix.controls.*;


public class StartController implements Initializable {
	
	// Java FX controls
	@FXML private JFXSlider sldNumberOfSeats;
	@FXML private JFXTextField txtCabinNo;
	
	@FXML private AnchorPane anchor_start;
	
	@FXML private TableView<BookingsProperties> bookingsTable;
	@FXML private TableColumn<BookingsProperties, String> bookingID;
	@FXML private TableColumn<BookingsProperties, String> excursionName;
	@FXML private TableColumn<BookingsProperties, String> excursionDate;
	@FXML private TableColumn<BookingsProperties, String> portID;
	@FXML private TableColumn<BookingsProperties, Integer> noOfSeatsAvailable;
	@FXML private TableColumn<BookingsProperties, String> coachID;
	
	// Text field CSS Styles for validation
	String wrong_certificate = "-jfx-unfocus-color: red; -jfx-focus-color: red;";
	String correct_certificate = "-jfx-unfocus-color: #4d4d4d; -jfx-focus-color: #2A2E37;";
	
	// Booking object
	Booking bk = new Booking();
	
	// Profile object
	Profile profile = new Profile();
	
	@Override
	public void initialize(URL url, ResourceBundle rb) 
	{
		initCol();
		
		// Displays all the list of bookings unto the table
		bk.getAllBookings();
		bookingsTable.setItems(bk._allBookings);
		
		// Sets cabin number
		profile.getCustomerDetails();
		String cabinNo = profile.getCustomerCabinNumber();
		txtCabinNo.setText(cabinNo);
	}
	
	// Table column insider
	private void initCol() {
		bookingID.setCellValueFactory(new PropertyValueFactory<BookingsProperties, String>("bookingID"));
		excursionName.setCellValueFactory(new PropertyValueFactory<BookingsProperties, String>("excursionName"));
		excursionDate.setCellValueFactory(new PropertyValueFactory<BookingsProperties, String>("excursionDate"));
		portID.setCellValueFactory(new PropertyValueFactory<BookingsProperties, String>("portID"));
		noOfSeatsAvailable.setCellValueFactory(new PropertyValueFactory<BookingsProperties, Integer>("noOfSeatsAvailable"));
		coachID.setCellValueFactory(new PropertyValueFactory<BookingsProperties, String>("coachID"));
	}
	
	
	// Books the selected excursion
	@FXML
	private void bookNow(ActionEvent event) throws IOException 
	{
		boolean successful;
		
		if(!bookingsTable.getSelectionModel().isEmpty()) 
		{
			Double slNo= sldNumberOfSeats.getValue();
			int noOfSeats = slNo.intValue();
			
			int noOfSeatsAvailable = bookingsTable.getSelectionModel().getSelectedItem().getNoOfSeatsAvailable();
				
			String cabinNo = txtCabinNo.getText();
			cabinNo = cabinNo.trim();
			
			// Cabin number validation
			Main_Model main_model = new Main_Model();
			Tooltip cabinNoTooltip = new Tooltip();
			
			// If the cabin number text field is empty or If the cabin number gotten is not valid
			if (cabinNo.isEmpty() || main_model.isCabinNumberValid(cabinNo) != true){
				txtCabinNo.setStyle(wrong_certificate);
				cabinNoTooltip.setText("Please enter a valid cabin number.");
				txtCabinNo.setTooltip(cabinNoTooltip);
				return;
			}
			else{
				txtCabinNo.setStyle(correct_certificate);
				cabinNoTooltip.setText(null);
				txtCabinNo.setTooltip(cabinNoTooltip);
				}
			
			
			String bookingID = bookingsTable.getSelectionModel().getSelectedItem().getBookingID();
			
			// If customer's number of seats is less or equal to number of seats available
			if(noOfSeats <= noOfSeatsAvailable)
			{
				successful = bk.addOrder(bookingID, noOfSeats, cabinNo);
				
				// If successful
				if(successful == true) 
				{
					// Checking if customer is in the waiting list
					if (bk.isCustomerInWaitingList(bookingID) == true) 
					{
						String waitingListID = bk.getWaitingListID(bookingID);
						bk.removeCustomerFromWaitingList(waitingListID);
					}
					
					AppAlert.bookNowConfirmationAlert();
					reloadStart();
				}
				else
				{
					AppAlert.bookNowErrorAlert();
				}
			}
			// If customer's number of seats is greater then the number of seats available
			else if (noOfSeats > noOfSeatsAvailable)
			{
				String customerID = profile.getCustomerID();
				
				// If customer has already placed order
				if (bk.hasCustomerPlacedOrder(customerID, bookingID) != true)
				{
					String result = AppAlert.joinWaitingListConfirmationAlert();
					
					if (result.equals("Yes"))
					{
						// If customer is already in the waiting list
						if (bk.isCustomerInWaitingList(bookingID) != true)
						{
							boolean success;
							
							success = bk.addToWaitingList(bookingID, noOfSeats, cabinNo);
							
							// If successful
							if(success == true)
							{
								AppAlert.joinWaitingListConfirmationAlert2();
								reloadStart();
							}
						}
						else 
						{
							AppAlert.customerAlreadyInWaitingListWarningAlert();
							return;
						}
					}
					else 
					{
						return;
					}
				}
				else
				{
					AppAlert.bookNowErrorAlert();
				}
			}
		}
		else 
		{
			AppAlert.bookNowgSelectionModelErrorAlert();
		}
	}
	
	
	// Reloads start page
	@FXML
	private void reloadStart()throws IOException {
		AnchorPane pane = FXMLLoader.load(getClass().getResource("/application/home/start/Start.fxml"));
		anchor_start.getChildren().setAll(pane);
	}
	
	
}
