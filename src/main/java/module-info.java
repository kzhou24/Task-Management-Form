module com.example.todolistprooject {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.datatransfer;


    opens com.example.todolistprooject to javafx.fxml;
    exports com.example.todolistprooject;
}