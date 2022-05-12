package application.home;

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


public class HomeController implements Initializable 
{
	// Java FX controls
	@FXML private Label lblCustomerName;

	@FXML private AnchorPane anchor_home;
	
	// Local variable
	public String _customerID = MainController._customerID;
	
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		try {
			Booking bk = new Booking();
			lblCustomerName.setText("Hello " + bk.getCustomerName());
		}
		catch(Exception e) {
			System.out.println(e);
		}
	}
	
	// loads home page
	@FXML
	private void loadHome(ActionEvent event)throws IOException{
		AnchorPane pane = FXMLLoader.load(getClass().getResource("/application/home/start/Start.fxml"));
		anchor_home.getChildren().setAll(pane);
	}
	
	// loads profile page
	@FXML
	private void loadProfile(ActionEvent event) throws IOException{
		AnchorPane pane = FXMLLoader.load(getClass().getResource("/application/home/profile/Profile.fxml"));
		anchor_home.getChildren().setAll(pane);
		
	}
	
	// loads my bookings page
	@FXML
	private void loadMyBookings(ActionEvent event) throws IOException{
		AnchorPane pane = FXMLLoader.load(getClass().getResource("/application/home/myBookings/MyBookings.fxml"));
		anchor_home.getChildren().setAll(pane);
		
	}
	
	// loads waiting list page
	@FXML
	private void loadWaitingList(ActionEvent event) throws IOException{
		AnchorPane pane = FXMLLoader.load(getClass().getResource("/application/home/waitingList/WaitingList.fxml"));
		anchor_home.getChildren().setAll(pane);
		
	}
	
	// signs out
	@FXML
	private void signOut(ActionEvent event){
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
