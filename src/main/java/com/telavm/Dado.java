package com.telavm;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Dado {
    private StringProperty line = new SimpleStringProperty(this, "line");
    private StringProperty command = new SimpleStringProperty(this, "command");
    private StringProperty param1 = new SimpleStringProperty(this, "param1");
    private StringProperty param2 = new SimpleStringProperty(this, "param2");
    private StringProperty comment = new SimpleStringProperty(this, "comment");

    public String getLine() {
        return line.get();
    }

    public StringProperty lineProperty() {
        return line;
    }

    public void setLine(String line) {
        this.line.set(line);
    }

    public String getCommand() {
        return command.get();
    }

    public StringProperty commandProperty() {
        return command;
    }

    public void setCommand(String command) {
        this.command.set(command);
    }

    public String getParam1() {
        return param1.get();
    }

    public StringProperty param1Property() {
        return param1;
    }

    public void setParameter1(String param1) {
        this.param1.set(param1);
    }

    public String getParameter2() {
        return param2.get();
    }

    public StringProperty param2Property() {
        return param2;
    }

    public void setParameter2(String param2) {
        this.param2.set(param2);
    }

    public String getComment() {
        return comment.get();
    }

    public StringProperty commentProperty() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment.set(comment);
    }
}
