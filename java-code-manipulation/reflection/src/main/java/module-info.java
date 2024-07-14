module com.brothergeol.reflection.reflection {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.brothergeol.reflection.reflection to javafx.fxml;
    exports com.brothergeol.reflection.reflection;
}