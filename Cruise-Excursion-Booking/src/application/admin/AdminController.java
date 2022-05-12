package application.admin;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import application.main.Main;
import application.main.MainController;
import application.model.Booking;
import javafx.fxml.Initializable;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;


public class AdminController implements Initializable {
	
	@FXML private Label lblAdminName;
	
	@FXML private AnchorPane anchor_admin;
	
	public String _adminID = MainController._adminID;
	

	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		try {
			Booking bk = new Booking();
			lblAdminName.setText("Welcome " + bk.getAdminName());
			
			AnchorPane pane = FXMLLoader.load(getClass().getResource("/application/admin/addBookings/AddBookings.fxml"));
			anchor_admin.getChildren().setAll(pane);
		}
		catch(Exception e) {
			System.out.println(e);
		}
		
	}
	
	// loads add bookings page
	@FXML
	private void loadAddBookings(ActionEvent event)throws IOException{
		AnchorPane pane = FXMLLoader.load(getClass().getResource("/application/admin/addBookings/AddBookings.fxml"));
		anchor_admin.getChildren().setAll(pane);
	}
	
	// loads view bookings page
	@FXML
	private void loadViewBookings(ActionEvent event) throws IOException{
		AnchorPane pane = FXMLLoader.load(getClass().getResource("/application/admin/viewBookings/ViewBookings.fxml"));
		anchor_admin.getChildren().setAll(pane);
		
	}
	
	// loads view waiting list page
	@FXML
	private void loadViewWaitingList(ActionEvent event) throws IOException{
		AnchorPane pane = FXMLLoader.load(getClass().getResource("/application/admin/viewWaitingList/ViewWaitingList.fxml"));
		anchor_admin.getChildren().setAll(pane);
		
	}
	
	// loads profile page
	@FXML
	private void loadViewOrders(ActionEvent event) throws IOException{
		AnchorPane pane = FXMLLoader.load(getClass().getResource("/application/admin/viewOrders/ViewOrders.fxml"));
		anchor_admin.getChildren().setAll(pane);
		
	}
	
	
	// signs out
	@FXML
	private void logOut(ActionEvent event){
		HideStage(event);
		LoadMainPage();
	}
	
	// Loads the application main page (i.e. login page)
	private void LoadMainPage() {
		Main Main = new Main();
		
		Stage primaryStage = new Stage();
		
		Main.start(primaryStage);
	}
	
	// Hides the home page
	private void HideStage(ActionEvent event) {
		((Node)event.getSource()).getScene().getWindow().hide();
	}
	
}
