package com.telavm;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.stage.FileChooser;
import javafx.stage.Stage;


import java.io.*;

public class Controller {
    private File selectedFile;

    public void runMethod() throws IOException {

    }

    public void openMethod() throws IOException {
        Stage stage = new Stage();

        FileChooser chooser = new FileChooser();

        chooser.setTitle("Open File");

        selectedFile = chooser.showOpenDialog(stage);
        FileReader FR = new FileReader(selectedFile.getAbsolutePath());
        BufferedReader BR = new BufferedReader(FR);

        StringBuilder sb = new StringBuilder();
        String myText;

        while ((myText = BR.readLine()) != null) {
            sb.append(myText).append("\n");
        }

//        codeArea.replaceText(sb.toString());                              TEM QUE MUDAR!!!
    }
}