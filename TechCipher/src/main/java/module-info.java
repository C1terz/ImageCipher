module com.example.techcipher {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;
    requires java.desktop;

    opens com.example.techcipher to javafx.fxml;
    exports com.example.techcipher;
}