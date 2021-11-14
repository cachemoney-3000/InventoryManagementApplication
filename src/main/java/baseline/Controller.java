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

import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.*;

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

    @FXML
    void addItem(MouseEvent event) {
        if(event.getSource() == addButton){
            UpdateInventory inventory = new UpdateInventory();

            String item = itemNameTextField.getText();
            String itemValue = itemValueTextField.getText();
            String itemSerial = itemSerialNumberTextField.getText();

            if(!itemList.contains(new InventoryModel(item, itemValue, itemSerial))){
                itemNameTextField.setText("");
                itemValueTextField.setText("");
                itemSerialNumberTextField.setText("");
            }

            itemList = inventory.addItem(item, itemValue, itemSerial, itemList);
            table.setItems(itemList);

        }
    }

    @FXML
    void clearItems(MouseEvent event) {
        if(event.getSource() == clearButton){
            UpdateInventory inventory = new UpdateInventory();
            table.getItems().clear();
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


    @Override
    public void initialize(URL location, ResourceBundle resources) {
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

    private void cellPropertyValue(){
        itemName.setCellValueFactory(
                new PropertyValueFactory<>("itemName")
        );
        itemValue.setCellValueFactory(
                new PropertyValueFactory<>("value")
        );
        itemSerialNumber.setCellValueFactory(
                new PropertyValueFactory<>("itemSerialNumber")
        );
    }

    private void warningHandler(){
        String invalid = "Invalid Value!";

        nameTextFieldWarnings(invalid);
        valueTextFieldWarnings(invalid);
        serialTextFieldWarnings(invalid);
    }

    private void nameTextFieldWarnings(String invalid){
        itemName.setOnEditCommit(event -> {
            String newValue = event.getNewValue();

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

            else{
                event.getRowValue().setItemName(newValue);
            }

            itemName.setVisible(false);
            itemName.setVisible(true);
        });
    }

    private void valueTextFieldWarnings(String invalid){
        itemValue.setOnEditCommit(event -> {
            String newValue = event.getNewValue();

            if(Objects.equals(newValue, "")){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle(invalid);
                alert.setContentText("Item value is empty.");

                alert.showAndWait();

            }

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

            itemValue.setVisible(false);
            itemValue.setVisible(true);
        });
    }

    private void serialTextFieldWarnings(String invalid){
        itemSerialNumber.setOnEditCommit(event -> {
            String newValue = event.getNewValue();

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

            itemSerialNumber.setVisible(false);
            itemSerialNumber.setVisible(true);
        });
    }


    private void removeItem(){
        removeButton.setOnAction(e -> {
            InventoryModel selectedItem = table.getSelectionModel().getSelectedItem();
            System.out.println(selectedItem);
            table.getItems().remove(selectedItem);
        });
    }

    private void editTableContents() {
        itemName.setCellFactory(TextFieldTableCell.forTableColumn());

        itemName.setOnEditCommit(e ->
                e.getTableView().getItems().get(e.getTablePosition().getRow()).setItemName(e.getNewValue()));

        itemValue.setCellFactory(TextFieldTableCell.forTableColumn());

        itemValue.setOnEditCommit(e ->
                e.getTableView().getItems().get(e.getTablePosition().getRow()).setValue(e.getNewValue()));

        itemSerialNumber.setCellFactory(TextFieldTableCell.forTableColumn());

        itemSerialNumber.setOnEditCommit(e ->
                e.getTableView().getItems().get(e.getTablePosition().getRow()).setItemSerialNumber(e.getNewValue()));


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
