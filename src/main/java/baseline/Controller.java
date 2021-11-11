package baseline;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

public class Controller {

    @FXML
    private Button addButton;

    @FXML
    private Button clearButton;

    @FXML
    private TableColumn<?, ?> itemActions;

    @FXML
    private TableColumn<?, ?> itemName;

    @FXML
    private TextField itemNameTextField;

    @FXML
    private TableColumn<?, ?> itemSerialNumber;

    @FXML
    private TextField itemSerialNumberTextField;

    @FXML
    private TableColumn<?, ?> itemValue;

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

    @FXML
    private TableView<?> table;

    @FXML
    void add(MouseEvent event) {
        // This will call the "InventoryModel" class to get the value from the text fields
        // This will add an item into the list
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

}
