module com.telavm.telavm {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.telavm.telavm to javafx.fxml;
    exports com.telavm.telavm;
}