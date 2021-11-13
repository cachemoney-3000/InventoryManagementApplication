module baseline.inventorymanagement {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires com.google.gson;
    requires org.jsoup;


    opens baseline to javafx.fxml;
    exports baseline;
}