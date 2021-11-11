package baseline;

public record InventoryModel(String itemName, int value, String itemSerialNumber) {
    // This method will be used for getting the values from the text field
    public String getItemName(){
        return itemName;
    }

    public int getValue(){
        return value;
    }

    public String getItemSerialNumber(){
        return itemSerialNumber;
    }


}
