package application.alert;

import java.util.Optional;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

// Application alerts (i.e. Message boxes used to validate operations withing the program)
public class AppAlert
{
	
	// Program close (close operation) confirmation message box
	public static void CloseCurrentWindowConfirmationAlert(Stage primaryStage) {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Cruse Excursion Booking (C.E.B)");
		alert.setContentText("Are you sure you want to close?");
		alert.setHeaderText(null);
		
		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK) {
			primaryStage.close();
		}
	}
	
	
	
	
	//  Search error error alert
	public static void searchErrorAlert() {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Cruse Excursion Booking (C.E.B)");
		alert.setContentText("There are no search results. Please select from list.");
		alert.setHeaderText(null);
		alert.showAndWait();
	}
	
	//  Add booking error alert
	public static void addBoookingErrorAlert() {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Cruse Excursion Booking (C.E.B)");
		alert.setContentText("All sections must be completed to add excursion for booking.");
		alert.setHeaderText(null);
		alert.showAndWait();
	}
	
	//  Add booking confirmation alert
	public static void addBookingConfirmationAlert() {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Cruse Excursion Booking (C.E.B)");
		alert.setContentText("Excursion has successfully been added for booking.");
		alert.setHeaderText(null);
		alert.showAndWait();
	}
	
	
	
	
	//  Update booking selection model error alert
	public static void updateBoookingSelectionModelErrorAlert() {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Cruse Excursion Booking (C.E.B)");
		alert.setContentText("Select one from the table above to update.");
		alert.setHeaderText(null);
		alert.showAndWait();
	}
	
	//  Delete booking selection model error alert
	public static void deleteBoookingSelectionModelErrorAlert() {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Cruse Excursion Booking (C.E.B)");
		alert.setContentText("Select one from the table above to delete.");
		alert.setHeaderText(null);
		alert.showAndWait();
	}
	
	//  Update booking error alert
	public static void updateBoookingErrorAlert() {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Cruse Excursion Booking (C.E.B)");
		alert.setContentText("All sections must be completed to update booking.");
		alert.setHeaderText(null);
		alert.showAndWait();
	}
	
	//  Update booking confirmation alert
	public static void updateBookingConfirmationAlert() {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Cruse Excursion Booking (C.E.B)");
		alert.setContentText("Booking has successfully been updated.");
		alert.setHeaderText(null);
		alert.showAndWait();
	}
	
	// Delete booking confirmation alert
	public static String deleteBookingConfirmationAlert() {
		String confirmation = "";
		
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Cruse Excursion Booking (C.E.B)");
		alert.setContentText("Are you sure you want to delete the selected booking?\r\n" +
				"If you do, the booking will be deleted from all customers' order and the waiting list.\r\n" +
				"It is Non-retrievable.");
		alert.setHeaderText(null);
		
		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK) {
			confirmation = "Yes";
		}
		
		return confirmation;
	}
	
	//  Delete booking confirmation alert 2
	public static void deleteBookingConfirmationAlert2() {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Cruse Excursion Booking (C.E.B)");
		alert.setContentText("Booking has now successfully been deleted.");
		alert.setHeaderText(null);
		alert.showAndWait();
	}
	
	
	
	
	
	
	
	// Join waiting list confirmation alert (customer)
	public static String joinWaitingListConfirmationAlert() {
		String confirmation = "";
		
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Cruse Excursion Booking (C.E.B)");
		alert.setContentText("Your number of seats is more then the number of seats available.\r\n" +
				"Would you like to joing the waiting list?");
		alert.setHeaderText(null);
		
		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK) {
			confirmation = "Yes";
		}
		
		return confirmation;
	}
	
	//  Join waiting list confirmation alert 2 (customer)
	public static void joinWaitingListConfirmationAlert2() {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Cruse Excursion Booking (C.E.B)");
		alert.setContentText("You have successfully joined the waiting list. Check your waiting list for more.");
		alert.setHeaderText(null);
		alert.showAndWait();
	}
	
	//  Customer already in the waiting list (customer)
	public static void customerAlreadyInWaitingListWarningAlert() {
		Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle("Cruse Excursion Booking (C.E.B)");
		alert.setContentText("Sorry!! but you have already been added to the waiting list."
				+ "Check your waiting list for more.");
		alert.setHeaderText(null);
		alert.showAndWait();
	}

	
	
	
	
	
	//  Update booking selection model error alert
	public static void bookNowgSelectionModelErrorAlert() {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Cruse Excursion Booking (C.E.B)");
		alert.setContentText("Select an excursion from the list of excursions to start booking.");
		alert.setHeaderText(null);
		alert.showAndWait();
	}
	
	//  Customer order (book now!) error alert
	public static void bookNowErrorAlert() {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Cruse Excursion Booking (C.E.B)");
		alert.setContentText("Sorry! but you can't book an excursion more than once.");
		alert.setHeaderText(null);
		alert.showAndWait();
	}
	
	//  Customer order (book now!) confirmation alert
	public static void bookNowConfirmationAlert() {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Cruse Excursion Booking (C.E.B)");
		alert.setContentText("Excursion has successfully been booked.");
		alert.setHeaderText(null);
		alert.showAndWait();
	}
	
	
	
	
	
	
	
	
	//  Cancel order selection model error alert (administrator and customer)
	public static void cancelOrderSelectionModelErrorAlert() {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Cruse Excursion Booking (C.E.B)");
		alert.setContentText("Select one from the table above to cancel order.");
		alert.setHeaderText(null);
		alert.showAndWait();
	}
	
	// Cancel order confirmation alert (administrator)
	public static String cancelOrderConfirmationAlert() {
		String confirmation = "";
		
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Cruse Excursion Booking (C.E.B)");
		alert.setContentText("Are you sure you want to cancel the selected order?\r\n" +
				"If you do, the order will also be cancelled from the customer's order.\r\n" +
				"It is Non-retrievable.");
		alert.setHeaderText(null);
		
		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK) {
			confirmation = "Yes";
		}
		
		return confirmation;
	}
	
	// Cancel order confirmation alert (customer)
	public static String cancelOrderConfirmationAlert1a() {
		String confirmation = "";
		
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Cruse Excursion Booking (C.E.B)");
		alert.setContentText("Are you sure you want to cancel the selected order?\r\n" +
				"If you do, the order will be permanently deleted from your bookings "
				+ "and it will be Non-retrievable");
		alert.setHeaderText(null);
		
		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK) {
			confirmation = "Yes";
		}
		
		return confirmation;
	}
	
	//  Cancel order confirmation alert 2 (administrator and customer)
	public static void cancelOrderConfirmationAlert2() {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Cruse Excursion Booking (C.E.B)");
		alert.setContentText("Order has now successfully been cancelled.");
		alert.setHeaderText(null);
		alert.showAndWait();
	}
	
	
	
	
	
	
	
	//  Update order confirmation alert
	public static void updateCustomereOrderConfirmationAlert() {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Cruse Excursion Booking (C.E.B)");
		alert.setContentText("Order has successfully been updated.");
		alert.setHeaderText(null);
		alert.showAndWait();
	}
	
	
	
	
	
	
	
	//  Add to order selection model error alert
	public static void addToOrderSelectionModelErrorAlert() {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Cruse Excursion Booking (C.E.B)");
		alert.setContentText("Select one from the table above to add to order.");
		alert.setHeaderText(null);
		alert.showAndWait();
	}
	
	//  Add to order selection model error alert
	public static void addToOrderErrorAlert() {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Cruse Excursion Booking (C.E.B)");
		alert.setContentText("The selected number of seats is more then the number of seats available.");
		alert.setHeaderText(null);
		alert.showAndWait();
	}
	
	//  Delete booking confirmation alert 2
	public static void addToOrderConfirmationAlert() {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Cruse Excursion Booking (C.E.B)");
		alert.setContentText("It has successfully been added to orders.");
		alert.setHeaderText(null);
		alert.showAndWait();
	}
	
	//  Remove from waiting list selection model error alert
	public static void removeFromWaitListSelectionModelErrorAlert() {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Cruse Excursion Booking (C.E.B)");
		alert.setContentText("Select one from the table above to remove from waiting list.");
		alert.setHeaderText(null);
		alert.showAndWait();
	}
	
	// Delete booking confirmation alert
	public static String removeFromWaitListConfirmationAlert() {
		String confirmation = "";
		
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Cruse Excursion Booking (C.E.B)");
		alert.setContentText("Are you sure you want to remove the selected booking from waiting list?");
		alert.setHeaderText(null);
		
		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK) {
			confirmation = "Yes";
		}
		
		return confirmation;
	}
	
	//  Delete booking confirmation alert 2
	public static void removeFromWaitListConfirmationAlert2() {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Cruse Excursion Booking (C.E.B)");
		alert.setContentText("Booking has successfully been removed from waiting list.");
		alert.setHeaderText(null);
		alert.showAndWait();
	}
	
	
	// Delete booking confirmation alert
	public static String clearAllWaitingListConfirmationAlert() {
		String confirmation = "";
		
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Cruse Excursion Booking (C.E.B)");
		alert.setContentText("Are you sure you want to clear all from the waiting list table?\r\n" +
				"If you do, it will be deleted from all customers' waiting list.\r\n" +
				"It is Non-retrievable.");
		alert.setHeaderText(null);
		
		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK) {
			confirmation = "Yes";
		}
		
		return confirmation;
	}
	
	//  Delete booking confirmation alert 2
	public static void clearAllWaitingListConfirmationAlert2() {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Cruse Excursion Booking (C.E.B)");
		alert.setContentText("Waiting list table has successfully been cleared.");
		alert.setHeaderText(null);
		alert.showAndWait();
	}
	
	
	
	
	
	
	
	
	
	
	//  Profile update confirmation alert
	public static void profiltUpdateConfirmationAlert() {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Cruse Excursion Booking (C.E.B)");
		alert.setContentText("Your profile has successfully been updated.");
		alert.setHeaderText(null);
		alert.showAndWait();
	}
	
	//  Password update confirmation alert
	public static void passwordUpdateConfirmationAlert() {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Cruse Excursion Booking (C.E.B)");
		alert.setContentText("Your password has successfully been changed.");
		alert.setHeaderText(null);
		alert.showAndWait();
	}
	
	
	
}