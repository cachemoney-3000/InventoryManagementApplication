module baseline.inventorymanagement {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;


    opens baseline to javafx.fxml;
    exports baseline;
}