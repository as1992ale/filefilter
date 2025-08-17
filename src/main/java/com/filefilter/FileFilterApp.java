package com.filefilter;

public class FileFilterApp {
    public static void main(String[] args) {
        try {
            // Parse command line arguments
            CommandLineParser cli = new CommandLineParser(args);

            // Process files with given parameters
            FileProcessor processor = new FileProcessor(
                    cli.getOutputPath(),
                    cli.getFilePrefix(),
                    cli.isAppendMode()
            );

            processor.processFiles(cli.getInputFiles());

            // Display statistics
            DataStatistics stats = processor.getStatistics();
            if (cli.showShortStats()) {
                stats.displayShortStats();
            } else if (cli.showFullStats()) {
                stats.displayFullStats();
            }

        } catch (InvalidInputException e) {
            System.err.println("Input error: " + e.getMessage());
        } catch (FileProcessingException e) {
            System.err.println("File processing error: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
        }
    }
}