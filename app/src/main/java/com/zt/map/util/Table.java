package com.zt.map.util;

public class Table {
    private String name;
    private String value;

    public Table(String name) {
        this.name = name;
    }

    public Table(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
