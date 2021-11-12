package baseline;

import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.Button;

import java.util.Objects;

public class InventoryModel {
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

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof InventoryModel)) {
            return false;
        }
        InventoryModel inventory = (InventoryModel) o;

        // persons.id.equals() leads to the default implementation in Object
        // --> instead use this one.
        // The Property classes have their own isEqualTo method
        // with get(), you will get your simple boolean from the returned BooleanBinding
        return inventory.itemSerialNumber.isEqualTo(itemSerialNumber).get() &&
                inventory.itemName.isEqualTo(itemName).get();
    }

    @Override
    public String toString() {
        return itemName + "/" + itemSerialNumber + "/" + value;
    }
}
