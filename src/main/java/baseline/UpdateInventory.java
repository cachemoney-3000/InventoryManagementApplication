package baseline;

import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.TableView;

public class UpdateInventory {
    public ObservableList<InventoryModel> addItem(String item, String itemValue, String itemSerial,
                                                  ObservableList<InventoryModel> itemList){

        if(itemList.contains(new InventoryModel(item, itemValue, itemSerial))){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Item already exist!");
            alert.setContentText("An item name and serial number must be unique.");

            alert.showAndWait();
        }

        else
            itemList.add(new InventoryModel(item, itemValue, itemSerial));

        return itemList;
    }

    public ObservableList<InventoryModel> clearInventory(ObservableList<InventoryModel> itemList){
        itemList.clear();
        return itemList;
    }




}
