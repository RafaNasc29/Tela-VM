module com.telavm.telavm {
    requires javafx.controls;
    requires javafx.fxml;
    requires richtextfx.fat;


    opens com.telavm to javafx.fxml;
    exports com.telavm;
}