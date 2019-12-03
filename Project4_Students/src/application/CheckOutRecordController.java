package application;

import java.time.LocalDate;
import java.time.Period;
import java.util.HashMap;
import java.util.Map;

import business.BaseController;
import business.Book;
import business.CheckOutEntry;
import business.LibraryMember;
import dataaccess.DataAccess;
import dataaccess.DataAccessFacade;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class CheckOutRecordController implements BaseController{
	@FXML
	private ComboBox<Book> checkOutBook;
	@FXML
	private ComboBox<LibraryMember> checkOutMember;
	@FXML
	private TextField checkOutAvailable;
	@FXML
	private DatePicker chechOutDueDate;
	@FXML
	private Button checkOutSave;
	@FXML
	private Button checkOutCancel;
	@FXML
	private DatePicker dtReturnDate;
	@FXML
	private Label lblreturnDate;
	@FXML
	private Label lblFineAmount;
	@FXML
	private TextField txtFineAmount;
	private String recordId;
	DataAccess da = new DataAccessFacade();
	private CheckOutEntry chout = null;

	public void initData(String recordId) {
		this.recordId = recordId;
		initWindow();
	}

	@FXML
	public void initialize() {
		HashMap<String, Book> books = da.readBooksMap();
		// HashMap<String, CheckOutEntry> records = da.readCheckoutRecordMap();
		txtFineAmount.setDisable(true);
		for (Map.Entry<String, Book> e : books.entrySet()) {
			checkOutBook.getItems().add(e.getValue());
		}

		HashMap<String, LibraryMember> members = da.readMemberMap();
		for (Map.Entry<String, LibraryMember> e : members.entrySet()) {
			checkOutMember.getItems().add(e.getValue());
		}

		checkOutBook.valueProperty().addListener(new ChangeListener<Book>() {
			@Override
			public void changed(ObservableValue<? extends Book> observable, Book oldValue, Book newValue) {
				checkOutAvailable.setText(String.valueOf(newValue.isAvailable()));
				LocalDate dueDate = LocalDate.now().plusDays(newValue.getMaxCheckoutLength());
				chechOutDueDate.setValue(dueDate);
			}
		});

		dtReturnDate.valueProperty().addListener(new ChangeListener<LocalDate>() {
			@Override
			public void changed(ObservableValue<? extends LocalDate> observable, LocalDate oldValue,
					LocalDate newValue) {
				int days = Period.between(chechOutDueDate.getValue(), newValue).getDays();
				if (days > 0) {
					lblFineAmount.setVisible(true);
					txtFineAmount.setVisible(true);
					txtFineAmount.setDisable(false);
					txtFineAmount.setText(String.valueOf(days * CheckOutEntry.FINE_PER_DAY));
				}
				else
				{
					txtFineAmount.setDisable(true);
					txtFineAmount.setText("0");
				}
			}
		});
	}

	private void initWindow() {
		if (recordId != null) {
			HashMap<String, CheckOutEntry> records = da.readCheckoutRecordMap();
			if (records.containsKey(recordId))
				chout = records.get(recordId);
			if (chout != null) {
				checkOutBook.getSelectionModel().select(chout.getBook());
				checkOutMember.getSelectionModel().select(chout.getMember());
			}
			checkOutBook.setDisable(true);
			checkOutMember.setDisable(true);
			lblreturnDate.setVisible(true);
			chechOutDueDate.setDisable(true);
			dtReturnDate.setVisible(true);
			lblFineAmount.setVisible(true);
			txtFineAmount.setVisible(true);
		}
	}

	public void SaveCheckOut(ActionEvent evt) {
		// return book
		boolean saved = false;
		DataAccess da = new DataAccessFacade();
		CheckOutEntry entry = null;
		if (recordId != null) {
			chout.setHasReturned(true);
			chout.setFineAmount(Double.valueOf(txtFineAmount.getText()));
			chout.setReturnedDate(dtReturnDate.getValue());
			da.saveNewCheckoutRecord(chout);
			Book rbook = chout.getBook();
			rbook.getCopy(chout.getCopy().getCopyNum()).changeAvailability();
			da.saveBook(rbook);
			saved = true;
		} else {
			Book b = (Book) checkOutBook.getSelectionModel().getSelectedItem();
			LibraryMember member = (LibraryMember) checkOutMember.getSelectionModel().getSelectedItem();
			if (checkOutAvailable.getText().equals("true")) {
				entry = new CheckOutEntry(b, b.getNextAvailableCopy(), member, LocalDate.now(), null, null);
				da.saveNewCheckoutRecord(entry);
				da.saveBook(b);
				saved = true;
				checkOutBook.getSelectionModel().select(null);
				// HashMap<String, Book> bs = da.readBooksMap();
			} else if (checkOutAvailable.getText().equals("false")) {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Info");
				alert.setHeaderText("Insuficient copy");
				alert.setContentText("No copy of this book is available yet.");
				alert.showAndWait().ifPresent(rs -> {
					if (rs == ButtonType.OK) {
						System.out.println("Pressed OK.");
					}
				});
			}
		}
		if (saved) {
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
