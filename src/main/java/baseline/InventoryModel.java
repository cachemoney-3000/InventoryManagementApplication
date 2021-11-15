package baseline;

import javafx.beans.property.SimpleStringProperty;

public class InventoryModel {
    // This method will be used to handle the data that was inputted by the user
    SimpleStringProperty itemName;
    SimpleStringProperty value;
    SimpleStringProperty itemSerialNumber;

    InventoryModel(String itemName, String value, String itemSerialNumber){
        this.itemName = new SimpleStringProperty(itemName);
        this.value = new SimpleStringProperty(value);
        this.itemSerialNumber = new SimpleStringProperty(itemSerialNumber);
    }

    // This method will be used for getting the values from the text field
    public String getItemName(){
        return itemName.get();
    }

    public void setItemName(String name){
        this.itemName = new SimpleStringProperty(name);
    }

    public String getValue(){
        return value.get();
    }

    public void setValue(String val){
        this.value = new SimpleStringProperty(val);
    }

    public String getItemSerialNumber(){
        return itemSerialNumber.get();
    }

    public void setItemSerialNumber(String serialNumber){
        this.itemSerialNumber = new SimpleStringProperty(serialNumber);
    }

    // This will check if the item name or the item serial number already exist in the list
    // This will return true if it already exists, false if not
    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof InventoryModel inventory)) {
            return false;
        }

        return inventory.itemName.isEqualTo(itemName).get() || inventory.itemSerialNumber.isEqualTo(itemSerialNumber).get();
    }

    // This will just convert the data address into a string, so it will be readable when adding it into the observable list
    @Override
    public String toString() {
        return getItemName() + "/" + Double.parseDouble(getItemSerialNumber()) + "/" + getValue();
    }
}
