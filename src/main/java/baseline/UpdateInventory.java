/*
 * UCF COP3330 Fall 2021 Application Assignment 2 Solution
 * Copyright 2021 Joshua Samontanez
 */

package baseline;

import javafx.collections.ObservableList;
import javafx.scene.control.Alert;

public class UpdateInventory {
    public ObservableList<InventoryModel> addItem(String item, String itemValue, String itemSerial,
                                                  ObservableList<InventoryModel> itemList){

        // Shall #2.1
        // If an item name or serial number already exists, it will show an error
        if(itemList.contains(new InventoryModel(item, itemValue, itemSerial))){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Item already exist!");
            alert.setContentText("An item name and serial number must be unique.");

            alert.showAndWait();
        }

        // If the inputs passed all the requirements, it will be added into the list
        else
            itemList.add(new InventoryModel(item, itemValue, itemSerial));

        return itemList;
    }

    public ObservableList<InventoryModel> clearInventory(ObservableList<InventoryModel> itemList){
        // This will clear all the items inside the observable list
        itemList.clear();

        return itemList;
    }




}
