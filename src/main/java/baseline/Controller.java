package baseline;

import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.SetChangeListener;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.css.PseudoClass;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;

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

    private final ObservableList<InventoryModel> itemList = FXCollections.observableArrayList(
            item -> new Observable[] {item.itemName}
    );


    @FXML
    void add(MouseEvent event) {
        String item = itemNameTextField.getText();
        String itemValue = itemValueTextField.getText();
        String itemSerial = itemSerialNumberTextField.getText();

        itemList.add(new InventoryModel(item, itemValue, itemSerial));
        table.setItems(itemList);
        System.out.println(itemList);
    }

    @FXML
    void clearAll(MouseEvent event) {
        table.getItems().clear();
        System.out.println(itemList);
    }

    @FXML
    void load(ActionEvent event) {
        // This will call the File object, so it can load a file and show it to the table

    }

    @FXML
    void save(ActionEvent event) {

    }



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        itemName.setCellValueFactory(
                new PropertyValueFactory<>("itemName")
        );
        itemValue.setCellValueFactory(
                new PropertyValueFactory<>("value")
        );
        itemSerialNumber.setCellValueFactory(
                new PropertyValueFactory<>("itemSerialNumber")
        );

        itemValue.setResizable(false);
        itemSerialNumber.setResizable(false);
        itemName.setResizable(false);

        editTableCol();
        textFieldListener();
        searchItem();
        removeItem();


    }

    private void removeItem(){
        removeButton.setOnAction(e -> {
            InventoryModel selectedItem = table.getSelectionModel().getSelectedItem();
            table.getItems().remove(selectedItem);
        });
    }
    private void editTableCol() {
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
            String invalid = "Invalid Value";
            if(Objects.equals(itemNameTextField.getText(), "") ||
                    itemNameTextField.getText().length() < 2 || itemNameTextField.getText().length() > 256){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle(invalid);
                alert.setContentText("An item name must be 2 - 256 characters inclusive");

                alert.showAndWait();
            }
            else if (Objects.equals(itemValueTextField.getText(), "")) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle(invalid);
                alert.setContentText("Item value is empty");

                alert.showAndWait();
            }
            else if (Objects.equals(itemSerialNumberTextField.getText(), "")) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle(invalid);
                alert.setContentText("Serial number is empty");

                alert.showAndWait();
            }

            if(itemValueTextField.getText().matches("[0-9]+")){
                System.out.println("Input valid");
            }

            else{
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle(invalid);
                alert.setContentText("An item must be a valid digit");

                alert.showAndWait();
            }

            if(itemSerialNumberTextField.getText().matches("[A-Za-z]+-[A-Za-z0-9]{1,3}-[A-Za-z0-9]{1,3}-[A-Za-z0-9]{1,3}")){
                System.out.println("Serial Valid");
            }
            else{
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle(invalid);
                alert.setContentText("A serial number must follow this format A-XXX-XXX-XXX");

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
