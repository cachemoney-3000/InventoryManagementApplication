package baseline;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
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

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.*;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

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
    void updateInventoryAction(ActionEvent event) {
        UpdateInventory inventory = new UpdateInventory();

        if(event.getSource() == addButton){
            String item = itemNameTextField.getText();
            String itemValue = itemValueTextField.getText();
            String itemSerial = itemSerialNumberTextField.getText();

            if(!itemList.contains(new InventoryModel(item, itemValue, itemSerial))){
                itemNameTextField.setText(null);
                itemValueTextField.setText(null);
                itemSerialNumberTextField.setText(null);
            }

            itemList = inventory.addItem(item, itemValue, itemSerial, itemList);
            table.setItems(itemList);
        }

        else if(event.getSource() == clearButton){
            table.getItems().clear();
            itemList = inventory.clearInventory(itemList);
        }
    }


    @FXML
    void load(ActionEvent event) throws FileNotFoundException {
        if (event.getSource() == loadButton) {
            table.getItems().clear();

            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Load");

            List<InventoryModel> inventory = new ArrayList<>();
            java.io.File selectedFile = fileChooser.showOpenDialog(new Stage());
            java.io.File file = new java.io.File(selectedFile.getAbsolutePath());

            String fileName = selectedFile.toString();
            String extension = fileName.split("\\.")[1];

            try (Scanner reader = new Scanner(file)) {
                if(extension.matches("txt")){
                    reader.nextLine();
                    while (reader.hasNextLine()) {

                        String line = reader.nextLine();
                        String[] separator = line.split("\t");

                        String serial = separator[0];
                        String name = separator[1];
                        String value = separator[2];

                        InventoryModel newInventory = new InventoryModel(name, value, serial);
                        inventory.add(newInventory);
                    }

                    itemList.addAll(inventory);
                    table.setItems(itemList);
                }

                if(extension.matches("html")){
                    Document html = Jsoup.parse(file, "UTF-8", "");
                    Elements rows = html.select("tr");
                    int num = 0;

                    for(Element row: rows){

                        Elements columns = row.select("td");
                        StringBuilder sb = new StringBuilder();

                        int counter = 0;
                        for (Element column: columns){
                            sb.append(column.text());
                            if(counter < 2){
                                sb.append(".");
                            }

                            counter++;
                        }

                        if(num != 0){
                            String[] separator = sb.toString().split("\\.");
                            String serial = separator[0];
                            String name = separator[1];
                            String value = separator[2];

                            InventoryModel newInventory = new InventoryModel(name, value, serial);
                            inventory.add(newInventory);

                        }
                        num++;

                    }
                    itemList.addAll(inventory);
                    table.setItems(itemList);

                }

                if (extension.matches("json")){
                    Object parser = JsonParser.parseReader(new FileReader(selectedFile));
                    JsonObject jsonObject = (JsonObject) parser;

                    JsonArray jsonArray = (JsonArray) jsonObject.get("inventory");
                    Iterator<JsonElement> iterator = jsonArray.iterator();

                    while(iterator.hasNext()){
                        JsonObject inventoryObject = (JsonObject) iterator.next();
                        String serial = inventoryObject.get("Serial Number").getAsString();
                        String name = inventoryObject.get("Item Name").getAsString();
                        String value = inventoryObject.get("Value").getAsString();

                        InventoryModel newInventory = new InventoryModel(name, value, serial);
                        inventory.add(newInventory);
                    }

                    itemList.addAll(inventory);
                    table.setItems(itemList);

                }
            } catch (IOException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Invalid File");
                alert.setContentText(e.toString());

                alert.showAndWait();
            }
        }
    }

    @FXML
    void save(ActionEvent event) {

        if(event.getSource() == saveButton){
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text", "*.txt"));
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("HTML", "*.html"));
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON", "*.json"));

            fileChooser.setTitle("Save");

            try{
                try(FileWriter writer = new FileWriter(fileChooser.showSaveDialog(new Stage()))){
                    String extension = fileChooser.selectedExtensionFilterProperty().get().getExtensions().get(0).substring(1);
                    System.out.println(extension);

                    if(extension.matches(".txt")){
                        writer.write(String.format("%-10s \t%-35s \t%s\n",
                                "Serial Number", "Item Name", "Value"));
                        for(InventoryModel item : itemList){
                            writer.write(String.format("%-10s \t%-35s \t%s",
                                    item.getItemSerialNumber(), item.getItemName(), item.getValue()));
                            writer.write("\n");
                        }
                    }

                    if(extension.matches(".html")){
                        String html1 = """
                                <!DOCTYPE html>
                                <html>
                                <style>
                                table, th, td {
                                  border:1px solid black;
                                }
                                </style>
                                <body>

                                <table style="width:100%">
                                    <tr>
                                        <th>Serial Number</th>
                                        <th>Item Name</th>
                                        <th>Value</th>
                                    </tr>""";
                        String html2 = """
                                </table>

                                </body>
                                </html>""";

                        StringBuilder data = new StringBuilder();

                        for(InventoryModel item : itemList) {
                            data.append("<tr>\n<td>").append(item.getItemSerialNumber()).
                                    append("</td>\n").append("<td>").append(item.getItemName()).
                                    append("</td>\n").append("<td>").append(item.getValue()).append("</td>\n");
                        }

                        writer.write(html1 + data + html2);


                    }

                    if(extension.matches(".json")){
                        String json = "{\n\t\"inventory\" : [\n";
                        String json2 = "\t]\n}";

                        StringBuilder sb = new StringBuilder();

                        for(InventoryModel item : itemList){
                            sb.append("\t\t{\"Serial Number\":" + "\"").append(item.getItemSerialNumber()).append("\"");
                            sb.append(",\"Item Name\":" + "\"").append(item.getItemName()).append("\"");
                            sb.append(",\"Value\":" + "\"").append(item.getValue()).append("\"}");

                            sb.append(",\n");
                        }
                        sb.deleteCharAt(sb.lastIndexOf(",\n"));
                        writer.write(json + sb + json2);
                    }


                }catch (Exception e){
                    e.printStackTrace();
                }

                System.out.println("saved");

            }catch (Exception e){
                e.printStackTrace();
            }
        }
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
        editableTable();

    }



    private void editableTable(){
        String invalid = "Invalid Value!";

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
