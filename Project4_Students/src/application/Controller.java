package application;

import java.io.IOException;
import business.ControllerInterface;
import business.LoginException;
import business.SystemController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import ui.Start;

public class Controller {
	@FXML
	private TextField tf;
	@FXML
	private PasswordField pf;
	@FXML
	private Button btn;
	private Text messageBar = new Text();
	public void clear() {
		messageBar.setText("");
	}
	public void submit(ActionEvent event) throws IOException {		
		try {
			String uname = tf.getText();
			String password = pf.getText();
			ControllerInterface c = new SystemController();
			c.login(uname.trim(), password.trim());
			Parent root = FXMLLoader.load(getClass().getResource("/application/MainWindow.fxml"));
			Scene scene = new Scene(root, 800, 800);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			Stage stg = new Stage();
			stg.setMaximized(true);
			stg.setTitle("Library System 1.0");
			stg.setScene(scene);
			stg.show();
		} catch(LoginException ex) {
			//OptionPane.
			messageBar.setFill(Start.Colors.red);
			messageBar.setText("Error! " + ex.getMessage());
		}
	}
}
