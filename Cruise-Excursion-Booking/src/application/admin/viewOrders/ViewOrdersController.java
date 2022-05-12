package application.admin.viewOrders;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import application.alert.AppAlert;
import application.model.Booking;
import application.model.OrderProperties;
import javafx.fxml.Initializable;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;


public class ViewOrdersController implements Initializable {
	
	// Java FX controls
	@FXML private AnchorPane anchor_order;
	
	@FXML private TableView<OrderProperties> ordersTable;
	@FXML private TableColumn<OrderProperties, String> orderID;
	@FXML private TableColumn<OrderProperties, String> orderDate;
	@FXML private TableColumn<OrderProperties, String> customerID;
	@FXML private TableColumn<OrderProperties, String> bookingID;
	@FXML private TableColumn<OrderProperties, Integer> noOfSeatsBooked;
	@FXML private TableColumn<OrderProperties, String> cabinNo;
	
	// Booking object
	Booking bk = new Booking();
	
	@Override
	public void initialize(URL url, ResourceBundle rb) 
	{
		initCol();
		
		bk.getAllOrders();
		ordersTable.setItems(bk._allOrders);
	}
	
	// Table column insider
	private void initCol() {
		orderID.setCellValueFactory(new PropertyValueFactory<OrderProperties, String>("orderID"));
		orderDate.setCellValueFactory(new PropertyValueFactory<OrderProperties, String>("orderDate"));
		customerID.setCellValueFactory(new PropertyValueFactory<OrderProperties, String>("customerID"));
		bookingID.setCellValueFactory(new PropertyValueFactory<OrderProperties, String>("bookingID"));
		noOfSeatsBooked.setCellValueFactory(new PropertyValueFactory<OrderProperties, Integer>("noOfSeatsBooked"));
		cabinNo.setCellValueFactory(new PropertyValueFactory<OrderProperties, String>("cabinNo"));
	}
	
	
	// Cancels order
	@FXML
	private void cancelOrder(ActionEvent event)throws IOException
	{
		if(!ordersTable.getSelectionModel().isEmpty()) 
		{
			String result = AppAlert.cancelOrderConfirmationAlert();
			
			if(result.equals("Yes"))
			{
				boolean successful;
				String orderID = ordersTable.getSelectionModel().getSelectedItem().getOrderID();
				String bookingID = ordersTable.getSelectionModel().getSelectedItem().getBookingID();
				int noOfSeatsBooked = ordersTable.getSelectionModel().getSelectedItem().getNoOfSeatsBooked();
				
				successful = bk.cancelOrder(orderID, bookingID, noOfSeatsBooked);
				
				if(successful == true) 
				{
					AppAlert.cancelOrderConfirmationAlert2();
					reloadViewOrders();
				}
			}
		}
		else 
		{
			AppAlert.cancelOrderSelectionModelErrorAlert();
		}
	}
	
	
	// Reloads view orders page
	@FXML
	private void reloadViewOrders()throws IOException {
		AnchorPane pane = FXMLLoader.load(getClass().getResource("/application/admin/viewOrders/ViewOrders.fxml"));
		anchor_order.getChildren().setAll(pane);
	}
	
	
}
