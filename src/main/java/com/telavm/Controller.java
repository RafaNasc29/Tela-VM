package com.telavm;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;


import java.io.*;

public class Controller {
    private File selectedFile;

    @FXML
    private TableView<Dado> codeArea;
    @FXML
    private TableColumn<Dado, String> label;
    @FXML
    private TableColumn<Dado, String> command;
    @FXML
    private TableColumn<Dado, String> param1;
    @FXML
    private TableColumn<Dado, String> param2;
//    @FXML
//    private TableColumn<Dado, String> comment;
    private MaquinaVirtual maquinaVirtual;

    @FXML
    public void initializeTable() {
        label.setCellValueFactory(cellData -> cellData.getValue().labelProperty());
        command.setCellValueFactory(cellData -> cellData.getValue().commandProperty());
        param1.setCellValueFactory(cellData -> cellData.getValue().param1Property());
        param2.setCellValueFactory(cellData -> cellData.getValue().param2Property());
//        comment.setCellValueFactory(cellData -> cellData.getValue().commentProperty());

        ObservableList<Dado> data = FXCollections.observableArrayList();

        try(BufferedReader br = new BufferedReader(new FileReader(selectedFile))){
            String line;

            while ((line = br.readLine()) != null ) {
                String[] parts = line.trim().split("\\s+");
                Dado dado = new Dado();

                if (line.startsWith(" ") && parts.length >= 1) {
                    dado.setlabel("");
                    dado.setCommand(parts[0]);
                }else if (parts.length >= 1) {
                    dado.setlabel(parts[0]);
                }
                if (line.startsWith(" ") && parts.length >= 2) {
                    dado.setlabel("");
                    dado.setCommand(parts[0]);
                    dado.setParameter1(parts[1]);
                }else if (parts.length >= 2){
                    dado.setlabel(parts[0]);
                    dado.setCommand(parts[1]);
                }
                if (line.startsWith(" ") && parts.length >= 3) {
                    dado.setlabel("");
                    dado.setCommand(parts[0]);
                    dado.setParameter1(parts[1]);
                    dado.setParameter2(parts[2]);
                }

                data.add(dado);
            }
        }catch (IOException e){
            e.printStackTrace();
        }

        codeArea.setItems(data);
    }

    @FXML
    public void runMethod() throws IOException {
        maquinaVirtual.execute();
    }

    public void openMethod() throws IOException {
        Stage stage = new Stage();

        FileChooser chooser = new FileChooser();

        chooser.setTitle("Open File");

        selectedFile = chooser.showOpenDialog(stage);
        maquinaVirtual = new MaquinaVirtual(new LineNumberReader(new FileReader(selectedFile.getAbsolutePath())));
        maquinaVirtual.loadPC();
        initializeTable();
    }
}