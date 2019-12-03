package application;

import business.Address;
import business.BaseController;
import business.LibraryMember;
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

public class MemberController implements BaseController{
	public static int memberId = 0;
	@FXML
	private TextField txtMemberID;
	@FXML
	private TextField txtMemberFName;
	@FXML
	private TextField txtMemberLName;
	@FXML
	private TextField txtMemberPhone;

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
	private String MemberID;

	public void initData(String MemberID) {
		this.MemberID = MemberID;
		initWindow();
	}

	public void initWindow() {
		if (MemberID == null) {
			txtMemberID.setText(String.valueOf(++memberId));
		} else {
			LibraryMember lm = da.readMemberMap().get(MemberID);
			txtMemberID.setText(String.valueOf(MemberID));
			txtMemberFName.setText(lm.getFirstName());
			txtMemberLName.setText(lm.getLastName());
			txtMemberPhone.setText(lm.getTelephone());
			Address addr = lm.getAddress();
			txtMemberAddrStr.setText(addr.getStreet());
			txtMemberAddrCity.setText(addr.getCity());
			txtMemberAddrState.setText(addr.getState());
			txtMemberAddrZip.setText(addr.getZip());
		}
	}

	public void initialize() {
		da = new DataAccessFacade();
		int mid = 1;
		for(LibraryMember l: da.readMemberMap().values())
			if(Integer.parseInt(l.getMemberId()) > mid)
					mid = Integer.valueOf(l.getMemberId());
		memberId = mid;
	}

	public void saveMember(ActionEvent evt)
	{
		LibraryMember lm;
		boolean saved = false;
		Address addr = new Address(txtMemberAddrStr.getText(), txtMemberAddrCity.getText(), txtMemberAddrState.getText(), txtMemberAddrState.getText());
		if(MemberID == null)
		{
			lm = new LibraryMember(String.valueOf(++memberId), txtMemberFName.getText(), txtMemberLName.getText(), txtMemberPhone.getText(), addr);
			da.saveNewMember(lm);
			saved = true;
		}
		else
		{
			lm = da.readMemberMap().get(MemberID);
			lm.setAddress(addr);
			lm.setFirstName(txtMemberFName.getText());
			lm.setLastName(txtMemberLName.getText());
			lm.setTelephone(txtMemberPhone.getText());
			da.saveNewMember(lm);
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
