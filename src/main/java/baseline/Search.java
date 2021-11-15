package baseline;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.List;

public class Search {
    // This method will store all the matching data into an observable list
    public ObservableList<InventoryModel> filterList(List<InventoryModel> list, String searchText){
        List<InventoryModel> filteredList = new ArrayList<>();

        // It will loop through all the data that is already inputted and search for any data that matched
        for (InventoryModel inventory : list){
            if(searchFindsOrder(inventory, searchText)){
                filteredList.add(inventory);
            }
        }
        return FXCollections.observableList(filteredList);
    }

    // This is a helper method for filter list, it will just return true or false, when finding an item from the list
    private boolean searchFindsOrder(InventoryModel inventory, String searchText){
        return (inventory.getItemName().toLowerCase().contains(searchText)) ||
                (inventory.getItemSerialNumber().toLowerCase().contains(searchText));
    }
}
