module com.example {
    requires javafx.controls;
    requires transitive javafx.graphics;
    requires javafx.fxml;
    requires commons.math3;
    requires transitive java.desktop;
    opens com.example to javafx.fxml;
    exports com.example;
}
