package baseline;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
    void loadTxtTest() throws FileNotFoundException {
        List<InventoryModel> inventory = new ArrayList<>();
        ObservableList<InventoryModel> itemList = FXCollections.observableArrayList();

        java.io.File file = new java.io.File("C:\\Users\\samon\\Desktop\\New folder (3)\\samontanez-app2\\docs\\Test Files\\test.txt");

        /*
        Input inside the file:
        Serial Number       Item Name       Value
        A-123-123-123       XBOX            23
        A-123-123-124       Playstation     53
        A-123-123-125       Nintendo        500
        A-123-123-126       PC              5000
         */

        LoadData load = new LoadData();
        itemList = load.loadTxt(file, inventory, itemList);
        String actual = itemList.toString();
        String expected = "[XBOX                                /A-123-123-124 /23, " +
                "Playstation                         /A-123-123-125 /53, " +
                "Nintendo                            /A-123-123-126 /500," +
                " PC                                  /A-123-123-127 /5000]";

        assertEquals(expected, actual);
    }

    @Test
    void loadHtmlTest() throws IOException {
        List<InventoryModel> inventory = new ArrayList<>();
        ObservableList<InventoryModel> itemList = FXCollections.observableArrayList();

        java.io.File file = new java.io.File("C:\\Users\\samon\\Desktop\\New folder (3)\\samontanez-app2\\docs\\Test Files\\new.html");
        LoadData load = new LoadData();
        itemList = load.loadHtml(file, inventory, itemList);

        /*
        Input inside the file:
        Serial Number       Item Name       Value
        A-123-123-123       XBOX            1000
        A-123-123-124       Playstation     500
        A-123-123-125       Nintendo        1000
        A-123-123-126       PC              500
         */

        String actual = itemList.toString();
        String expected = "[XBOX/A-123-123-123/1000, " +
                "Playstation/a-123-123-124/500, " +
                "Nintendo/A-123-123-125/1000, " +
                "PC/a-123-123-126/500]";

        assertEquals(expected, actual);
    }

    @Test
    void loadJson() throws FileNotFoundException {
        List<InventoryModel> inventory = new ArrayList<>();
        ObservableList<InventoryModel> itemList = FXCollections.observableArrayList();

        java.io.File file = new java.io.File("C:\\Users\\samon\\Desktop\\New folder (3)\\samontanez-app2\\docs\\Test Files\\test.json");
        LoadData load = new LoadData();
        itemList = load.loadJson(file, inventory, itemList);

        /*
        Input inside the file:
        Serial Number       Item Name       Value
        A-123-123-132       XBOX            100
        A-123-123-133       Playstation     100
        A-123-123-134       Nintendo        500
        A-123-123-135       PC              1000
         */

        String actual = itemList.toString();
        String expected = "[XBOX/a-123-123-132/100, " +
                "Playstation/a-123-123-133/100, " +
                "Nintendo/a-123-123-134/500, " +
                "PC/a-123-123-135/1000]";

        assertEquals(expected, actual);
    }

    @Test
    void saveTxtTest(){
        // This will test what will be printed into a file
        SaveData file = new SaveData();
        java.io.File fileName = new java.io.File("docs/test.txt");
        ObservableList<InventoryModel> itemList = FXCollections.observableArrayList();
        itemList.add(new InventoryModel("XBOX", "25", "A-123-123-123"));
        itemList.add(new InventoryModel("XBOX 360", "25", "A-123-123-124"));
        itemList.add(new InventoryModel("XBOX S", "25", "A-123-123-125"));

        String actual = file.saveTxt(fileName, itemList);
        String expected = """
                Serial Number \tItem Name                           \tValue
                A-123-123-123 \tXBOX                                \t25
                A-123-123-124 \tXBOX 360                            \t25
                A-123-123-125 \tXBOX S                              \t25
                """;

        assertEquals(expected, actual);
    }

    @Test
    void saveHtmlTest() throws IOException {
        // This will test what will be printed into a file
        SaveData file = new SaveData();
        java.io.File fileName = new java.io.File("docs/test.html");
        ObservableList<InventoryModel> itemList = FXCollections.observableArrayList();
        itemList.add(new InventoryModel("XBOX", "25", "A-123-123-123"));
        itemList.add(new InventoryModel("XBOX 360", "25", "A-123-123-124"));
        itemList.add(new InventoryModel("XBOX S", "25", "A-123-123-125"));

        String actual = file.saveHtml(fileName, itemList);
        String expected = """
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
                    </tr><tr>
                <td>A-123-123-123</td>
                <td>XBOX</td>
                <td>25</td>
                <tr>
                <td>A-123-123-124</td>
                <td>XBOX 360</td>
                <td>25</td>
                <tr>
                <td>A-123-123-125</td>
                <td>XBOX S</td>
                <td>25</td>
                </table>

                </body>
                </html>""";

        assertEquals(expected, actual);
    }

    @Test
    void saveJsonTest(){
        // This will test what will be printed into a file
        SaveData file = new SaveData();
        java.io.File fileName = new java.io.File("docs/test.json");
        ObservableList<InventoryModel> itemList = FXCollections.observableArrayList();
        itemList.add(new InventoryModel("XBOX", "25", "A-123-123-123"));
        itemList.add(new InventoryModel("XBOX 360", "25", "A-123-123-124"));
        itemList.add(new InventoryModel("XBOX S", "25", "A-123-123-125"));

        String actual = file.saveJson(fileName, itemList);
        String expected = """
                {
                \t"inventory" : [
                \t\t{"Serial Number":"A-123-123-123","Item Name":"XBOX","Value":"25"},
                \t\t{"Serial Number":"A-123-123-124","Item Name":"XBOX 360","Value":"25"},
                \t\t{"Serial Number":"A-123-123-125","Item Name":"XBOX S","Value":"25"}
                \t]
                }""";

        assertEquals(expected, actual);
    }



}