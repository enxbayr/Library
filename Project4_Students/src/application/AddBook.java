package application;

import dataaccess.DataAccess;
import dataaccess.DataAccessFacade;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import business.Author;
import business.BaseController;
import business.Book;

public class AddBook extends Stage implements BaseController {
	@FXML
	private TextField tfNewBookTitle;
	@FXML
	private TextField tfNewBookIsdn;
	@FXML
	private RadioButton rb21;
	@FXML
	private RadioButton rb7;
	@FXML
	private ToggleGroup rbGroup;
	@FXML
	private ListView<Author> lvNewBookAuthors;
	@FXML
	private Button btnLoadAuthor;
	@FXML
	private Button btnAddNewBook;
	@FXML
	private Button btnNewBookCancel;

	public void initial() {

		rbGroup = new ToggleGroup();
		rb21 = new RadioButton("21 Days");
		rb21.setToggleGroup(rbGroup);
		rb21.setSelected(true);

		rb7 = new RadioButton("21 Days");
		rb7.setToggleGroup(rbGroup);

		lvNewBookAuthors.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
	}

	String isbn = null;

	public void initData(String isbn) {
		this.isbn = isbn;
		if (this.isbn != null) {
			DataAccess daf = new DataAccessFacade();
			Book book = daf.readBooksMap().get(isbn);

			tfNewBookTitle.setText(book.getTitle());
			tfNewBookIsdn.setText(book.getIsbn());
			rb21.setSelected((book.getMaxCheckoutLength() == 21) ? true : false);
			ObservableList<Author> authors = FXCollections.observableArrayList();
			book.getAuthors().forEach((a) -> authors.add(a));
			lvNewBookAuthors.setItems(authors);
			btnAddNewBook.setText("Book edit");
		}
	}

	public void loadAuthor(ActionEvent event) throws IOException {

		DataAccess daf = new DataAccessFacade();
		HashMap<String, Author> hmAuthors = daf.readAuthorMap();
		ObservableList<Author> authors = FXCollections.observableArrayList();
		hmAuthors.forEach((s, a) -> authors.add(a));

		lvNewBookAuthors.setItems(authors);
		lvNewBookAuthors.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
	}

	public void addBook(ActionEvent event) throws IOException {
		DataAccess daf = new DataAccessFacade();
		boolean isSelected = rb21.isSelected();
		int maxCheck = (isSelected) ? 21 : 7;
		List<Author> selectedAuthors = new ArrayList<Author>();
		ObservableList<Author> selectedItems = lvNewBookAuthors.getSelectionModel().getSelectedItems();
		selectedAuthors.addAll(selectedItems);

		if (this.isbn != null) {
			Book book = daf.readBooksMap().get(isbn);
			book.setTitle(tfNewBookTitle.getText());
			book.setIsbn(tfNewBookIsdn.getText());
			book.setMaxDay(maxCheck);
			book.setAuthors(selectedAuthors);
			daf.updateBook(book);
		} else {

			String title = tfNewBookTitle.getText();
			String isbn = tfNewBookIsdn.getText();

			Book newBook = new Book(isbn, title, maxCheck, selectedAuthors);

			daf.saveNewBook(newBook);
		}
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/MainWindow.fxml"));
		MainWindow controller = loader.<MainWindow>getController();
		controller.reloadBook();
	}

	public void closeWindow(ActionEvent evt) {
		btnNewBookCancel = (Button) evt.getSource();
		Stage stage = (Stage) btnNewBookCancel.getScene().getWindow();
		// do what you have to do
		stage.close();
	}
}
