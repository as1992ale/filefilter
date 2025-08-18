package com.filefilter;

import com.filefilter.exceptions.InvalidInputException;
import java.util.ArrayList;
import java.util.List;

public class CommandLineParser {
    private String outputPath = ".";
    private String filePrefix = "";
    private boolean appendMode = false;
    private boolean shortStats = false;
    private boolean fullStats = false;
    private final List<String> inputFiles = new ArrayList<>();

    public CommandLineParser(String[] args) throws InvalidInputException {
        parseArguments(args);
    }

    private void parseArguments(String[] args) throws InvalidInputException {
        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "-o":
                    if (i + 1 >= args.length) {
                        throw new InvalidInputException("Missing path after -o option");
                    }
                    outputPath = args[++i];
                    break;
                case "-p":
                    if (i + 1 >= args.length) {
                        throw new InvalidInputException("Missing prefix after -p option");
                    }
                    filePrefix = args[++i];
                    break;
                case "-a":
                    appendMode = true;
                    break;
                case "-s":
                    shortStats = true;
                    break;
                case "-f":
                    fullStats = true;
                    break;
                default:
                    if (args[i].startsWith("-")) {
                        throw new InvalidInputException("Unknown option: " + args[i]);
                    }
                    inputFiles.add(args[i]);
            }
        }

        if (inputFiles.isEmpty()) {
            throw new InvalidInputException("No input files specified");
        }
    }

    // Getters
    public String getOutputPath() { return outputPath; }
    public String getFilePrefix() { return filePrefix; }
    public boolean isAppendMode() { return appendMode; }
    public boolean showShortStats() { return shortStats; }
    public boolean showFullStats() { return fullStats; }
    public List<String> getInputFiles() { return inputFiles; }
}