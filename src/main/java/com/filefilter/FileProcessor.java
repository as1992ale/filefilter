package com.filefilter;

import com.filefilter.exceptions.FileProcessingException;
import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.regex.Pattern;

public class FileProcessor {
    private final String outputPath;
    private final String filePrefix;
    private final boolean appendMode;
    private final DataStatistics statistics = new DataStatistics();

    private static final Pattern INTEGER_PATTERN = Pattern.compile("^-?\\d+$");
    private static final Pattern FLOAT_PATTERN = Pattern.compile("^-?\\d+\\.\\d+([Ee][+-]?\\d+)?$");

    public FileProcessor(String outputPath, String filePrefix, boolean appendMode)
            throws FileProcessingException {
        this.outputPath = outputPath;
        this.filePrefix = filePrefix;
        this.appendMode = appendMode;

        createOutputDirectory();
    }

    private void createOutputDirectory() throws FileProcessingException {
        try {
            Files.createDirectories(Paths.get(outputPath));
        } catch (IOException e) {
            throw new FileProcessingException("Failed to create output directory: " + outputPath, e);
        }
    }

    public void processFiles(List<String> inputFiles) throws FileProcessingException {
        Map<DataType, BufferedWriter> writers = new EnumMap<>(DataType.class);

        try {
            for (String inputFile : inputFiles) {
                processSingleFile(inputFile, writers);
            }
        } finally {
            closeAllWriters(writers);
        }
    }

    private void processSingleFile(String inputFile, Map<DataType, BufferedWriter> writers)
            throws FileProcessingException {
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                processLine(line, writers);
            }
        } catch (IOException e) {
            throw new FileProcessingException("Error processing file: " + inputFile, e);
        }
    }

    private void processLine(String line, Map<DataType, BufferedWriter> writers)
            throws FileProcessingException {
        if (line.trim().isEmpty()) return;

        DataType type = determineDataType(line);
        statistics.updateStats(type, line);

        try {
            writeToFile(type, line, writers);
        } catch (IOException e) {
            throw new FileProcessingException("Error writing to output file", e);
        }
    }

    private DataType determineDataType(String line) {
        if (INTEGER_PATTERN.matcher(line).matches()) {
            return DataType.INTEGER;
        } else if (FLOAT_PATTERN.matcher(line).matches()) {
            return DataType.FLOAT;
        }
        return DataType.STRING;
    }

    private void writeToFile(DataType type, String line, Map<DataType, BufferedWriter> writers)
            throws IOException {
        BufferedWriter writer = writers.computeIfAbsent(type, this::createWriter);
        if (writer != null) {
            writer.write(line);
            writer.newLine();
        }
    }

    private BufferedWriter createWriter(DataType type) {
        try {
            String filename = outputPath + File.separator + filePrefix + type.getFilename();
            return new BufferedWriter(new FileWriter(filename, appendMode));
        } catch (IOException e) {
            System.err.println("Failed to create writer for " + type + ": " + e.getMessage());
            return null;
        }
    }

    private void closeAllWriters(Map<DataType, BufferedWriter> writers) {
        writers.values().forEach(writer -> {
            try {
                if (writer != null) writer.close();
            } catch (IOException e) {
                System.err.println("Error closing file writer: " + e.getMessage());
            }
        });
    }

    public DataStatistics getStatistics() {
        return statistics;
    }
}