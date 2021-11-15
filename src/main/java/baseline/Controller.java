/*
 * UCF COP3330 Fall 2021 Application Assignment 2 Solution
 * Copyright 2021 Joshua Samontanez
 */

package baseline;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.css.PseudoClass;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML
    private Button addButton;

    @FXML
    private Button clearButton;

    @FXML
    private TextField itemNameTextField;

    @FXML
    private TableColumn<InventoryModel, String> itemSerialNumber;

    @FXML
    private TableColumn<InventoryModel, String> itemValue;

    @FXML
    private TableColumn<InventoryModel, String> itemName;

    @FXML
    private TableView<InventoryModel> table;

    @FXML
    private TextField itemSerialNumberTextField;

    @FXML
    private TextField itemValueTextField;

    @FXML
    private MenuItem loadButton;

    @FXML
    private MenuItem saveButton;

    @FXML
    private Button removeButton;

    @FXML
    private TextField searchField;

    private ObservableList<InventoryModel> itemList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        /*
        Shall #8, #9, #10 is already handled by the table view
        The table view can sort the data based on the item name, item value, and serial number
         */

        // These will not allow user to resize the columns
        itemValue.setResizable(false);
        itemSerialNumber.setResizable(false);
        itemName.setResizable(false);

        // This will determine which data should be stored into what column
        cellPropertyValue();
        // This will allow the table to be editable when double-clicked by the user
        editTableContents();
        // This will check if the user input is valid, if it is not valid an error will appear
        textFieldListener();
        // This will be used for searching an item
        searchItem();
        // This will remove an item from the list, if the remove button is clicked
        removeItem();
        // Whenever an existing data was edited by the user, this will check if It's valid or not, if the input is invalid
        // it will show an error
        warningHandler();

        // This will help sort the value either ascending or descending order
        itemValue.setComparator(new CustomComparator());
    }

    @FXML
    void addItem(MouseEvent event) {
        // Satisfies: shall #1, #1.1, #1.2, #1.3, #2
        if(event.getSource() == addButton){
            UpdateInventory inventory = new UpdateInventory();

            // These will convert all the inputs from the all the text fields into a string
            String item = itemNameTextField.getText();
            String itemValue = itemValueTextField.getText();
            String itemSerial = itemSerialNumberTextField.getText();

            // If there are no duplicate names or serial number, it will remove the inputs from all the text field
            // But if a duplicate item was found, it will not remove the inputs, so it can be easily edited by the user
            if(!itemList.contains(new InventoryModel(item, itemValue, itemSerial))){
                itemNameTextField.setText("");
                itemValueTextField.setText("");
                itemSerialNumberTextField.setText("");
            }

            // This will call the add item method and will handle adding new items into the list
            // Assuming there are no warnings and the inputs passed the requirements
            itemList = inventory.addItem(item, itemValue, itemSerial, itemList);

            // The newly added items will be added into the table
            table.setItems(itemList);
        }
    }

    @FXML
    void clearItems(MouseEvent event) {
        // Satisfies shall #4
        if(event.getSource() == clearButton){
            UpdateInventory inventory = new UpdateInventory();
            // This will clear all the items inside the table
            table.getItems().clear();
            // This will clear all the items inside the observable list
            itemList = inventory.clearInventory(itemList);
        }
    }


    @FXML
    void load(ActionEvent event) throws IOException {
        // This method is for loading a file and showing it into the table
        if (event.getSource() == loadButton) {
            // This will clear all the inputs from the table


            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Load");

            List<InventoryModel> inventory = new ArrayList<>();
            java.io.File selectedFile = fileChooser.showOpenDialog(new Stage());
            java.io.File file = new java.io.File(selectedFile.getAbsolutePath());

            // This stores the info about the file, its path and name
            String fileName = selectedFile.toString();
            // This will only store the extension of the file
            String extension = fileName.split("\\.")[1];

            LoadData load = new LoadData();

            // If the file extension is in "txt"
            if(extension.matches("txt")){
                // It will load the text file, and store the data into the itemList
                itemList = load.loadTxt(file, inventory, itemList);
                // It will then show it on the table
                table.setItems(itemList);
            }

            // If the file extension is in "html
            if(extension.matches("html")){
                // It will load the html file, and scan the data so it can store it into the itemList
                itemList = load.loadHtml(file, inventory, itemList);
                // It will then show it on the table
                table.setItems(itemList);
            }

            // If the file extension is in "json"
            if(extension.matches("json")){
                // This will parse the file, and store the data into the itemList
                itemList = load.loadJson(file, inventory, itemList);
                // This will show the data from the itemList into the table
                table.setItems(itemList);
            }
            table.getItems().clear();

        }
    }

    @FXML
    void save(ActionEvent event) throws IOException {
        // This method is for saving the table data into a file
        if(event.getSource() == saveButton){
            FileChooser fileChooser = new FileChooser();
            // Told file chooser which file extensions can I save a file
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text", "*.txt"));
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("HTML", "*.html"));
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON", "*.json"));

            fileChooser.setTitle("Save");
            // This will store the file name and its path
            java.io.File fileName = fileChooser.showSaveDialog(new Stage());
            // This will store the extension of the file that is chosen by the user
            String extension = fileChooser.selectedExtensionFilterProperty().get().getExtensions().get(0).substring(1);

            SaveData save = new SaveData();

            // If the extension matches "txt"
            if(extension.matches(".txt")){
                System.out.println("\n");
                // It will save all the data from the table into a text file
                String result = save.saveTxt(fileName, itemList);
                // This will just show what is printed into the file
                System.out.println(result);
            }

            // If the extension matches html
            if(extension.matches(".html")){
                System.out.println("\n");
                // It will save all the data from the table into a html file
                String result = save.saveHtml(fileName, itemList);
                // This will just show what is printed into the file
                System.out.println(result);
            }

            // If the extension matches "json"
            if(extension.matches(".json")){
                System.out.println("\n");
                // It will save all the data from the table into a json file
                String result = save.saveJson(fileName, itemList);
                // This will just show what is printed into the file
                System.out.println(result);
            }
        }
    }

    // This method will determine which data will be put into a column
    private void cellPropertyValue(){
        // The item name should be in the item name column
        itemName.setCellValueFactory(
                new PropertyValueFactory<>("itemName")
        );
        // The item value should be in the value column
        itemValue.setCellValueFactory(
                new PropertyValueFactory<>("value")
        );
        // The item serial number should be in the serial number column
        itemSerialNumber.setCellValueFactory(
                new PropertyValueFactory<>("itemSerialNumber")
        );
    }

    private void warningHandler(){
        // This method is for showing all the warnings if the user input doesn't satisfy a requirement
        String invalid = "Invalid Value!";

        // These methods, will handle which warnings to show the user, or not
        editItemNameWarnings(invalid);
        editValueWarnings(invalid);
        serialTextFieldWarnings(invalid);
    }

    // This method satisfies: shall #7.1
    private void editItemNameWarnings(String invalid){
        /*
        The program will show warnings if the item name is:
        - Less than 2 or more than 256 characters
        - The item already exist
         */

        // This is a listener for any change made from the item name column
        itemName.setOnEditCommit(event -> {
            // It will store the new value into the 'newValue' string
            String newValue = event.getNewValue();

            // It will then check if it passes all the requirements listed above
            if(Objects.equals(newValue, "") ||
                    newValue.length() < 2 || newValue.length() > 256){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle(invalid);
                alert.setContentText("An item name must be 2 - 256 characters inclusive.");

                alert.showAndWait();
            }

            if(itemList.contains(new InventoryModel(newValue, null, null))){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Item already exist!");
                alert.setContentText("An item name and serial number must be unique.");

                alert.showAndWait();
            }

            // If it passed all the requirements, the new value will appear in the table
            else{
                event.getRowValue().setItemName(newValue);
            }

            // This is for fixing a bug where, the new value doesn't appear correctly
            itemName.setVisible(false);
            itemName.setVisible(true);
        });
    }

    // This method satisfies: shall #5.1
    private void editValueWarnings(String invalid){
        /*
        This method will show warning if:
        - A new value is empty
        - The value is not in digits greater or equal than 0
         */

        // This is a listener for any change made from the item value column
        itemValue.setOnEditCommit(event -> {
            // The new value will be stored in this string
            String newValue = event.getNewValue();

            // It will then check if it passes the requirements listed above. If not, it will show a warning
            if(Objects.equals(newValue, "")){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle(invalid);
                alert.setContentText("Item value is empty.");

                alert.showAndWait();

            }

            // If the input passed all the requirements, it will now be added into the table
            if(newValue.matches("[0-9]+")){
                System.out.println("Input valid");
                event.getRowValue().setValue(newValue);
            }

            else{
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle(invalid);
                alert.setContentText("An item must be a valid digit.");

                alert.showAndWait();
            }

            // This is for fixing a bug where, the new value doesn't appear correctly
            itemValue.setVisible(false);
            itemValue.setVisible(true);
        });
    }

    // This method satisfies shall #6.1
    private void serialTextFieldWarnings(String invalid){
        /*
        This method will show warning if:
        - A new serial number is empty
        - The serial number already exist
        - The serial number doesn't follow the format (A-XXX-XXX-XXX)
         */

        // This is a listener for any change made from the item serial number column
        itemSerialNumber.setOnEditCommit(event -> {
            // It will store the value into this string
            String newValue = event.getNewValue();

            // It will then check if the new value passes all the requirements. If not, it will show an error
            if (Objects.equals(newValue, "")) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle(invalid);
                alert.setContentText("Serial number is empty.");

                alert.showAndWait();
            }

            if(itemList.contains(new InventoryModel(null, null, newValue))){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Item already exist!");
                alert.setContentText("An item name and serial number must be unique.");

                alert.showAndWait();
            }

            // If the input passes all the requirements, it will update the table
            else{
                event.getRowValue().setItemSerialNumber(newValue);
            }

            if(newValue.matches("[A-Za-z]+-[A-Za-z0-9]{1,3}-[A-Za-z0-9]{1,3}-[A-Za-z0-9]{1,3}")){
                System.out.println("Serial Valid");
            }

            else{
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle(invalid);
                alert.setContentText("A serial number must follow this format A-XXX-XXX-XXX.");

                alert.showAndWait();
            }

            // This is for fixing a bug where, the new value doesn't appear correctly
            itemSerialNumber.setVisible(false);
            itemSerialNumber.setVisible(true);
        });
    }

    // Satisfies shall #3
    private void removeItem(){
        // This will automatically remove an item from the table and on the observable list
        removeButton.setOnAction(e -> {
            InventoryModel selectedItem = table.getSelectionModel().getSelectedItem();
            table.getItems().remove(selectedItem);
            itemList.remove(selectedItem);
        });
    }

    // This method satisfies shall #5, #6, #7
    private void editTableContents() {
        /*
        This allows user to edit the value of an existing inventory item.
        It can edit the:
        - Item Name
        - Item Value
        - Item Serial Number
         */

        itemName.setCellFactory(TextFieldTableCell.forTableColumn());

        itemName.setOnEditCommit(e ->
                e.getTableView().getItems().get(e.getTablePosition().getRow()).setItemName(e.getNewValue()));

        itemValue.setCellFactory(TextFieldTableCell.forTableColumn());

        itemValue.setOnEditCommit(e ->
                e.getTableView().getItems().get(e.getTablePosition().getRow()).setValue(e.getNewValue()));

        itemSerialNumber.setCellFactory(TextFieldTableCell.forTableColumn());

        itemSerialNumber.setOnEditCommit(e ->
                e.getTableView().getItems().get(e.getTablePosition().getRow()).setItemSerialNumber(e.getNewValue()));

        // Make the table data editable
        table.setEditable(true);
    }


    // This method will check the inputs on the text fields whenever the user clicked the add button
    private void textFieldListener() {
        addButton.setOnAction(event -> {
            String invalid = "Invalid Value!";

            // If the item name input is empty or less than 2 or more than 256 characters, it will show this error
            if(Objects.equals(itemNameTextField.getText(), "") ||
                    itemNameTextField.getText().length() < 2 || itemNameTextField.getText().length() > 256){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle(invalid);
                alert.setContentText("An item name must be 2 - 256 characters inclusive.");

                alert.showAndWait();
            }

            // If the user did not have any input for the value, it wil show this error
            else if (Objects.equals(itemValueTextField.getText(), "")) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle(invalid);
                alert.setContentText("Item value is empty.");

                alert.showAndWait();
            }

            // If the serial number text field is empty, it will show an error
            else if (Objects.equals(itemSerialNumberTextField.getText(), "")) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle(invalid);
                alert.setContentText("Serial number is empty.");

                alert.showAndWait();
            }

            // If the input value is in digits and more than or equal to 0, it will just print "input valid"
            if(itemValueTextField.getText().matches("[0-9]+")){
                System.out.println("Input valid.");
            }

            // If the user did not put a digit more than or equal to 0 in the value text field, it will show this error
            else{
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle(invalid);
                alert.setContentText("An item must be a valid digit.");

                alert.showAndWait();
            }

            // If the user followed the serial number format, it will not have an error
            if(itemSerialNumberTextField.getText().matches("[A-Za-z]+-[A-Za-z0-9]{1,3}-[A-Za-z0-9]{1,3}-[A-Za-z0-9]{1,3}")){
                System.out.println("Serial Valid.");
            }

            // But if the user did not follow the format for serial number, an error will appear
            else{
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle(invalid);
                alert.setContentText("A serial number must follow this format A-XXX-XXX-XXX.");

                alert.showAndWait();
            }
        });

    }

    // This method is for filtering and searching a data from the list
    private void searchItem() {
        FilteredList<InventoryModel> filteredData = new FilteredList<>(FXCollections.observableList(itemList));
        table.setItems(filteredData);

        table.setRowFactory(tableView -> {
            TableRow<InventoryModel> row = new TableRow<>();
            row.pseudoClassStateChanged(PseudoClass.getPseudoClass("highlighted"), false);
            row.itemProperty().addListener((obs, oldOrder, newOrder) -> {
                boolean assignClass = filteredData.contains(newOrder);
                row.pseudoClassStateChanged(PseudoClass.getPseudoClass("highlighted"), assignClass);
            });
            return row;
        });

        Search searchItem = new Search();
        // Whenever the search text field is changed, this action will be activated, and will call the helper method
        searchField.textProperty().addListener((observable, oldValue, newValue) ->
                table.setItems(searchItem.filterList(itemList, newValue.toLowerCase()))
        );
    }

}
