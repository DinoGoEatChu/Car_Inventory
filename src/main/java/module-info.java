module com.example.car_inventory {
    requires javafx.controls;
    requires javafx.fxml;
    requires kotlin.stdlib;

    requires org.controlsfx.controls;

    opens com.example.car_inventory to javafx.fxml;
    exports com.example.car_inventory;

}