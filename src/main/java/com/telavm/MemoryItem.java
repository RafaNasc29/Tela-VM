package com.telavm;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class MemoryItem {
    private final String address;
    private String value;

    public MemoryItem(String address, String value) {
        this.address = address;
        this.value = value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public StringProperty addressProperty() {
        return new SimpleStringProperty(address);
    }

    public StringProperty valueProperty() {
        return new SimpleStringProperty(value);
    }
}