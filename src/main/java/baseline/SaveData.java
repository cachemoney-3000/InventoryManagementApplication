/*
 * UCF COP3330 Fall 2021 Application Assignment 2 Solution
 * Copyright 2021 Joshua Samontanez
 */

package baseline;

import javafx.collections.ObservableList;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class SaveData {
    public String saveTxt(File filename, ObservableList<InventoryModel> itemList){
        // String builder, this will be helpful for testing if the method is saving data correctly
        StringBuilder sb = new StringBuilder();
        try(FileWriter writer = new FileWriter(filename)){
            // This will append the header
            sb.append(String.format("%-10s \t%-35s \t%s\n","Serial Number", "Item Name", "Value"));
            // This will write the header that will be shown into a text file
            writer.write(String.format("%-10s \t%-35s \t%s\n",
                    "Serial Number", "Item Name", "Value"));

            // This will loop through all the items inside the list
            for(InventoryModel item : itemList){
                // This will append the data from the list into the string builder
                sb.append(String.format("%-10s \t%-35s \t%s",
                        item.getItemSerialNumber(), item.getItemName(), item.getValue()));

                // This will write the data from the list into the text file
                writer.write(String.format("%-10s \t%-35s \t%s",
                        item.getItemSerialNumber(), item.getItemName(), item.getValue()));

                sb.append("\n");
                writer.write("\n");
        }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // It will return the string from the string builder (this will be useful for unit testing)
        return sb.toString();
    }

    public String saveHtml(File filename, ObservableList<InventoryModel> itemList) throws IOException {
        // String builder, this will be helpful for testing if the method is saving data correctly
        StringBuilder sb = new StringBuilder();
        try(FileWriter writer = new FileWriter(filename)){
            // For the first part of the data that will make us write a valid html file
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
            // For the last part of the data that will help us have a valid html file
            String html2 = """
                                </table>

                                </body>
                                </html>""";

            // This string builder will be sandwiched into the string "html1" and "html2"
            StringBuilder data = new StringBuilder();

            // It will loop through the items into the list, and put it into a table to be transferred into the html
            for(InventoryModel item : itemList) {
                data.append("<tr>\n<td>").append(item.getItemSerialNumber()).
                        append("</td>\n").append("<td>").append(item.getItemName()).
                        append("</td>\n").append("<td>").append(item.getValue()).append("</td>\n");
            }

            // This will put the html1, data, and html2 string all together
            sb.append(html1).append(data).append(html2);
            // This will put the html1, data, and html2 string all together, this will provide us a valid html file
            writer.write(html1 + data + html2);
        }
        // It will return the string from the string builder (this will be useful for unit testing)
        return sb.toString();
    }

    public String saveJson(File filename, ObservableList<InventoryModel> itemList){
        // String builder, this will be helpful for testing if the method is saving data correctly
        StringBuilder sb = new StringBuilder();

        try(FileWriter writer = new FileWriter(filename)){
            // For the first part of the data to be put into the json file
            String json = "{\n\t\"inventory\" : [\n";
            // For the last part of the data to be put into the json file
            String json2 = "\t]\n}";

            // This will store the values from the data
            StringBuilder add = new StringBuilder();
            // This will loop through all the items from the list and store it into "add"
            for(InventoryModel item : itemList){
                add.append("\t\t{\"Serial Number\":" + "\"").append(item.getItemSerialNumber()).append("\"");
                add.append(",\"Item Name\":" + "\"").append(item.getItemName()).append("\"");
                add.append(",\"Value\":" + "\"").append(item.getValue()).append("\"}");

                add.append(",\n");
            }
            // This will remove the last comma after the last item, so the json file will be a valid file
            add.deleteCharAt(add.lastIndexOf(",\n"));

            // This will help us have a valid json file, by adding json, add, and json2 string all together
            sb.append(json).append(add).append(json2);
            writer.write(json + add + json2);

        } catch (IOException e) {
            e.printStackTrace();
        }
        // It will return the string from the string builder (this will be useful for unit testing)
        return sb.toString();
    }

}
