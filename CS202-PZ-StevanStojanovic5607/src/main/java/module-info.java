module com.example.luckysix {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires mysql.connector.j;


    opens com.example.luckysix to javafx.fxml;
    exports com.example.luckysix;
    exports com.example.luckysix.networking;
    opens com.example.luckysix.networking to javafx.fxml;
    exports com.example.luckysix.utill;
    opens com.example.luckysix.utill to javafx.fxml;
    exports com.example.luckysix.scenes;
    opens com.example.luckysix.scenes to javafx.fxml;
    exports com.example.luckysix.database;
    opens com.example.luckysix.database to javafx.fxml;
}