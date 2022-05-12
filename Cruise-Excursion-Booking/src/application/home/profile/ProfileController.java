package application.home.profile;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;

import com.jfoenix.controls.*;

import application.alert.AppAlert;
import application.model.Main_Model;
import application.model.Profile;


public class ProfileController implements Initializable {
	
	// Java FX controls
	@FXML private Label lblCustomerID;
	@FXML private Label lblUNCBC;
	@FXML private JFXTextField txtFirstName;
	@FXML private JFXTextField txtLastName;
	@FXML private JFXTextField txtEmail;
	@FXML private JFXTextField txtCabinNo;
	@FXML private JFXTextField txtUsername;
	@FXML private JFXPasswordField txtNewPassword;
	@FXML private JFXPasswordField txtReenterPassword;
	@FXML private JFXButton btnSubmitProfile;
	
	@FXML private AnchorPane anchor_profile;
	
	// Profile object
	Profile profile = new Profile();
	
	// Main Model object
	Main_Model main_model = new Main_Model();
	
	// Text field CSS Styles for validation
	String wrong_certificate = "-jfx-unfocus-color: red; -jfx-focus-color: red;";
	String correct_certificate = "-jfx-unfocus-color: #4d4d4d; -jfx-focus-color: #2A2E37;";
	
	@Override
	public void initialize(URL url, ResourceBundle rb) 
	{
		profile.getCustomerDetails();
		
		String customerID = profile.getCustomerID();
		String firstName = profile.getCustomerFirstName();
		String lastName = profile.getCustomerLastName();
		String email = profile.getCustomerEmail();
		String cabinNo = profile.getCustomerCabinNumber();
		String username = profile.getCustomerUserName(); 
		
		lblCustomerID.setText(customerID);
		txtFirstName.setText(firstName);
		txtLastName.setText(lastName);
		txtEmail.setText(email);
		txtCabinNo.setText(cabinNo);
		txtUsername.setText(username);
		
	}
	
	
	// All customer to be able to update/change profile
	@FXML
	private void changeProfile(ActionEvent event)
	{
		txtFirstName.setEditable(true);
		txtLastName.setEditable(true);
		txtEmail.setEditable(true);
		txtCabinNo.setEditable(true);
		btnSubmitProfile.setVisible(true);
		lblUNCBC.setVisible(true);
		
		txtFirstName.requestFocus();
		txtFirstName.selectAll();
	}
	
	
	// Submits updated/changed profile
	@FXML
	private void submitUpdatedProfile(ActionEvent event) throws IOException
	{
		String firstName = txtFirstName.getText();;
		String lastName = txtLastName.getText();
		String email = txtEmail.getText();
		String cabinNo = txtCabinNo.getText();
		
		firstName = firstName.trim();
		lastName = lastName.trim();
		email = email.trim();
		cabinNo = cabinNo.trim();
		
		boolean successful = profile.updateCustomerDetails(firstName, lastName, email, cabinNo);
		
		updateProfileValidations(firstName, lastName, email, cabinNo, successful);
		
	}
	
	
	// Submits updated/changed password
	@FXML
	private void submitUpdatedPassword(ActionEvent event) throws IOException
	{
		boolean successful;
		
		String newPassword =  txtNewPassword.getText();
		String reEnterPassword = txtReenterPassword.getText();
		
		successful = profile.updateCustomerPassword(newPassword, reEnterPassword);
		
		udpatePasswordValidations(newPassword, reEnterPassword, successful);
		
	}
	
	
	// Reloads profile page
	@FXML
	private void reloadProfile()throws IOException {
		AnchorPane pane = FXMLLoader.load(getClass().getResource("/application/home/profile/Profile.fxml"));
		anchor_profile.getChildren().setAll(pane);
	}
	
	
	// Updates profile validations
	private void updateProfileValidations(String firstName, String lastName, String email, String cabinNo, boolean successful) throws IOException
	{
		// Tooltip Objects
		Tooltip firstnameTooltip = new Tooltip();
		Tooltip lastnameTooltip = new Tooltip();
		Tooltip emailTooltip = new Tooltip();
		Tooltip cabinNoTooltip = new Tooltip();
		
		
		// If the first name text field is empty
		if (firstName.isEmpty()){
			txtFirstName.setStyle(wrong_certificate);
			firstnameTooltip.setText("Please enter your first name.");
			txtFirstName.setTooltip(firstnameTooltip);
		}
		else{
			txtFirstName.setStyle(correct_certificate);
			firstnameTooltip.setText(null);
			txtFirstName.setTooltip(firstnameTooltip);
		}
		
		
		// If the last name text field is empty
		if (lastName.isEmpty()){
			txtLastName.setStyle(wrong_certificate);
			lastnameTooltip.setText("Please enter your last name");
			txtLastName.setTooltip(lastnameTooltip);
		}
		else{
			txtLastName.setStyle(correct_certificate);
			lastnameTooltip.setText(null);
			txtLastName.setTooltip(lastnameTooltip);
		}
		
		
		// If the email text field is empty or If the email gotten is not valid
		if (email.isEmpty() || main_model.isEmailValid(email) != true){
			txtEmail.setStyle(wrong_certificate);
			emailTooltip.setText("Please enter a valid email address.");
			txtEmail.setTooltip(emailTooltip);
		}
		else
		{
			String customerEmail = profile.getCustomerEmail();
			if (customerEmail.equals(email))
			{
				txtEmail.setStyle(correct_certificate);
				emailTooltip.setText(null);
				txtEmail.setTooltip(emailTooltip);
			}
			else if (main_model.doesEmailExistsInDB(email) == true)
			{
				txtEmail.setStyle(wrong_certificate);
				emailTooltip.setText("Email already exists.");
				txtEmail.setTooltip(emailTooltip);
			}
			else
			{
				txtEmail.setStyle(correct_certificate);
				emailTooltip.setText(null);
				txtEmail.setTooltip(emailTooltip);
			}
		}
		
		// If the cabin number text field is empty or If the cabin number gotten is not valid
		if (cabinNo.isEmpty() || main_model.isCabinNumberValid(cabinNo) != true){
			txtCabinNo.setStyle(wrong_certificate);
			cabinNoTooltip.setText("Please enter a valid cabin number.");
			txtCabinNo.setTooltip(cabinNoTooltip);
		}
		else{
			txtCabinNo.setStyle(correct_certificate);
			cabinNoTooltip.setText(null);
			txtCabinNo.setTooltip(cabinNoTooltip);
			}
		
		
		// If all the required information is accurate
		if(successful == true)
		{
			AppAlert.profiltUpdateConfirmationAlert();
			
			reloadProfile();
		}
		else
		{
			return;
		}
	}
	
	
	// Updates password validations
	private void udpatePasswordValidations(String newPassword, String reEnterPassword, boolean successful) throws IOException 
	{
		// Tooltip Objects
		Tooltip enterBothPasswordTooltip = new Tooltip();
		Tooltip matchPasswordTooltip = new Tooltip();
		
		// If the new password and the re-enter password text field is empty
		if (newPassword.isEmpty() && reEnterPassword.isEmpty())
		{
			txtNewPassword.setStyle(wrong_certificate);
			txtReenterPassword.setStyle(wrong_certificate);
			enterBothPasswordTooltip.setText("Please enter password.");
			txtNewPassword.setTooltip(enterBothPasswordTooltip);
			txtReenterPassword.setTooltip(enterBothPasswordTooltip);
		}
		else{
			
			// If the new password and the re-entered password does not match
			if (!reEnterPassword.equals(newPassword))
			{
				txtNewPassword.setStyle(wrong_certificate);
				txtReenterPassword.setStyle(wrong_certificate);
				matchPasswordTooltip.setText("Password does not match.");
				txtNewPassword.setTooltip(matchPasswordTooltip);
				txtReenterPassword.setTooltip(matchPasswordTooltip);
			}
			else
			{
				txtNewPassword.setStyle(correct_certificate);
				txtReenterPassword.setStyle(correct_certificate);
				matchPasswordTooltip.setText(null);
				txtNewPassword.setTooltip(matchPasswordTooltip);
				txtReenterPassword.setTooltip(matchPasswordTooltip);
			}
		}
		
		
		// If required information is accurate
		if(successful == true)
		{
			AppAlert.passwordUpdateConfirmationAlert();
			
			reloadProfile();
		}
		else 
		{
			return;
		}
		
		
	}
	
	
	
	
}
