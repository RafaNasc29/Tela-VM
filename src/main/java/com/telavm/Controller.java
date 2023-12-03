package com.telavm;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;


import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
    @FXML
    public TextArea output;

//    @FXML
//    private TableColumn<Dado, String> comment;
    private MaquinaVirtual maquinaVirtual;

    @FXML
    public void initializeTable() {

        output.setEditable(false);
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
    public void runMethod() {
        if (output == null){
            maquinaVirtual.execute();
        }else {
            output.clear();
            maquinaVirtual.execute();
        }
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

    public void saida(String saida){
        output.appendText(saida);
    }

    public class MaquinaVirtual {
        private LineNumberReader lr;
        private List<Instrucao> program;
        private HashMap<String, Integer> labelMemory;
        private Integer[] memory;

        public MaquinaVirtual(LineNumberReader lr) {
            this.lr = lr;
        }

        public void loadPC() throws IOException {
            var line = lr.readLine();
            program = new ArrayList<>();
            labelMemory = new HashMap<>();
            while (line != null) {
                Instrucao instrucao = new Instrucao(line);
                program.add(instrucao);

                if (instrucao.getComando().equals("NULL")) {
                    labelMemory.put(instrucao.getLabel(), program.indexOf(instrucao));
                }

                line = lr.readLine();
            }
        }

        public void execute() {
            memory = new Integer[1000];
            int memory_pointer = 0;
            int pc = 0;
            for (; pc < program.size(); pc++) {
                Instrucao instrucao = program.get(pc);
                if (instrucao.getComando().equals("START")) {
                    memory_pointer += 1;
                } else if (instrucao.getComando().equals("LDC")) {
                    Integer op1 = Integer.parseInt(instrucao.getParameter1());

                    memory_pointer += 1;
                    memory[memory_pointer] = op1;
                } else if (instrucao.getComando().equals("LDV")) {
                    Integer op1 = Integer.parseInt(instrucao.getParameter1());

                    memory_pointer += 1;
                    memory[memory_pointer] = memory[op1];
                } else if (instrucao.getComando().equals("ADD")) {
                    Integer op1 = memory[memory_pointer - 1];
                    Integer op2 = memory[memory_pointer];

                    memory[memory_pointer - 1] = op1 + op2;
                    memory_pointer -= 1;
                } else if (instrucao.getComando().equals("SUB")) {
                    Integer op1 = memory[memory_pointer - 1];
                    Integer op2 = memory[memory_pointer];

                    memory[memory_pointer - 1] = op1 - op2;
                    memory_pointer -= 1;
                } else if (instrucao.getComando().equals("MULT")) {
                    Integer op1 = memory[memory_pointer - 1];
                    Integer op2 = memory[memory_pointer];

                    memory[memory_pointer - 1] = op1 * op2;
                    memory_pointer -= 1;
                } else if (instrucao.getComando().equals("DIVI")) {
                    Integer op1 = memory[memory_pointer - 1];
                    Integer op2 = memory[memory_pointer];

                    memory[memory_pointer - 1] = op1 / op2;
                    memory_pointer -= 1;
                } else if (instrucao.getComando().equals("INV")) {
                    memory[memory_pointer] = -memory[memory_pointer];
                } else if (instrucao.getComando().equals("AND")) {
                    if (memory[memory_pointer - 1] == 1 && memory[memory_pointer] == 1) {
                        memory[memory_pointer - 1] = 1;
                    } else {
                        memory[memory_pointer - 1] = 0;
                    }
                    memory_pointer -= 1;
                } else if (instrucao.getComando().equals("OR")) {
                    if (memory[memory_pointer - 1] == 1 || memory[memory_pointer] == 1) {
                        memory[memory_pointer - 1] = 1;
                    } else {
                        memory[memory_pointer - 1] = 0;
                    }
                    memory_pointer -= 1;
                } else if (instrucao.getComando().equals("NEG")) {
                    memory[memory_pointer] = 1 - memory[memory_pointer];
                } else if (instrucao.getComando().equals("CME")) {
                    int op1 = memory[memory_pointer - 1];
                    int op2 = memory[memory_pointer];

                    if (op1 < op2) {
                        memory[memory_pointer - 1] = 1;
                    } else {
                        memory[memory_pointer - 1] = 0;
                    }
                    memory_pointer -= 1;
                } else if (instrucao.getComando().equals("CMA")) {
                    int op1 = memory[memory_pointer - 1];
                    int op2 = memory[memory_pointer];

                    if (op1 > op2) {
                        memory[memory_pointer - 1] = 1;
                    } else {
                        memory[memory_pointer - 1] = 0;
                    }
                    memory_pointer -= 1;
                } else if (instrucao.getComando().equals("CEQ")) {
                    int op1 = memory[memory_pointer - 1];
                    int op2 = memory[memory_pointer];

                    if (op1 == op2) {
                        memory[memory_pointer - 1] = 1;
                    } else {
                        memory[memory_pointer - 1] = 0;
                    }
                    memory_pointer -= 1;
                } else if (instrucao.getComando().equals("CDIF")) {
                    int op1 = memory[memory_pointer - 1];
                    int op2 = memory[memory_pointer];

                    if (op1 != op2) {
                        memory[memory_pointer - 1] = 1;
                    } else {
                        memory[memory_pointer - 1] = 0;
                    }
                    memory_pointer -= 1;
                } else if (instrucao.getComando().equals("CMEQ")) {
                    int op1 = memory[memory_pointer - 1];
                    int op2 = memory[memory_pointer];

                    if (op1 <= op2) {
                        memory[memory_pointer - 1] = 1;
                    } else {
                        memory[memory_pointer - 1] = 0;
                    }
                    memory_pointer -= 1;
                } else if (instrucao.getComando().equals("CMAQ")) {
                    int op1 = memory[memory_pointer - 1];
                    int op2 = memory[memory_pointer];

                    if (op1 >= op2) {
                        memory[memory_pointer - 1] = 1;
                    } else {
                        memory[memory_pointer - 1] = 0;
                    }
                    memory_pointer -= 1;
                } else if (instrucao.getComando().equals("STR")) {
                    int op = Integer.parseInt(instrucao.getParameter1());
                    memory[op] = memory[memory_pointer];
                    memory_pointer -= 1;
                } else if (instrucao.getComando().equals("JMP")) {
                    pc = labelMemory.get(instrucao.getParameter1());
                } else if (instrucao.getComando().equals("JMPF")) {
                    if (memory[memory_pointer] == 0) {
                        pc = labelMemory.get(instrucao.getParameter1());
                    }
                    memory_pointer -= 1;
                } else if (instrucao.getComando().equals("HLT")) {
                    return;
                } else if (instrucao.getComando().equals("ALLOC")) {
                    int op1 = Integer.parseInt(instrucao.getParameter1());
                    int op2 = Integer.parseInt(instrucao.getParameter2());
                    for (int k = 0; k < op2; k++) {
                        memory_pointer += 1;
                        memory[memory_pointer] = memory[op1 + k];
                    }
                } else if (instrucao.getComando().equals("DALLOC")) {
                    int op1 = Integer.parseInt(instrucao.getParameter1());
                    int op2 = Integer.parseInt(instrucao.getParameter2());
                    for (int k = op2-1; k > -1 ; k--) {
                        memory[op1 + k] = memory[memory_pointer];
                        memory_pointer -= 1;
                    }
                } else if (instrucao.getComando().equals("CALL")) {
                    memory_pointer += 1;
                    memory[memory_pointer] = pc + 1;
                    pc = labelMemory.get(instrucao.getParameter1());
                } else if (instrucao.getComando().equals("RETURN")) {
                    pc = memory[memory_pointer] - 1;
                    memory_pointer -= 1;
                } else if (instrucao.getComando().equals("RETURNF")) {
                    pc = memory[memory_pointer] - 1;

                    memory[memory_pointer] = memory[0];
                } else if (instrucao.getComando().equals("RD")) {
                    TextInputDialog td = new TextInputDialog();
                    td.showAndWait();

                    memory_pointer+=1;
                    memory[memory_pointer] = Integer.parseInt(td.getEditor().getText());
                } else if (instrucao.getComando().equals("PRN")) {
                    saida(memory[memory_pointer] + "\n");
                    memory_pointer-=1;
                }
            }
        }
    }
}