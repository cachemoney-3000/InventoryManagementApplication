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

    }

    @FXML
    void clearAll(MouseEvent event) {

    }

    @FXML
    void load(ActionEvent event) {

    }

    @FXML
    void save(ActionEvent event) {

    }

    @FXML
    void search(MouseEvent event) {

    }

}
