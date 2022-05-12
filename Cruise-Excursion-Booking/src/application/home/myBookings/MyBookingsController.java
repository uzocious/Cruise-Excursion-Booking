package application.home.myBookings;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import com.jfoenix.controls.*;
import application.alert.AppAlert;
import application.model.Booking;
import application.model.Main_Model;
import application.model.OrderProperties;
import application.model.Profile;


public class MyBookingsController implements Initializable {
	
	// Java FX controls
	@FXML private AnchorPane anchor_myBookings;
	@FXML private AnchorPane anchor_updateMyBookings;
	
	@FXML private JFXSlider sldUpdateNumberOfSeats;
	@FXML private JFXTextField txtUpdateCabinNo;
	
	@FXML private TableView<OrderProperties> ordersTable;
	@FXML private TableColumn<OrderProperties, String> orderID;
	@FXML private TableColumn<OrderProperties, String> orderDate;
	@FXML private TableColumn<OrderProperties, String> customerID;
	@FXML private TableColumn<OrderProperties, String> bookingID;
	@FXML private TableColumn<OrderProperties, Integer> noOfSeatsBooked;
	@FXML private TableColumn<OrderProperties, String> cabinNo;
	
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
		
		bk.getCustomerOrder();
		ordersTable.setItems(bk._customerOrders);
		
		// Sets cabin number
		profile.getCustomerDetails();
		String cabinNo = profile.getCustomerCabinNumber();
		txtUpdateCabinNo.setText(cabinNo);
	}
	
	// Table column insider
	private void initCol() 
	{
		orderID.setCellValueFactory(new PropertyValueFactory<OrderProperties, String>("orderID"));
		orderDate.setCellValueFactory(new PropertyValueFactory<OrderProperties, String>("orderDate"));
		customerID.setCellValueFactory(new PropertyValueFactory<OrderProperties, String>("customerID"));
		bookingID.setCellValueFactory(new PropertyValueFactory<OrderProperties, String>("bookingID"));
		noOfSeatsBooked.setCellValueFactory(new PropertyValueFactory<OrderProperties, Integer>("noOfSeatsBooked"));
		cabinNo.setCellValueFactory(new PropertyValueFactory<OrderProperties, String>("cabinNo"));
	}
	
	
	// loads update my booking anchor pane (windows)
	@FXML
	private void loadUpdateMyBookingPane(ActionEvent event) 
	{
		if(!ordersTable.getSelectionModel().isEmpty()) {
			anchor_updateMyBookings.setVisible(true);
		}
		else {
			AppAlert.updateBoookingSelectionModelErrorAlert();
		}
	}
	
	
	// Updates my booking/order
	@FXML
	private void updateMyOrder(ActionEvent event) throws IOException {
		
		boolean successful;
		int differenceInNo;
		
		// Selected Items
		String tOrderID = ordersTable.getSelectionModel().getSelectedItem().getOrderID();
		String tBookingID = ordersTable.getSelectionModel().getSelectedItem().getBookingID();
		int tNoOfSeatsBooked = ordersTable.getSelectionModel().getSelectedItem().getNoOfSeatsBooked();
		
		int noOfSeatsAvailable = bk.getBookingsNoOfSeatsAvailable(tBookingID);
		
		Double slNo= sldUpdateNumberOfSeats.getValue();
		int noOfSeats = slNo.intValue();
		
		String cabinNo = txtUpdateCabinNo.getText();
		cabinNo = cabinNo.trim();
		
		// Cabin number validation
		Main_Model main_model = new Main_Model();
		Tooltip cabinNoTooltip = new Tooltip();
		
		// If the cabin number text field is empty or If the cabin number gotten is not valid
		if (cabinNo.isEmpty() || main_model.isCabinNumberValid(cabinNo) != true){
			txtUpdateCabinNo.setStyle(wrong_certificate);
			cabinNoTooltip.setText("Please enter a valid cabin number.");
			txtUpdateCabinNo.setTooltip(cabinNoTooltip);
			return;
		}
		else
		{
			txtUpdateCabinNo.setStyle(correct_certificate);
			cabinNoTooltip.setText(null);
			txtUpdateCabinNo.setTooltip(cabinNoTooltip);
		}
		
		if (tNoOfSeatsBooked > noOfSeats) {
			differenceInNo =  tNoOfSeatsBooked - noOfSeats;
			bk.updateBookingNoOfSeatsAvailableWhenUpdatingOrderAddition(tBookingID, differenceInNo);
			successful = bk.updateOrder(tOrderID, noOfSeats, cabinNo);
			if(successful == true) 
			{
				AppAlert.updateCustomereOrderConfirmationAlert();
				reloadMyBookings();
			}
		}
		else if(noOfSeats > tNoOfSeatsBooked) 
		{
			differenceInNo = noOfSeats - tNoOfSeatsBooked;
			if (differenceInNo > noOfSeatsAvailable)
			{
				String result = AppAlert.joinWaitingListConfirmationAlert();
				
				if (result.equals("Yes"))
				{
					boolean success;
					
					success = bk.addToWaitingList(tBookingID, noOfSeats, cabinNo);
					
					if(success == true)
					{
						bk.cancelCustomerOrder(tOrderID, tBookingID, tNoOfSeatsBooked);
						AppAlert.joinWaitingListConfirmationAlert2();
						reloadMyBookings();
					}
				}
				else 
				{
					return;
				}
			}
			else 
			{
				bk.updateBookingNoOfSeatsAvailableWhenUpdatingOrderSubtraction(tBookingID, differenceInNo);
				successful = bk.updateOrder(tOrderID, noOfSeats, cabinNo);
				if(successful == true) 
				{
					AppAlert.updateCustomereOrderConfirmationAlert();
					reloadMyBookings();
				}
			}
		}
		else if(tNoOfSeatsBooked == noOfSeats)
		{
			successful = bk.updateOrder(tOrderID, noOfSeats, cabinNo);
			if(successful == true)
			{
				AppAlert.updateCustomereOrderConfirmationAlert();
				reloadMyBookings();
			}
		}
		
	}
	
	
	// Cancels my order/booking
	@FXML
	private void cancelMyOrder(ActionEvent event)throws IOException
	{
		if(!ordersTable.getSelectionModel().isEmpty()) 
		{
			String result = AppAlert.cancelOrderConfirmationAlert1a();
			
			if(result.equals("Yes"))
			{
				boolean successful;
				String orderID = ordersTable.getSelectionModel().getSelectedItem().getOrderID();
				String bookingID = ordersTable.getSelectionModel().getSelectedItem().getBookingID();
				int noOfSeatsBooked = ordersTable.getSelectionModel().getSelectedItem().getNoOfSeatsBooked();
				
				successful = bk.cancelCustomerOrder(orderID, bookingID, noOfSeatsBooked);
				
				if(successful == true) 
				{
					AppAlert.cancelOrderConfirmationAlert2();
					reloadMyBookings();
				}
			}
		}
		else 
		{
			AppAlert.cancelOrderSelectionModelErrorAlert();
		}
	}
	
	
	// Reloads my bookings page
	@FXML
	private void reloadMyBookings()throws IOException {
		AnchorPane pane = FXMLLoader.load(getClass().getResource("/application/home/myBookings/MyBookings.fxml"));
		anchor_myBookings.getChildren().setAll(pane);
	}
	
	
}
