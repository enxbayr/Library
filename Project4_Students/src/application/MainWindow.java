package application;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import dataaccess.DataAccessFacade;
import business.Book;
import business.BookCopy;

public class MainWindow {
	
	private DataAccessFacade daf;
	private HashMap<String, Book> bookMap;
	
	@FXML private Tab tabCheckoutRecords;
	@FXML private Tab tabMembers;
	@FXML private Tab tabBooks;
	@FXML private Tab tabUsers;
	@FXML private TabPane tabPane;
	
	@FXML private Button btnBooks;
	@FXML private Button btnCheckoutRecords;
	@FXML private Button btnMembers;
	@FXML private Button btnUsers;
	@FXML private Button btnNew;
	@FXML private Button btnEdit;
	@FXML private Button btnDelete;
	
	
	@FXML private TableView<BookData> tvBook;
	@FXML private TableView<?> tvBookCopy;

	public void toggleTabs(ActionEvent event) {
		Button btn = ((Button) event.getSource());
		Tab[] tabs = {tabCheckoutRecords, tabMembers, tabBooks, tabUsers};
		//System.out.println(btn.getId());
		for (Tab t : tabs) {
			//System.out.println(t.getId());
			t.setDisable(true);
			if (t.getId().endsWith(btn.getId().substring(3)))
			{
				t.setDisable(false);
				tabPane.getSelectionModel().select(t);
			}
		}
	}
	
	public void listBooks(ActionEvent event) {
		System.out.println("LIST BOOK");
		tvBook = new TableView<>();
		
        TableColumn colBTitle = new TableColumn<>("Title");
        TableColumn colBAuth = new TableColumn<>("Author");
        TableColumn colBIsdn = new TableColumn<>("ISBN");
        TableColumn colBAvNm = new TableColumn<>("Number");
        
		
        colBTitle.setCellValueFactory(new PropertyValueFactory("title"));
        colBAuth.setCellValueFactory(new PropertyValueFactory("authors"));
        colBIsdn.setCellValueFactory(new PropertyValueFactory("isbn"));
        colBAvNm.setCellValueFactory(new PropertyValueFactory("number"));
        
		daf = new DataAccessFacade();
		bookMap = new HashMap<String, Book>();		
		bookMap = daf.readBooksMap();
		
		List<BookData> bdList = new ArrayList<BookData>();
		
		bookMap.forEach((s,b) -> bdList.add(new BookData(b)));
		
		bdList.forEach((e) -> System.out.println(e.getTitle()));
		
		tvBook.setEditable(false);
		tvBook.getColumns().clear();
		tvBook.getColumns().addAll(colBTitle, colBAuth, colBIsdn, colBAvNm);

        ObservableList<BookData> data = FXCollections.observableArrayList();
        
        data.addAll(bdList);
        
        tvBook.setItems(data);
	}
	
}
