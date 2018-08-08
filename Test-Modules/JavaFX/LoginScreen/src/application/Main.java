package application;
	
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import com.tatva.service.LoginScreenService;


/**
 * 
 * @author pci94
 *	the application will be start from this class as it extends Application Class
 */

public class Main extends Application {
	
	TextField txtUserName;
	PasswordField passwordField;
	GridPane gridPane;
	
	LoginScreenService loginService=new LoginScreenService();
	
	
	/*
	 * (non-Javadoc)
	 * @see javafx.application.Application#start(javafx.stage.Stage)
	 * 
	 * Start Of Application
	 * Shows the first screen that is going to b displayed.
	 * 
	 */
	public void start(final Stage primaryStage) {
		try {
			
			BorderPane root1 = new BorderPane();
			Scene scene = new Scene(root1,400,400);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
			
	        ToolBar toolBar = new ToolBar();

	        Button btnLogin = new Button("Login");
	        Button btnExit = new Button("Exit");
	        Button btnList = new Button("List Users");
	        Button btnAdd = new Button("Create User");

	        toolBar.getItems().addAll(btnLogin,btnAdd, btnList , btnExit);
	      
	        root1.setTop(toolBar);
			
			primaryStage.setTitle("stage title");
			
			btnLogin.setOnAction(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent paramT) {

					primaryStage.close();
					loginService.loginScreen();
				}
			});
			
			btnExit.setOnAction(new EventHandler<ActionEvent>() {
				
				@Override
				public void handle(ActionEvent paramT) {

					System.out.println("Button2 Clicked");
					System.exit(0);
				}
			});
			
			btnList.setOnAction(new EventHandler<ActionEvent>() {
				
				@Override
				public void handle(ActionEvent event) {

					primaryStage.close();
					loginService.listUser();
				}
			});
			
			btnAdd.setOnAction(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent event) {

					primaryStage.close();
					loginService.insertUser();
				}
			});
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
