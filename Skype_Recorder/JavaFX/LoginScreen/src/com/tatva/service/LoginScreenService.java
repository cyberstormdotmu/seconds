package com.tatva.service;

import java.util.List;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.ToolBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import application.Main;

import com.tatva.dao.ILoginScreenDao;
import com.tatva.dao.LoginScreenDaoImpl;
import com.tatva.model.UserModel;


/**
 * Service Class that will Change the screen as per user choice and call to the Dao class for database operations
 * @author pci94
 *
 */
public class LoginScreenService {

	TextField txtUserName;
	PasswordField passwordField;
	GridPane gridPane;
	
	ILoginScreenDao loginScreen=new LoginScreenDaoImpl();
	
	/**
	 *  This will display the login screen when user clicks the login button
	 */
	public void loginScreen(){
		
			BorderPane root1 = new BorderPane();
		 	ToolBar toolBar = new ToolBar();

		 	Button btnBack = new Button("Home");

	        toolBar.getItems().addAll(btnBack);
	      
	        root1.setTop(toolBar);
			
			System.out.println("inside loginScreen");
			
			final Stage stage=new Stage();
			stage.setTitle("Login Screen");
	
			gridPane=new GridPane();
			gridPane.setAlignment(Pos.CENTER);
			gridPane.setHgap(10);
			gridPane.setVgap(10);
			
			Label lblUserName=new Label("User Name:");
			Label lblPassword=new Label("Password:");
			
			txtUserName=new TextField();
			passwordField=new PasswordField();
			Button login=new Button("Login");
			
			gridPane.add(lblUserName, 0, 1);
			gridPane.add(lblPassword, 0, 2);
			gridPane.add(txtUserName, 1, 1);
			gridPane.add(passwordField, 1, 2);
			gridPane.add(login, 0, 4);
			
			Scene scene=new Scene(root1,400,400);
			root1.setCenter(gridPane);
			scene.getStylesheets().add(getClass().getResource("../../../application/application.css").toExternalForm());
			
			login.setOnAction(new EventHandler<ActionEvent>() {
	
				@Override
				public void handle(ActionEvent paramT) {
					
					String userName=txtUserName.getText();
					String password=passwordField.getText();
					
					if(loginScreen.authenticate(userName, password)){
						stage.close();
						successPage();
					}else{
						Label errorMessage=new Label("Wrong User Id or Password");
						errorMessage.setTextFill(Color.RED);
						gridPane.add(errorMessage, 0, 3);
					}
				}
			});
			stage.setScene(scene);
			stage.show();
			
			btnBack.setOnAction(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent paramT) {
					// TODO Auto-generated method stub
					System.out.println("Back Method");
					stage.close();
					back();
				}
			});
			
			
		}

	/**
	 *  If User authenticate he/she will be redirected to this page
	 */
	public void successPage(){
		
		BorderPane root1 = new BorderPane();
	 	ToolBar toolBar = new ToolBar();

	 	Button btnBack = new Button("Home");

        toolBar.getItems().addAll(btnBack);
      
        root1.setTop(toolBar);
	
		System.out.println("inside successpage");
		Label label=new Label("You Are Logged in");
		label.setFont(new Font(25));
		Scene scene=new Scene(root1,400,400);
		root1.setCenter(label);
		scene.getStylesheets().add(getClass().getResource("../../../application/application.css").toExternalForm());
		final Stage stage=new Stage();
		stage.setScene(scene);
		stage.show();
		
		btnBack.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent paramT) {
				// TODO Auto-generated method stub
				stage.close();
				back();
			}
		});
	}
	
	/**
	 *  List the user when user clicks the list user button
	 */
	public void listUser() {
		
		
		BorderPane root1 = new BorderPane();
	 	ToolBar toolBar = new ToolBar();

        Button btnBack = new Button("Home");

        toolBar.getItems().addAll(btnBack);
      
        root1.setTop(toolBar);
        
		System.out.println("Inside listUser");
		List<UserModel> list=loginScreen.listUsers();
		
		gridPane=new GridPane();
		gridPane.setAlignment(Pos.TOP_LEFT);
		gridPane.setHgap(10);
		gridPane.setVgap(10);
		
		Scene scene=new Scene(root1,400,400);
		root1.setCenter(gridPane);
		scene.getStylesheets().add(getClass().getResource("../../../application/application.css").toExternalForm());
		final Stage stage=new Stage();
		stage.setScene(scene);
		stage.show();
		
		Label lblId=new Label("User Id");
		Label lblFirstName=new Label("First Name");
		Label lblLastName=new Label("Last Name");
		Label lblUserName=new Label("User Name");
		Label lblEdit=new Label("Edit");
		Label lblDelete=new Label("Delete");
		
		gridPane.add(lblId, 0, 0);
		gridPane.add(lblFirstName, 1, 0);
		gridPane.add(lblLastName, 2, 0);
		gridPane.add(lblUserName, 3, 0);
		gridPane.add(lblEdit, 4, 0);
		gridPane.add(lblDelete,5, 0);
		
		for(int i=0;i<list.size();i++){
			final int id=list.get(i).getId();
			gridPane.add(new Label(String.valueOf(list.get(i).getId())), 0, i+1);
			gridPane.add(new Label(list.get(i).getFirstName()), 1, i+1);
			gridPane.add(new Label(list.get(i).getLastName()), 2, i+1);
			gridPane.add(new Label(list.get(i).getUserName()), 3, i+1);
			
			ImageView editView=new ImageView();
			Image editImage=new Image("edit.png");
			editView.setImage(editImage);
			gridPane.add(editView, 4, i+1);
			
			editView.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

				@Override
				public void handle(MouseEvent paramT) {

					stage.close();
					editUser(id);
					
				}
			});
			
			ImageView deleteView=new ImageView();
			Image deleteImage=new Image("delete.png");
			deleteView.setImage(deleteImage);
			gridPane.add(deleteView, 5, i+1);
			
			deleteView.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

				@Override
				public void handle(MouseEvent paramT) {

					stage.close();
					loginScreen.deleteUser(id);
					listUser();
				}
			});
		}
		
		
		 btnBack.setOnAction(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent paramT) {
					// TODO Auto-generated method stub
					System.out.println("Back Method");
					stage.close();
					back();
				}
			});
	}
	
	
	/**
	 *  when user click the edit link this Screen will be opened
	 * @param id
	 */
	public void editUser(int id){
		
		BorderPane root1 = new BorderPane();
	 	ToolBar toolBar = new ToolBar();

	 	Button btnBack = new Button("Home");

        toolBar.getItems().addAll(btnBack);
      
        root1.setTop(toolBar);
			
			final UserModel model=loginScreen.editUser(id);
			if(model!=null){
				
				gridPane=new GridPane();
				gridPane.setAlignment(Pos.TOP_LEFT);
				gridPane.setHgap(10);
				gridPane.setVgap(10);
				
				Scene scene=new Scene(root1,400,400);
				root1.setCenter(gridPane);
				scene.getStylesheets().add(getClass().getResource("../../../application/application.css").toExternalForm());
				final Stage stage=new Stage();
				stage.setScene(scene);
				stage.show();
				
				Label lblFirstName=new Label("First Name");
				Label lblLastName=new Label("Last Name");
				
				gridPane.add(lblFirstName, 0, 1);
				gridPane.add(lblLastName, 0, 2);
				
				final TextField txtFirstName=new TextField(model.getFirstName());
				final TextField txtLastName=new TextField(model.getLastName());
				
				gridPane.add(txtFirstName, 1, 1);
				gridPane.add(txtLastName, 1, 2);
				
				Button update=new Button("Update");
				gridPane.add(update, 0, 3);
				
				update.setOnAction(new EventHandler<ActionEvent>() {
					
					@Override
					public void handle(ActionEvent paramT) {
	
						String firstName=txtFirstName.getText();
						String lastName=txtLastName.getText();
						model.setFirstName(firstName);
						model.setLastName(lastName);
						
						loginScreen.updateUser(model);
						stage.close();
						listUser();
						
					}
				});
			
				btnBack.setOnAction(new EventHandler<ActionEvent>() {

					@Override
					public void handle(ActionEvent paramT) {
						// TODO Auto-generated method stub
						System.out.println("Back Method");
						stage.close();
						back();
					}
				});
			}
			
		}
		
	/**
	 * when user clicks create user button this screen will be called
	 */
	public void insertUser(){
		
		BorderPane root1 = new BorderPane();
	 	ToolBar toolBar = new ToolBar();

	 	Button btnBack = new Button("Home");

        toolBar.getItems().addAll(btnBack);
      
        root1.setTop(toolBar);
		
		gridPane=new GridPane();
		gridPane.setAlignment(Pos.TOP_LEFT);
		gridPane.setHgap(10);
		gridPane.setVgap(10);
		
		Scene scene=new Scene(root1,400,400);
		root1.setCenter(gridPane);
		scene.getStylesheets().add(getClass().getResource("../../../application/application.css").toExternalForm());
		final Stage stage=new Stage();
		stage.setScene(scene);
		stage.show();

		Label lblFirstName=new Label("First Name");
		Label lblLastName=new Label("Last Name");
		Label lblUserName=new Label("User Name");
		Label lblPassword=new Label("Password");
		
		final TextField txtFirstName=new TextField();
		final TextField txtLastName=new TextField();
		final TextField txtUserName=new TextField();
		final PasswordField passwordField=new PasswordField();
		
		Button create=new Button("Create User");

		gridPane.add(lblFirstName, 0, 1);
		gridPane.add(lblLastName, 0, 2);
		gridPane.add(lblUserName, 0, 3);
		gridPane.add(lblPassword, 0, 4);
		
		gridPane.add(txtFirstName, 1, 1);
		gridPane.add(txtLastName, 1, 2);
		gridPane.add(txtUserName, 1, 3);
		gridPane.add(passwordField, 1, 4);
		
		gridPane.add(create, 0, 5);
		
		create.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {

				UserModel model=new UserModel();
				model.setFirstName(txtFirstName.getText());
				model.setLastName(txtLastName.getText());
				model.setUserName(txtUserName.getText());
				model.setPassword(passwordField.getText());
				
				loginScreen.insertUser(model);
				stage.close();
				listUser();
			}
		});
		
		btnBack.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent paramT) {
				// TODO Auto-generated method stub
				System.out.println("Back Method");
				stage.close();
				back();
			}
		});
		
	}
	
	public void back(){
		Main main=new Main();
		Stage stage=new Stage();
		main.start(stage);
	}

}
