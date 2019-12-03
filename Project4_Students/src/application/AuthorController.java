package application;

import business.Address;
import business.Author;
import business.BaseController;
import dataaccess.DataAccess;
import dataaccess.DataAccessFacade;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class AuthorController implements BaseController {

	private String recordID;
	@FXML
	private TextField txtAuthBio;
	@FXML
	private TextField txtAuthFName;
	@FXML
	private TextField txtAuthLName;
	@FXML
	private TextField txtAuthPhone;

	@FXML
	private TextField txtMemberAddrStr;
	@FXML
	private TextField txtMemberAddrCity;
	@FXML
	private TextField txtMemberAddrState;
	@FXML
	private TextField txtMemberAddrZip;

	@FXML
	private Button btnMemberCancel;
	@FXML
	private Button btnMemberSave;
	private DataAccess da;

	public void initData(String recordId) {
		this.recordID = recordId;
		initWindow();
	}

	public void initWindow() {
		if(recordID != null)
		{
			Author lm = da.readAuthorMap().get(recordID);
			txtAuthBio.setText(lm.getBio());
			txtAuthFName.setText(lm.getFirstName());
			txtAuthLName.setText(lm.getLastName());
			txtAuthPhone.setText(lm.getTelephone());
			Address addr = lm.getAddress();
			txtMemberAddrStr.setText(addr.getStreet());
			txtMemberAddrCity.setText(addr.getCity());
			txtMemberAddrState.setText(addr.getState());
			txtMemberAddrZip.setText(addr.getZip());
		}
	}

	public void initialize() {
		da = new DataAccessFacade();
	}

	public void saveMember(ActionEvent evt)
	{
		Author lm;
		boolean saved = false;
		Address addr = new Address(txtMemberAddrStr.getText(), txtMemberAddrCity.getText(), txtMemberAddrState.getText(), txtMemberAddrState.getText());
		if(recordID == null)
		{
			lm = new Author(txtAuthFName.getText(), txtAuthLName.getText(), txtAuthPhone.getText(), addr, txtAuthBio.getText());
			da.saveNewAuthor(lm);
			saved = true;
		}
		else
		{
			lm = da.readAuthorMap().get(recordID);
			lm.setAddress(addr);
			lm.setFirstName(txtAuthFName.getText());
			lm.setLastName(txtAuthLName.getText());
			lm.setTelephone(txtAuthPhone.getText());
			da.saveNewAuthor(lm);
			saved = true;
		}
		if(saved)
		{
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Info");
			alert.setContentText("Successfully saved.");
			alert.showAndWait().ifPresent(rs -> {
				if (rs == ButtonType.OK) {
					close(evt);
					System.out.println("Pressed OK.");
				}
			});
		}
	}
	public void close(ActionEvent evt)
	{
		Button closeButton = (Button)evt.getSource();
		Stage stage = (Stage) closeButton.getScene().getWindow();
	    // do what you have to do
	    stage.close();
	}
}
