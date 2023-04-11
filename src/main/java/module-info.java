module com.example.car_inventory {
    requires javafx.controls;
    requires javafx.fxml;
    requires kotlin.stdlib;

    requires org.controlsfx.controls;
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.kotlin;
    requires com.fasterxml.jackson.datatype.jsr310;

    opens com.example.car_inventory to javafx.fxml, com.fasterxml.jackson.databind;

    exports com.example.car_inventory;

}