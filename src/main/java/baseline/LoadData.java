package baseline;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javafx.collections.ObservableList;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class LoadData {
    public ObservableList<InventoryModel> loadTxt(java.io.File file , List<InventoryModel> inventory,
                                                  ObservableList<InventoryModel> itemList) throws FileNotFoundException {
        try (Scanner reader = new Scanner(file)) {
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
            }
        return itemList;
    }

    public ObservableList<InventoryModel> loadHtml(java.io.File file , List<InventoryModel> inventory,
                                                   ObservableList<InventoryModel> itemList) throws IOException {
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

            itemList.addAll(inventory);

        }
        return itemList;
    }

    public ObservableList<InventoryModel> loadJson(File file , List<InventoryModel> inventory,
                                                   ObservableList<InventoryModel> itemList) throws FileNotFoundException {
        Object parser = JsonParser.parseReader(new FileReader(file));
        JsonObject jsonObject = (JsonObject) parser;

        JsonArray jsonArray = (JsonArray) jsonObject.get("inventory");

        for (JsonElement jsonElement : jsonArray) {
            JsonObject inventoryObject = (JsonObject) jsonElement;
            String serial = inventoryObject.get("Serial Number").getAsString();
            String name = inventoryObject.get("Item Name").getAsString();
            String value = inventoryObject.get("Value").getAsString();

            InventoryModel newInventory = new InventoryModel(name, value, serial);
            inventory.add(newInventory);
        }

        itemList.addAll(inventory);
        return itemList;
    }

}
