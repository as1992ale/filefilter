package com.filefilter;

public enum DataType {
    INTEGER("integers.txt"),
    FLOAT("floats.txt"),
    STRING("strings.txt");

    private final String filename;

    DataType(String filename) {
        this.filename = filename;

    }

    public String getFilename() {
        return filename;
    }
}
