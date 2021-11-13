module baseline.inventorymanagement {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires com.google.gson;


    opens baseline to javafx.fxml;
    exports baseline;
}