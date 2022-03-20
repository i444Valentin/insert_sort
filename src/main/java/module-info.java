module com.example.insert_sort {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;
    requires mysql.connector.java;

    opens com.example.insert_sort to javafx.fxml;
    exports com.example.insert_sort;
}