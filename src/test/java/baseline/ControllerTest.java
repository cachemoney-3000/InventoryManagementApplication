package baseline;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ControllerTest {

    @Test
    void addItemTest() {
        UpdateInventory inventory = new UpdateInventory();
        ObservableList<InventoryModel> itemList = FXCollections.observableArrayList();
        String item = "Xbox";
        String itemVal = "500";
        String itemSerial = "A-123-123-123";

        itemList = inventory.addItem(item, itemVal, itemSerial, itemList);

        String item1 = "Nintendo";
        String itemVal1 = "500";
        String itemSerial1 = "A-123-123-124";

        itemList = inventory.addItem(item1, itemVal1, itemSerial1, itemList);

        String item2 = "Playstation";
        String itemVal2 = "500";
        String itemSerial2 = "A-123-123-125";

        itemList = inventory.addItem(item2, itemVal2, itemSerial2, itemList);

        String actual = itemList.toString();
        String expected = "[Xbox/A-123-123-123/500, Nintendo/A-123-123-124/500, Playstation/A-123-123-125/500]";
        assertEquals(expected, actual);
    }

    @Test
    void clearList() {
        UpdateInventory inventory = new UpdateInventory();
        ObservableList<InventoryModel> itemList = FXCollections.observableArrayList();
        
        String item = "Xbox";
        String itemVal = "500";
        String itemSerial = "A-123-123-123";

        itemList = inventory.addItem(item, itemVal, itemSerial, itemList);

        String item1 = "Nintendo";
        String itemVal1 = "500";
        String itemSerial1 = "A-123-123-124";

        itemList = inventory.addItem(item1, itemVal1, itemSerial1, itemList);

        String item2 = "Playstation";
        String itemVal2 = "500";
        String itemSerial2 = "A-123-123-125";

        itemList = inventory.addItem(item2, itemVal2, itemSerial2, itemList);
        
        /*
        Adding all the items:
        [Xbox/A-123-123-123/500, Nintendo/A-123-123-124/500, Playstation/A-123-123-125/500]
         */

        itemList = inventory.clearInventory(itemList);
        String actual = itemList.toString();
        String expected ="[]"; // After clearing all the items, the list must be empty
        assertEquals(expected, actual);

    }

    @Test
    void save() {
    }
}