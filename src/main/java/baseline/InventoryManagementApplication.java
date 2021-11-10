/*
 * UCF COP3330 Fall 2021 Application Assignment 2 Solution
 * Copyright 2021 first_name last_name
 */

package baseline;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class InventoryManagementApplication extends Application {
    @Override
    public void start(Stage stage) {
        try{
            // load the fxml file
            FXMLLoader fxmlLoader = new FXMLLoader(InventoryManagementApplication.class.getResource("InventoryManagement.fxml"));
            // create a new scene
            Scene scene = new Scene(fxmlLoader.load());
            // the window cannot be resized
            stage.setResizable(false);

            // title of the application
            stage.setTitle("Inventory Management");
            stage.setScene(scene);
            stage.show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch();
    }
}