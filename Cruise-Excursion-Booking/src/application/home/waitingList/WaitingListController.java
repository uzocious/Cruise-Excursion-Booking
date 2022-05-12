package application.home.waitingList;

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


public class WaitingListController implements Initializable {
	
	// Java FX controls
	@FXML private AnchorPane anchor_myWaitingList;
	
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
		
		bk.getCustomerWaitingList();
		waitingListTable.setItems(bk._customerWaitingList);
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
	
	
	// Removes customer from waiting list
	@FXML
	private void deleteWaitingList() throws IOException
	{
		// If table has been selected
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
					reloadMyWaitingList();
				}
			}
		}
		else 
		{
			AppAlert.removeFromWaitListSelectionModelErrorAlert();
		}
	}
		
	
	// Reloads my waiting list page
	@FXML
	private void reloadMyWaitingList()throws IOException {
		AnchorPane pane = FXMLLoader.load(getClass().getResource("/application/home/waitingList/WaitingList.fxml"));
		anchor_myWaitingList.getChildren().setAll(pane);
	}
	
}
