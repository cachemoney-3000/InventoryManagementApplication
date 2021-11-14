package baseline;

import javafx.collections.ObservableList;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class SaveData {
    public String saveTxt(File filename, ObservableList<InventoryModel> itemList){
        StringBuilder sb = new StringBuilder();
        try(FileWriter writer = new FileWriter(filename)){
            sb.append(String.format("%-10s \t%-35s \t%s\n","Serial Number", "Item Name", "Value"));

            writer.write(String.format("%-10s \t%-35s \t%s\n",
                    "Serial Number", "Item Name", "Value"));
            for(InventoryModel item : itemList){
                sb.append(String.format("%-10s \t%-35s \t%s",
                        item.getItemSerialNumber(), item.getItemName(), item.getValue()));

                writer.write(String.format("%-10s \t%-35s \t%s",
                        item.getItemSerialNumber(), item.getItemName(), item.getValue()));

                sb.append("\n");

                writer.write("\n");
        }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return sb.toString();
    }

    public String saveHtml(File filename, ObservableList<InventoryModel> itemList) throws IOException {
        StringBuilder sb = new StringBuilder();
        try(FileWriter writer = new FileWriter(filename)){
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

            sb.append(html1).append(data).append(html2);
            writer.write(html1 + data + html2);
        }

        return sb.toString();
    }

    public String saveJson(File filename, ObservableList<InventoryModel> itemList){
        StringBuilder sb = new StringBuilder();

        try(FileWriter writer = new FileWriter(filename)){
            String json = "{\n\t\"inventory\" : [\n";
            String json2 = "\t]\n}";

            StringBuilder add = new StringBuilder();
            for(InventoryModel item : itemList){
                add.append("\t\t{\"Serial Number\":" + "\"").append(item.getItemSerialNumber()).append("\"");
                add.append(",\"Item Name\":" + "\"").append(item.getItemName()).append("\"");
                add.append(",\"Value\":" + "\"").append(item.getValue()).append("\"}");

                add.append(",\n");
            }
            add.deleteCharAt(add.lastIndexOf(",\n"));

            sb.append(json).append(add).append(json2);
            writer.write(json + add + json2);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return sb.toString();
    }

}
