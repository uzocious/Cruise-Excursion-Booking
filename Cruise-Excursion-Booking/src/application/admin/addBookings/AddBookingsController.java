package application.admin.addBookings;

import application.alert.AppAlert;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;

import com.jfoenix.controls.*;

import application.model.Booking;


public class AddBookingsController implements Initializable {
	
	
	// Java FX controls
	@FXML private JFXListView<String> lstExcursionNames;
	@FXML private JFXComboBox<String> combCoachs;
	@FXML private JFXDatePicker dtpExcursionDate;
	@FXML private JFXSlider sldNumberOfSeats;
	@FXML private JFXTextField txtSearchExName;
	
	@FXML private AnchorPane anchor_addBookings;
	
	// Booking object
	Booking bk = new Booking();
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {

		// Lists the different excursion names
		bk.getExcursionNames();
		lstExcursionNames.getItems().addAll(bk._excursionNames); 
		
		// Lists the different coach names
		bk.getCoachs();
		combCoachs.getItems().addAll(bk._coachNames);
		
		// Sets list's selection mode to single
		lstExcursionNames.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		
	}
	
	// Adds to booking
	@FXML
	public void addToBookings(ActionEvent event) 
	{
		boolean successful;
		
		String excursionDate = "";
		try {
			excursionDate = selectDate();
		}
		catch(Exception e) {
			excursionDate = "Date Empty";
		}
		
		String coachName = combCoachs.getSelectionModel().getSelectedItem();
		
		String excursionName = lstExcursionNames.getSelectionModel().getSelectedItem();
		
		Double slNo= sldNumberOfSeats.getValue();
		int noOfSeats = slNo.intValue();		
		
		try {
			successful = bk.addBookings(excursionName, excursionDate, noOfSeats, coachName);
			
			if(successful == true) 
			{
				AppAlert.addBookingConfirmationAlert();
				reloadAddBookings();
			}
			else
			{
				AppAlert.addBoookingErrorAlert();
			}
		}
		catch(Exception e)
		{
			AppAlert.addBoookingErrorAlert();
		}
		
	}
	
	
	// Displays the search results unto the list view
	@FXML
	public void searchExcursion(ActionEvent event) {
		String searchedText = txtSearchExName.getText();
		searchedText = searchedText.trim();
		searchedText = searchedText.toUpperCase();
		
		if(bk._excursionNames.contains(searchedText)) {
			lstExcursionNames.getItems().clear();
			lstExcursionNames.getItems().add(searchedText);
		}
		else {
			lstExcursionNames.getItems().clear();
			lstExcursionNames.getItems().addAll(bk._excursionNames);
			AppAlert.searchErrorAlert();
		}
		
	}
	
	
	// Gets the date from the jfx date picker
	@FXML
	public String selectDate() {
		String value = "";
		String excursionDate;
		
		LocalDate id = dtpExcursionDate.getValue();
		excursionDate = id.toString();
		
		if(!excursionDate.isEmpty()) {
			value = excursionDate;
		}
		
		return value;
	}
	
	
	// reload add bookings page
	private void reloadAddBookings()throws IOException{
		AnchorPane pane = FXMLLoader.load(getClass().getResource("/application/admin/addBookings/AddBookings.fxml"));
		anchor_addBookings.getChildren().setAll(pane);
	}
}
