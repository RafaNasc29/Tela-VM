package com.telavm;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;


import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
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
    private TextArea output;
    @FXML
    private ToggleGroup radioButtons;

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
        if (maquinaVirtual.pc == 0){
            output.clear();
        }
        RadioButton selected = (RadioButton) radioButtons.getSelectedToggle();
        if(selected.getText().equals("Normal")) {
            maquinaVirtual.execute();
        }
        else{
            maquinaVirtual.stepBystep();
            codeArea.getSelectionModel().select(maquinaVirtual.pc);
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
        private final LineNumberReader lr;
        private List<Instrucao> program;
        private HashMap<String, Integer> labelMemory;
        private Integer[] memory;
        private int pc;
        private int memory_pointer;
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

        public void stepBystep(){
            if(this.pc == 0){
                memory = new Integer[100];
                Arrays.fill(memory,0);
                this.memory_pointer = 0;
            }
            Instrucao instrucao = program.get(this.pc);
            if (instrucao.getComando().equals("START")) {
                this.memory_pointer += 1;
            } else if (instrucao.getComando().equals("LDC")) {
                Integer op1 = Integer.parseInt(instrucao.getParameter1());

                this.memory_pointer += 1;
                memory[this.memory_pointer] = op1;
            } else if (instrucao.getComando().equals("LDV")) {
                Integer op1 = Integer.parseInt(instrucao.getParameter1());

                this.memory_pointer += 1;
                memory[this.memory_pointer] = memory[op1];
            } else if (instrucao.getComando().equals("ADD")) {
                Integer op1 = memory[this.memory_pointer - 1];
                Integer op2 = memory[this.memory_pointer];

                memory[this.memory_pointer - 1] = op1 + op2;
                this.memory_pointer -= 1;
            } else if (instrucao.getComando().equals("SUB")) {
                Integer op1 = memory[this.memory_pointer - 1];
                Integer op2 = memory[this.memory_pointer];

                memory[this.memory_pointer - 1] = op1 - op2;
                this.memory_pointer -= 1;
            } else if (instrucao.getComando().equals("MULT")) {
                Integer op1 = memory[this.memory_pointer - 1];
                Integer op2 = memory[this.memory_pointer];

                memory[this.memory_pointer - 1] = op1 * op2;
                this.memory_pointer -= 1;
            } else if (instrucao.getComando().equals("DIVI")) {
                Integer op1 = memory[this.memory_pointer - 1];
                Integer op2 = memory[this.memory_pointer];

                memory[this.memory_pointer - 1] = op1 / op2;
                this.memory_pointer -= 1;
            } else if (instrucao.getComando().equals("INV")) {
                memory[this.memory_pointer] = -memory[this.memory_pointer];
            } else if (instrucao.getComando().equals("AND")) {
                if (memory[this.memory_pointer - 1] == 1 && memory[this.memory_pointer] == 1) {
                    memory[this.memory_pointer - 1] = 1;
                } else {
                    memory[this.memory_pointer - 1] = 0;
                }
                this.memory_pointer -= 1;
            } else if (instrucao.getComando().equals("OR")) {
                if (memory[this.memory_pointer - 1] == 1 || memory[this.memory_pointer] == 1) {
                    memory[this.memory_pointer - 1] = 1;
                } else {
                    memory[this.memory_pointer - 1] = 0;
                }
                this.memory_pointer -= 1;
            } else if (instrucao.getComando().equals("NEG")) {
                memory[this.memory_pointer] = 1 - memory[this.memory_pointer];
            } else if (instrucao.getComando().equals("CME")) {
                int op1 = memory[this.memory_pointer - 1];
                int op2 = memory[this.memory_pointer];

                if (op1 < op2) {
                    memory[this.memory_pointer - 1] = 1;
                } else {
                    memory[this.memory_pointer - 1] = 0;
                }
                this.memory_pointer -= 1;
            } else if (instrucao.getComando().equals("CMA")) {
                int op1 = memory[this.memory_pointer - 1];
                int op2 = memory[this.memory_pointer];

                if (op1 > op2) {
                    memory[this.memory_pointer - 1] = 1;
                } else {
                    memory[this.memory_pointer - 1] = 0;
                }
                this.memory_pointer -= 1;
            } else if (instrucao.getComando().equals("CEQ")) {
                int op1 = memory[this.memory_pointer - 1];
                int op2 = memory[this.memory_pointer];

                if (op1 == op2) {
                    memory[this.memory_pointer - 1] = 1;
                } else {
                    memory[this.memory_pointer - 1] = 0;
                }
                this.memory_pointer -= 1;
            } else if (instrucao.getComando().equals("CDIF")) {
                int op1 = memory[this.memory_pointer - 1];
                int op2 = memory[this.memory_pointer];

                if (op1 != op2) {
                    memory[this.memory_pointer - 1] = 1;
                } else {
                    memory[this.memory_pointer - 1] = 0;
                }
                this.memory_pointer -= 1;
            } else if (instrucao.getComando().equals("CMEQ")) {
                int op1 = memory[this.memory_pointer - 1];
                int op2 = memory[this.memory_pointer];

                if (op1 <= op2) {
                    memory[this.memory_pointer - 1] = 1;
                } else {
                    memory[this.memory_pointer - 1] = 0;
                }
                this.memory_pointer -= 1;
            } else if (instrucao.getComando().equals("CMAQ")) {
                int op1 = memory[this.memory_pointer - 1];
                int op2 = memory[this.memory_pointer];

                if (op1 >= op2) {
                    memory[this.memory_pointer - 1] = 1;
                } else {
                    memory[this.memory_pointer - 1] = 0;
                }
                this.memory_pointer -= 1;
            } else if (instrucao.getComando().equals("STR")) {
                int op = Integer.parseInt(instrucao.getParameter1());
                memory[op] = memory[this.memory_pointer];
                this.memory_pointer -= 1;
            } else if (instrucao.getComando().equals("JMP")) {
                this.pc = labelMemory.get(instrucao.getParameter1());
            } else if (instrucao.getComando().equals("JMPF")) {
                if (memory[this.memory_pointer] == 0) {
                    this.pc = labelMemory.get(instrucao.getParameter1());
                }
                this.memory_pointer -= 1;
            } else if (instrucao.getComando().equals("HLT")) {
                this.pc = 0;
                return;
            } else if (instrucao.getComando().equals("ALLOC")) {
                int op1 = Integer.parseInt(instrucao.getParameter1());
                int op2 = Integer.parseInt(instrucao.getParameter2());
                for (int k = 0; k < op2; k++) {
                    this.memory_pointer += 1;
                    memory[this.memory_pointer] = memory[op1 + k];
                }
            } else if (instrucao.getComando().equals("DALLOC")) {
                int op1 = Integer.parseInt(instrucao.getParameter1());
                int op2 = Integer.parseInt(instrucao.getParameter2());
                for (int k = op2-1; k > -1 ; k--) {
                    memory[op1 + k] = memory[this.memory_pointer];
                    this.memory_pointer -= 1;
                }
            } else if (instrucao.getComando().equals("CALL")) {
                this.memory_pointer += 1;
                memory[this.memory_pointer] = this.pc + 1;
                this.pc = labelMemory.get(instrucao.getParameter1());
            } else if (instrucao.getComando().equals("RETURN")) {
                this.pc = memory[this.memory_pointer] - 1;
                this.memory_pointer -= 1;
            } else if (instrucao.getComando().equals("RETURNF")) {
                this.pc = memory[this.memory_pointer] - 1;

                memory[this.memory_pointer] = memory[0];
            } else if (instrucao.getComando().equals("RD")) {
                TextInputDialog td = new TextInputDialog();
                td.showAndWait();

                this.memory_pointer+=1;
                memory[this.memory_pointer] = Integer.parseInt(td.getEditor().getText());
            } else if (instrucao.getComando().equals("PRN")) {
                saida(memory[this.memory_pointer] + "\n");
                this.memory_pointer-=1;
            }
            this.pc += 1;
        }

        public void execute() {
            this.pc = 0;
            int aux = this.pc;
            while(aux < program.size()-1) {
                aux = this.pc;
                stepBystep();
            }
        }
    }
}