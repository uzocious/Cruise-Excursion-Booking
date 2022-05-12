package application.admin;
	
import application.alert.AppAlert;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;


public class Admin extends Application 
{
	// Admin Loader
	@Override
	public void start(Stage primaryStage) {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("/application/admin/Admin.fxml"));
			
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("/application.css").toExternalForm());
			
			primaryStage.setScene(scene);
			primaryStage.setResizable(false);
			primaryStage.setTitle("Cruise Excursion Booking (C.E.B)");
			primaryStage.setOnCloseRequest(e -> {
				e.consume();
				AppAlert.CloseCurrentWindowConfirmationAlert(primaryStage);
				});
			primaryStage.show();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
