package baseline;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;

import java.net.URL;
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
    private TableColumn<InventoryModel, String> itemActions;

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
    private Button searchButton;

    @FXML
    private TextField searchField;

    private final ObservableList<InventoryModel> itemList = FXCollections.observableArrayList();


    @FXML
    void add(MouseEvent event) {
        String item = itemNameTextField.getText();
        String itemValue = itemValueTextField.getText();
        String itemSerial = itemSerialNumberTextField.getText();

        itemList.add(new InventoryModel(item, itemValue, itemSerial));
        table.setItems(itemList);
    }

    @FXML
    void clearAll(MouseEvent event) {
        // This will remove all the items inside the list
    }

    @FXML
    void load(ActionEvent event) {
        // This will call the File object, so it can load a file and show it to the table

    }

    @FXML
    void save(ActionEvent event) {
        // This will call the File object
        // Then it will save the items inside the table into a file
        // The user will choose for the extension

    }

    @FXML
    void search(MouseEvent event) {
        // This method will search
        // It can search either by item name or serial number

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

        editTableCol();
        removeButton();
        textFieldListener();

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

    private void removeButton() {
        Callback<TableColumn<InventoryModel, String>, TableCell<InventoryModel, String>> cellFactory =
                new Callback<>() {
                    @Override
                    public TableCell<InventoryModel, String> call(TableColumn<InventoryModel, String> param) {
                        return new TableCell<InventoryModel, String>() {
                            final Button btn = new Button("Remove");

                            {
                                btn.setOnAction(event -> table.getItems().remove(getIndex()));
                            }

                            @Override
                            public void updateItem(String item, boolean empty) {
                                super.updateItem(item, empty);
                                if (empty) {
                                    setGraphic(null);
                                } else {
                                    setGraphic(btn);
                                }
                                setText(null);
                            }
                        };

                    }
                };

        itemActions.setCellFactory(cellFactory);
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
}
