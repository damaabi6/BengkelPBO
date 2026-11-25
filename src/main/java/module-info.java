module com.bengkel.bengkelpbo {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens com.bengkel to javafx.fxml;
    exports com.bengkel;
}