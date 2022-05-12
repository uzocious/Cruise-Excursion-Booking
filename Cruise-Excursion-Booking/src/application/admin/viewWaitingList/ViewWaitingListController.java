package application.admin.viewWaitingList;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import application.alert.AppAlert;
import application.model.Booking;
import application.model.WaitingListProperties;


public class ViewWaitingListController implements Initializable {
	
	// Java FX controls
	@FXML private AnchorPane anchor_waitingLiist;
	
	@FXML private TableView<WaitingListProperties> waitingListTable;
	@FXML private TableColumn<WaitingListProperties, String> waitingListID;
	@FXML private TableColumn<WaitingListProperties, String> bookingID;
	@FXML private TableColumn<WaitingListProperties, String> customerID;
	@FXML private TableColumn<WaitingListProperties, Integer> position;
	@FXML private TableColumn<WaitingListProperties, String> dateStarted;
	@FXML private TableColumn<WaitingListProperties, Integer> noOfSeats;
	
	// Booking object
	Booking bk = new Booking();
	
	
	@Override
	public void initialize(URL url, ResourceBundle rb) 
	{
		initCol();
		
		bk.getAllWaitingList();;
		waitingListTable.setItems(bk._allWaitingList);
	}
	
	
	// Table column insider
	private void initCol() 
	{
		waitingListID.setCellValueFactory(new PropertyValueFactory<WaitingListProperties, String>("waitingListID"));
		bookingID.setCellValueFactory(new PropertyValueFactory<WaitingListProperties, String>("bookingID"));
		customerID.setCellValueFactory(new PropertyValueFactory<WaitingListProperties, String>("customerID"));
		position.setCellValueFactory(new PropertyValueFactory<WaitingListProperties, Integer>("position"));
		dateStarted.setCellValueFactory(new PropertyValueFactory<WaitingListProperties, String>("dateStarted"));
		noOfSeats.setCellValueFactory(new PropertyValueFactory<WaitingListProperties, Integer>("noOfSeats"));
	}
	
	
	// Clears all waiting list
	@FXML
	private void clearAllWaitingList() throws IOException
	{
		String result = AppAlert.clearAllWaitingListConfirmationAlert();
		
		if(result.equals("Yes"))
		{
			boolean successful;
			
			successful = bk.clearAllWaitingList();
			
			if (successful == true) 
			{
				AppAlert.clearAllWaitingListConfirmationAlert2();
				reloadViewOrders();
			}
		}
	}
	
	
	// Removes customer from waiting list
	@FXML
	private void deleteWaitingList() throws IOException
	{
		if(!waitingListTable.getSelectionModel().isEmpty()) 
		{
			String result = AppAlert.removeFromWaitListConfirmationAlert();
			
			if(result.equals("Yes"))
			{
				boolean successful;
				String waitingListID = waitingListTable.getSelectionModel().getSelectedItem().getWaitingListID();
				
				successful = bk.removeCustomerFromWaitingList(waitingListID);
				
				if(successful == true) 
				{
					AppAlert.removeFromWaitListConfirmationAlert2();
					reloadViewOrders();
				}
			}
		}
		else 
		{
			AppAlert.removeFromWaitListSelectionModelErrorAlert();
		}
	}
	
	
	// Adds customer in waiting list to order
	@FXML
	private void addWaitingListToOrder() throws IOException
	{
		if(!waitingListTable.getSelectionModel().isEmpty()) 
		{
			boolean successful;
			
			String waitingListID = waitingListTable.getSelectionModel().getSelectedItem().getWaitingListID();
			String customerID = waitingListTable.getSelectionModel().getSelectedItem().getCustomerID();
			String bookingID = waitingListTable.getSelectionModel().getSelectedItem().getBookingID();
			int noOfSeats = waitingListTable.getSelectionModel().getSelectedItem().getNoOfSeats();
			
			String cabinNo = bk.getCustomerCabinNo(customerID);
			
			int noOfSeatsAvailable = bk.getBookingsNoOfSeatsAvailable(bookingID);
			
			if (noOfSeats > noOfSeatsAvailable)
			{
				AppAlert.addToOrderErrorAlert();
				return;
			}
			else if(noOfSeats <= noOfSeatsAvailable) 
			{
				successful = bk.addWaitingListToOrder(customerID, bookingID, noOfSeats, cabinNo);
				
				if(successful == true) 
				{
					bk.removeCustomerFromWaitingList(waitingListID);
					AppAlert.addToOrderConfirmationAlert();
					reloadViewOrders();
				}
			}
			
		}
		else 
		{
			AppAlert.addToOrderSelectionModelErrorAlert();
		}
	}
	
	
	// Reloads view waiting list page
	@FXML
	private void reloadViewOrders()throws IOException {
		AnchorPane pane = FXMLLoader.load(getClass().getResource("/application/admin/viewWaitingList/ViewWaitingList.fxml"));
		anchor_waitingLiist.getChildren().setAll(pane);
	}
	
}
