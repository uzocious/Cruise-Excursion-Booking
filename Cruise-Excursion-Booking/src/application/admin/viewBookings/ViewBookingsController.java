package application.admin.viewBookings;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import application.alert.AppAlert;
import application.model.Booking;
import application.model.BookingsProperties;
import javafx.fxml.Initializable;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import com.jfoenix.controls.*;


public class ViewBookingsController implements Initializable {
	
	// Java FX controls
	@FXML private JFXDatePicker dpkUpdateExcursionDate;
	@FXML private JFXComboBox<String> combUpdateCoach;
	
	@FXML private AnchorPane anchor_update;
	@FXML private AnchorPane anchor_viewBookings;
	
	@FXML private TableView<BookingsProperties> bookingsTable;
	@FXML private TableColumn<BookingsProperties, String> bookingID;
	@FXML private TableColumn<BookingsProperties, String> excursionName;
	@FXML private TableColumn<BookingsProperties, String> excursionDate;
	@FXML private TableColumn<BookingsProperties, String> portID;
	@FXML private TableColumn<BookingsProperties, Integer> noOfSeatsAvailable;
	@FXML private TableColumn<BookingsProperties, String> coachID;
	
	// Booking object
	Booking bk = new Booking();
	
	@Override
	public void initialize(URL url, ResourceBundle rb) 
	{
		initCol();
		
		// Displays all the list of bookings unto the table
		bk.getAllBookings();
		bookingsTable.setItems(bk._allBookings);
		
		// Lists the different coach names
		bk.getCoachs();
		combUpdateCoach.getItems().addAll(bk._coachNames);
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
	
	// loads update bookings anchor pane
	@FXML
	private void loadUpdateBookingsPane(ActionEvent event) {
		if(!bookingsTable.getSelectionModel().isEmpty()) {
			anchor_update.setVisible(true);
		}
		else {
			AppAlert.updateBoookingSelectionModelErrorAlert();
		}
	}
	
	
	// Updates bookings
	@FXML
	private void updateBookings(ActionEvent event) {
		
		boolean successful;
		
		String excursionDate = "";
		try {
			excursionDate = selectDate();
		}
		catch(Exception e) {
			excursionDate = "Date Empty";
		}
		
		String coachName = combUpdateCoach.getSelectionModel().getSelectedItem();
			
		String bookingID = bookingsTable.getSelectionModel().getSelectedItem().getBookingID();
		
		try {
			successful = bk.updateBookings(bookingID, excursionDate, coachName);
			
			if(successful == true) 
			{
				AppAlert.updateBookingConfirmationAlert();
				reloadViewBookings();
			}
			else
			{
				AppAlert.updateBoookingErrorAlert();
			}
		}
		catch(Exception e)
		{
			AppAlert.updateBoookingErrorAlert();
		}
	}
	
	
	// Deletes bookings
	@FXML
	private void deleteBookings(ActionEvent event) throws IOException 
	{
		if(!bookingsTable.getSelectionModel().isEmpty()) 
		{
			String result = AppAlert.deleteBookingConfirmationAlert();
			
			if(result.equals("Yes"))
			{
				boolean successful;
				String bookingID = bookingsTable.getSelectionModel().getSelectedItem().getBookingID();
				
				successful = bk.deleteBookings(bookingID);
				
				if(successful == true) 
				{
					AppAlert.deleteBookingConfirmationAlert2();
					reloadViewBookings();
				}
			}
		}
		else 
		{
			AppAlert.deleteBoookingSelectionModelErrorAlert();
		}
	}
	
	
	// Reloads view bookings page
	@FXML
	private void reloadViewBookings()throws IOException {
		AnchorPane pane = FXMLLoader.load(getClass().getResource("/application/admin/viewBookings/ViewBookings.fxml"));
		anchor_viewBookings.getChildren().setAll(pane);
	}
	
	
	// Gets the date from the java fx date picker
	@FXML
	public String selectDate() {
		String value = "";
		String excursionDate;
		
		LocalDate id = dpkUpdateExcursionDate.getValue();
		excursionDate = id.toString();
		
		if(!excursionDate.isEmpty()) {
			value = excursionDate;
		}
		
		return value;
	}
	

	
	
}
