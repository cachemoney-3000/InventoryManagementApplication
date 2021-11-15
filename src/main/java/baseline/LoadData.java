/*
 * UCF COP3330 Fall 2021 Application Assignment 2 Solution
 * Copyright 2021 Joshua Samontanez
 */

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
    public ObservableList<InventoryModel> loadTxt(java.io.File file, List<InventoryModel> inventory,
                                                  ObservableList<InventoryModel> itemList) throws FileNotFoundException {
        try (Scanner reader = new Scanner(file)) {
            reader.nextLine();
            // This will read all the lines from the text file until there are no more to read
            while (reader.hasNextLine()) {
                // Store all the lines into this string
                String line = reader.nextLine();
                // This will split the data from one line
                String[] separator = line.split("\t");

                // Stores the serial number of the string
                String serial = separator[0];
                // Stores the name of an item
                String name = separator[1];
                // Stores the value of the item
                String value = separator[2];

                // It will then create a new object
                InventoryModel newInventory = new InventoryModel(name, value, serial);
                // It will then add the new object into the list
                inventory.add(newInventory);
            }
            // After reading all the lines, it will then add all the items into the observable list
            itemList.addAll(inventory);
            }
        // It will return all the items
        return itemList;
    }

    public ObservableList<InventoryModel> loadHtml(java.io.File file , List<InventoryModel> inventory,
                                                   ObservableList<InventoryModel> itemList) throws IOException {
        Document html = Jsoup.parse(file, "UTF-8", "");
        Elements rows = html.select("tr");
        int num = 0;

        // This will loop through the rows
        for(Element row: rows){
            Elements columns = row.select("td");
            StringBuilder sb = new StringBuilder();

            int counter = 0;
            // This will loop through all the column inside a row, to get the data
            for (Element column: columns){
                // This will store the data into this string builder
                sb.append(column.text());
                if(counter < 2){
                    // This will add a dot for each data, so we know what is an item name, value, or serial number
                    sb.append(".");
                }
                // This is used for fixing the bug where it is not appending the data correctly
                counter++;
            }

            if(num != 0){
                // After adding all the data into a string builder
                // We can split the line by the dot, so we know what data is what
                String[] separator = sb.toString().split("\\.");
                // This will store the serial number
                String serial = separator[0];
                // This will store the item name
                String name = separator[1];
                // This will store the item value
                String value = separator[2];

                // Using the values, it will create a new object
                InventoryModel newInventory = new InventoryModel(name, value, serial);
                // The object will be added into the list
                inventory.add(newInventory);

            }
            num++;
        }
        // After scanning and separating the data, all the data will be added into the observable list
        itemList.addAll(inventory);
        // It wil return all the data from the item list
        return itemList;
    }

    public ObservableList<InventoryModel> loadJson(File file , List<InventoryModel> inventory,
                                                   ObservableList<InventoryModel> itemList) throws FileNotFoundException {
        Object parser = JsonParser.parseReader(new FileReader(file));
        JsonObject jsonObject = (JsonObject) parser;

        JsonArray jsonArray = (JsonArray) jsonObject.get("inventory");

        // This will loop through all the lines from the json file
        for (JsonElement jsonElement : jsonArray) {
            JsonObject inventoryObject = (JsonObject) jsonElement;
            // It will then separate the values based on the data description and store them into string
            String serial = inventoryObject.get("Serial Number").getAsString();
            String name = inventoryObject.get("Item Name").getAsString();
            String value = inventoryObject.get("Value").getAsString();

            // After getting all the data, it will be used to create a new objects
            InventoryModel newInventory = new InventoryModel(name, value, serial);
            // The object will be used and be added into the list
            inventory.add(newInventory);
        }
        // After scanning all the data from the json file, those data will be added into the observable list
        itemList.addAll(inventory);
        // It will then return the observable list with all the data inside it
        return itemList;
    }
}
