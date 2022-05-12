package application.main;

import com.jfoenix.controls.*;
import application.admin.Admin;
import application.home.Home;
import application.model.Main_Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.stage.Stage;

public class MainController {
	
	// SIGN IN FIELDS
	@FXML private JFXTextField txtSignInUsername;
	@FXML private JFXPasswordField txtSignInPassword;
	
	// FORGET PASSWORD FIELDS	
	@FXML private JFXTextField txtFPEmail;
	@FXML private JFXPasswordField txtFPNewPassword;
	@FXML private JFXPasswordField txtFPReenterPassword;
	@FXML private Label lblFPSuccess;
	
	// REGISTER FIELDS
	@FXML private JFXTextField txtRegFirstName;
	@FXML private JFXTextField txtRegLastName;
	@FXML private JFXTextField txtRegEmail;
	@FXML private JFXTextField txtRegCabinNo;
	@FXML private JFXTextField txtRegUsername;
	@FXML private JFXPasswordField txtRegPassword;
	@FXML private JFXPasswordField txtRegReenterPassword;
	@FXML private Label lblRegSuccess;
	
	
	//Local static variables
	public static String _customerID;
	public static String _adminID;
	
	// Text field CSS Styles for validation
	String wrong_certificate = "-jfx-unfocus-color: red; -jfx-focus-color: red;";
	String correct_certificate = "-jfx-unfocus-color: #4d4d4d; -jfx-focus-color: #2A2E37;";

	
	// Sign in Button Event Handler
	@FXML
	private void SignInButtonActionHandler(ActionEvent event)
	{
		String username = txtSignInUsername.getText();
		String password = txtSignInPassword.getText();
		
		username = username.trim();
		
		boolean signInSuccess;

		Main_Model mainModel = new Main_Model();
		
		signInSuccess = mainModel.signIn(username, password);
		
		if(signInSuccess == true)
		{
			if(mainModel.getCustomerID() != null) 
			{
				_customerID = mainModel.getCustomerID();
				HideStage(event);
				LoadHomePage();
			}
			else
			{
				_adminID = mainModel.getAdminID();
				HideStage(event);
				LoadAdminHomePage();
			}	
		}
		else {
			// Validations
			txtSignInUsername.getStyleClass().add("wrong-credentials");
			txtSignInPassword.getStyleClass().add("wrong-credentials");
			
			Tooltip tooltip = new Tooltip("Username or Password is incorrect.");
			txtSignInUsername.setTooltip(tooltip);
			txtSignInPassword.setTooltip(tooltip);
		}
	}
	
	
	// Registration Button Event Handler
	@FXML
	private void RegistrationButtonActionHandler(ActionEvent event)
	{
		lblRegSuccess.setVisible(false);
		
		boolean successful;
		
		String firstName = txtRegFirstName.getText();
		String lastName = txtRegLastName.getText();
		String email = txtRegEmail.getText();
		String cabinNo = txtRegCabinNo.getText();
		String username = txtRegUsername.getText();
		String password = txtRegPassword.getText();
		String re_enterPassword = txtRegReenterPassword.getText();
		
		firstName = firstName.trim();
		lastName = lastName.trim();
		email = email.trim();
		cabinNo = cabinNo.trim();
		username = username.trim();
		
		Main_Model Main_Model = new Main_Model();
		
		successful = Main_Model.registration(firstName, lastName, email, cabinNo, username, password, re_enterPassword);
		
		RegistrationValidations(firstName, lastName, email, cabinNo, username, password, re_enterPassword, successful);
	}
	

