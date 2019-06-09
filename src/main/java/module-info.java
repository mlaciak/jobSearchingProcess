module com.laciak {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.persistence;
    requires java.sql;

    opens com.laciak to javafx.fxml;
    exports com.laciak;
}