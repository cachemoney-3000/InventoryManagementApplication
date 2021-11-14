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

        cellPropertyValue();
        editTableContents();
        textFieldListener();
        searchItem();
        removeItem();
        warningHandler();
    }

    @FXML
    void addItem(MouseEvent event) {
        // Satisfies: shall #1, #1.1, #1.2, #1.3, #2
        if(event.getSource() == addButton){
            UpdateInventory inventory = new UpdateInventory();

            // These will convert all the inputs from the all the text fields into a string
            String item = itemNameTextField.getText();
            String itemValue = "$" + itemValueTextField.getText();
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
        if (event.getSource() == loadButton) {
            table.getItems().clear();

            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Load");

            List<InventoryModel> inventory = new ArrayList<>();
            java.io.File selectedFile = fileChooser.showOpenDialog(new Stage());
            java.io.File file = new java.io.File(selectedFile.getAbsolutePath());

            String fileName = selectedFile.toString();
            String extension = fileName.split("\\.")[1];

            LoadData load = new LoadData();

            if(extension.matches("txt")){
                itemList = load.loadTxt(file, inventory, itemList);
                table.setItems(itemList);
            }

            if(extension.matches("html")){
                itemList = load.loadHtml(file, inventory, itemList);
                table.setItems(itemList);
            }

            if(extension.matches("json")){
                itemList = load.loadJson(file, inventory, itemList);
                table.setItems(itemList);
            }
        }
    }

    @FXML
    void save(ActionEvent event) throws IOException {
        if(event.getSource() == saveButton){
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text", "*.txt"));
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("HTML", "*.html"));
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON", "*.json"));

            fileChooser.setTitle("Save");
            java.io.File fileName = fileChooser.showSaveDialog(new Stage());
            String extension = fileChooser.selectedExtensionFilterProperty().get().getExtensions().get(0).substring(1);

            SaveData save = new SaveData();

            if(extension.matches(".txt")){
                System.out.println("\n");
                String result = save.saveTxt(fileName, itemList);
                // This will just show what is printed into the file
                System.out.println(result);
            }

            if(extension.matches(".html")){
                System.out.println("\n");
                String result = save.saveHtml(fileName, itemList);
                // This will just show what is printed into the file
                System.out.println(result);
            }

            if(extension.matches(".json")){
                System.out.println("\n");
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
                event.getRowValue().setItemName(newValue);
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


    private void textFieldListener() {
        addButton.setOnAction(event -> {
            String invalid = "Invalid Value!";

            if(Objects.equals(itemNameTextField.getText(), "") ||
                    itemNameTextField.getText().length() < 2 || itemNameTextField.getText().length() > 256){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle(invalid);
                alert.setContentText("An item name must be 2 - 256 characters inclusive.");

                alert.showAndWait();
            }

            else if (Objects.equals(itemValueTextField.getText(), "")) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle(invalid);
                alert.setContentText("Item value is empty.");

                alert.showAndWait();
            }

            else if (Objects.equals(itemSerialNumberTextField.getText(), "")) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle(invalid);
                alert.setContentText("Serial number is empty.");

                alert.showAndWait();
            }

            if(itemValueTextField.getText().matches("[0-9]+")){
                System.out.println("Input valid.");
            }

            else{
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle(invalid);
                alert.setContentText("An item must be a valid digit.");

                alert.showAndWait();
            }

            if(itemSerialNumberTextField.getText().matches("[A-Za-z]+-[A-Za-z0-9]{1,3}-[A-Za-z0-9]{1,3}-[A-Za-z0-9]{1,3}")){
                System.out.println("Serial Valid.");
            }

            else{
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle(invalid);
                alert.setContentText("A serial number must follow this format A-XXX-XXX-XXX.");

                alert.showAndWait();
            }
        });

    }

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

        searchField.textProperty().addListener((observable, oldValue, newValue) ->
                table.setItems(filterList(itemList, newValue.toLowerCase()))
        );
    }

    private ObservableList<InventoryModel> filterList(List<InventoryModel> list, String searchText){
        List<InventoryModel> filteredList = new ArrayList<>();

        for (InventoryModel inventory : list){
            if(searchFindsOrder(inventory, searchText)){
                filteredList.add(inventory);
            }
        }
        return FXCollections.observableList(filteredList);
    }

    private boolean searchFindsOrder(InventoryModel inventory, String searchText){
        return (inventory.getItemName().toLowerCase().contains(searchText)) ||
                (inventory.getItemSerialNumber().toLowerCase().contains(searchText));
    }
}
