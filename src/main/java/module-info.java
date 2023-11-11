module com.example.timchat {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.timchat to javafx.fxml;
    exports com.example.timchat;
}