	// Forget Password Button Event Handler
	@FXML
	private void ForgetPasswordButtonActionHandler(ActionEvent event)
	{
		lblFPSuccess.setVisible(false);
		
		String email = txtFPEmail.getText();
		String newPassword =  txtFPNewPassword.getText();
		String re_enterPassword = txtFPReenterPassword.getText();
		
		email = email.trim();
		
		boolean successful;
		
		Main_Model Main_Model = new Main_Model();
		
		successful = Main_Model.forgetPassword(email, newPassword, re_enterPassword);
		
		ForgetPasswordValidations(email, newPassword, re_enterPassword, successful);
		
	}
	
	
	// Loads the application home page (for customers only)
	private void LoadHomePage() {
		Home Home = new Home();
		
		Stage primaryStage = new Stage();
		
		Home.start(primaryStage);
	}
	
	
	// Loads the application administrator page
	private void LoadAdminHomePage() {
		Admin Admin = new Admin();
		
		Stage primaryStage = new Stage();
		
		Admin.start(primaryStage);
	}
	
	
	// Hides the main page
	private void HideStage(ActionEvent event) {
		((Node)event.getSource()).getScene().getWindow().hide();
	}
	
	
	// Registration action validations
	private void RegistrationValidations(String firstName, String lastName, String email, String cabinNo, String username, String password, String re_enterPassword, boolean successful)
	{
		Main_Model main_model = new Main_Model();
		
		// Tooltip Objects
		Tooltip firstnameTooltip = new Tooltip();
		Tooltip lastnameTooltip = new Tooltip();
		Tooltip emailTooltip = new Tooltip();
		Tooltip cabinNoTooltip = new Tooltip();
		Tooltip usernameTooltip = new Tooltip();
		Tooltip passwordTooltip = new Tooltip();
		Tooltip validDetailsTooltip = new Tooltip();
		
		
		// If the first name text field is empty
		if (firstName.isEmpty()){
			txtRegFirstName.setStyle(wrong_certificate);
			firstnameTooltip.setText("Please enter your first name.");
			txtRegFirstName.setTooltip(firstnameTooltip);
		}
		else{
			txtRegFirstName.setStyle(correct_certificate);
			firstnameTooltip.setText(null);
			txtRegFirstName.setTooltip(firstnameTooltip);
			}
		
		
		// If the last name text field is empty
		if (lastName.isEmpty()){
			txtRegLastName.setStyle(wrong_certificate);
			lastnameTooltip.setText("Please enter your last name");
			txtRegLastName.setTooltip(lastnameTooltip);
		}
		else{
			txtRegLastName.setStyle(correct_certificate);
			lastnameTooltip.setText(null);
			txtRegLastName.setTooltip(lastnameTooltip);
			}
		
		
		// If the email text field is empty or If the email gotten is not valid
		if (email.isEmpty() || main_model.isEmailValid(email) != true){
			txtRegEmail.setStyle(wrong_certificate);
			emailTooltip.setText("Please enter a valid email address.");
			txtRegEmail.setTooltip(emailTooltip);
		}
		else
		{
			// If the email gotten already exists
			if (main_model.doesEmailExistsInDB(email) == true){
				txtRegEmail.setStyle(wrong_certificate);
				emailTooltip.setText("Email already exists.");
				txtRegEmail.setTooltip(emailTooltip);
			}
			else
			{
				txtRegEmail.setStyle(correct_certificate);
				emailTooltip.setText(null);
				txtRegEmail.setTooltip(emailTooltip);
			}
		}
		
		
		// If the cabin number text field is empty or If the cabin number gotten is not valid
		if (cabinNo.isEmpty() || main_model.isCabinNumberValid(cabinNo) != true){
			txtRegCabinNo.setStyle(wrong_certificate);
			cabinNoTooltip.setText("Please enter a valid cabin number.");
			txtRegCabinNo.setTooltip(cabinNoTooltip);
		}
		else{
			txtRegCabinNo.setStyle(correct_certificate);
			cabinNoTooltip.setText(null);
			txtRegCabinNo.setTooltip(cabinNoTooltip);
			}
		
		
		// If the username text field is empty
		if (username.isEmpty()){
			txtRegUsername.setStyle(wrong_certificate);
			usernameTooltip.setText("Please enter a username.");
			txtRegUsername.setTooltip(usernameTooltip);
		}
		else
		{
			// If the username gotten already exists
			if (main_model.doesUsernameExistsInDB(username) == true){
				txtRegUsername.setStyle(wrong_certificate);
				usernameTooltip.setText("Username already exists.");
				txtRegUsername.setTooltip(usernameTooltip);
			}
			else
			{
				txtRegUsername.setStyle(correct_certificate);
				usernameTooltip.setText(null);
				txtRegUsername.setTooltip(usernameTooltip);
			}
		}
		
		
		// If the password and the re-enter password text field is empty
		if (password.isEmpty() && re_enterPassword.isEmpty())
		{
			txtRegPassword.setStyle(wrong_certificate);
			txtRegReenterPassword.setStyle(wrong_certificate);
			passwordTooltip.setText("Please enter password.");
			txtRegPassword.setTooltip(passwordTooltip);
			txtRegReenterPassword.setTooltip(passwordTooltip);
		}
		else{
			
			// If the new password and the re-entered password does not match
			if (!re_enterPassword.equals(password))
			{
				txtRegPassword.setStyle(wrong_certificate);
				txtRegReenterPassword.setStyle(wrong_certificate);
				passwordTooltip.setText("Password does not match.");
				txtRegPassword.setTooltip(passwordTooltip);
				txtRegReenterPassword.setTooltip(passwordTooltip);
			}
			else
			{
				txtRegPassword.setStyle(correct_certificate);
				txtRegReenterPassword.setStyle(correct_certificate);
				passwordTooltip.setText(null);
				txtRegPassword.setTooltip(passwordTooltip);
				txtRegReenterPassword.setTooltip(passwordTooltip);
			}
		}
		
		
		// If all the required information is accurate
		if(successful == true)
		{
			lblRegSuccess.setVisible(true);
			
			// Changes style
			txtRegFirstName.setStyle(correct_certificate);
			txtRegLastName.setStyle(correct_certificate);
			txtRegEmail.setStyle(correct_certificate);
			txtRegCabinNo.setStyle(correct_certificate);
			txtRegUsername.setStyle(correct_certificate);
			txtRegPassword.setStyle(correct_certificate);
			txtRegReenterPassword.setStyle(correct_certificate);
			
			// Sets each text field's tooltip to null
			validDetailsTooltip.setText(null);
			
			txtRegFirstName.setTooltip(validDetailsTooltip);
			txtRegLastName.setTooltip(validDetailsTooltip);
			txtRegEmail.setTooltip(validDetailsTooltip);
			txtRegCabinNo.setTooltip(validDetailsTooltip);
			txtRegUsername.setTooltip(validDetailsTooltip);
			txtRegPassword.setTooltip(validDetailsTooltip);
			txtRegReenterPassword.setTooltip(validDetailsTooltip);
			
			// Clears all text in each text field
			txtRegFirstName.clear();
			txtRegLastName.clear();
			txtRegEmail.clear();
			txtRegCabinNo.clear();
			txtRegUsername.clear();
			txtRegPassword.clear();
			txtRegReenterPassword.clear();
		}
		else
		{
			return;
		}
		
	}
	
	
	// Forget password action validations
	private void ForgetPasswordValidations(String email, String newPassword, String re_enterPassword, boolean successful)
	{		
		// Tooltip Objects
		Tooltip enterEmailTooltip = new Tooltip();
		Tooltip enterBothPasswordTooltip = new Tooltip();
		Tooltip matchPasswordTooltip = new Tooltip();
		Tooltip allDetailsValid = new Tooltip();
		Tooltip unrecognisedEmailTooltip = new Tooltip();
		Tooltip wrongEmailTooltip = new Tooltip();
		
		
		// If the new password and the re-enter password text field is empty
		if (newPassword.isEmpty() && re_enterPassword.isEmpty())
		{
			txtFPNewPassword.setStyle(wrong_certificate);
			txtFPReenterPassword.setStyle(wrong_certificate);
			enterBothPasswordTooltip.setText("Please enter password.");
			txtFPNewPassword.setTooltip(enterBothPasswordTooltip);
			txtFPReenterPassword.setTooltip(enterBothPasswordTooltip);
		}
		else{
			
			// If the new password and the re-entered password does not match
			if (!re_enterPassword.equals(newPassword))
			{
				txtFPNewPassword.setStyle(wrong_certificate);
				txtFPReenterPassword.setStyle(wrong_certificate);
				matchPasswordTooltip.setText("Password does not match.");
				txtFPNewPassword.setTooltip(matchPasswordTooltip);
				txtFPReenterPassword.setTooltip(matchPasswordTooltip);
			}
			else
			{
				txtFPNewPassword.setStyle(correct_certificate);
				txtFPReenterPassword.setStyle(correct_certificate);
				matchPasswordTooltip.setText(null);
				txtFPNewPassword.setTooltip(matchPasswordTooltip);
				txtFPReenterPassword.setTooltip(matchPasswordTooltip);
			}
		}
		
		
		// If the email and both passwords are valid (i.e. all required information is accurate)
		if(successful == true)
		{
			lblFPSuccess.setVisible(true);
			
			txtFPNewPassword.setStyle(correct_certificate);
			txtFPReenterPassword.setStyle(correct_certificate);
			txtFPEmail.setStyle(correct_certificate);
			allDetailsValid.setText(null);
			txtFPNewPassword.setTooltip(allDetailsValid);
			txtFPReenterPassword.setTooltip(allDetailsValid);
			txtFPEmail.setTooltip(allDetailsValid);
			
			txtFPEmail.clear();
			txtFPNewPassword.clear();
			txtFPReenterPassword.clear();
		}
		else
		{
			// If the email field is empty
			if (email.isEmpty())
			{
				txtFPEmail.setStyle(wrong_certificate);
				enterEmailTooltip.setText("Please enter email address.");
				txtFPEmail.setTooltip(enterEmailTooltip);
			}
			else
			{
				// If the email is not empty
				if(!email.isEmpty())
				{
					txtFPEmail.setStyle(correct_certificate);
					wrongEmailTooltip.setText(null);
					txtFPEmail.setTooltip(wrongEmailTooltip);
					
					// If the new password and re-entered password does not match
					if(re_enterPassword.equals(newPassword))
					{
						// i.e. Email can not be found on the database
						txtFPEmail.setStyle(wrong_certificate);
						unrecognisedEmailTooltip.setText("Email can not be recognised.");
						txtFPEmail.setTooltip(unrecognisedEmailTooltip);
					}
				}
			}
		}
	
	}



}
