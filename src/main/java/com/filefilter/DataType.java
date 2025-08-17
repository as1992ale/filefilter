package com.filefilter;

public class DataType {
    INTEGER("integers.txt"),
    FLOAT("floats.txt"),
    STRING("strings.tst");

    private final String fileName;

    DataType(String fileName) {
        this.fileName = fileName;

    }

    public String getFileName() {
        return fileName;
    }
}
