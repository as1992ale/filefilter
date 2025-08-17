package com.filefilter;

import java.util.*;

public class DataStatistics {
    private static class TypeStats{
        int count = 0;
        int intSum = 0;
        double floatSum = 0.0;
        long minInt = Long.MAX_VALUE;
        long maxInt = Long.MIN_VALUE;
        double minFloat = Double.MAX_VALUE;
        double maxFloat = Double.MIN_VALUE;
        int minStringLength = Integer.MAX_VALUE;
        int maxStringLength = 0;

    }

    private final EnumMap<DataType, TypeStats> statsMap = new EnumMap<>(DataType.class);

    public void updateStats(DataType type, String value){
        TypeStats stats = statsMap.computeIfAbsent(type, k -> new TypeStats());
        stats.count++;

        switch (type){
            case INTEGER:
                long intVal = Long.parseLong(value);
                stats.intSum += intVal;
                stats.minInt = Math.min(stats.minInt, intVal);
                stats.maxInt = Math.max(stats.maxInt, intVal);
                break;
            case FLOAT:
                double floatVal = Double.parseDouble(value);
                stats.floatSum += floatVal;
                stats.minFloat = Math.min(stats.minFloat, floatVal);
                stats.maxFloat = Math.max(stats.maxFloat, floatVal);
                break;
            case STRONG:
                int length = value.length();
                stats.minStringLength = Math.min(stats.minStringLength, length);
                stats.maxStringLength = Math.max(stats.maxStringLength, length);
                break;
        }
    }

    public void displayShortsStats() {
        System.out.println("\n=== Summary Statistics ===");
        statsMap.forEach((type, stats) ->
                System.out.printf("%s: %d items%n", type, stats.count));
    }

    public void displayLongsStats() {
        System.out.println("\n=== Detailed Statistics ===");
        statsMap.forEach((type, stats) ->{
            System.out.printf("\n%s (%d items): %n", type, stats.count);
            switch (type){
                case INTEGER:
                    System.out.printf(" Min: %d%n ", stats.minInt);
                    System.out.printf(" Max: %d%n ", stats.maxInt);
                    System.out.printf(" Sum: %f%n ", stats.intSum);
                    System.out.printf(" Avg: %.2f%n ",
                            stats.count > 0 ? (double)stats.intSum / stats.count : 0);
                    break;
                case FLOAT:
                    System.out.printf(" Min: %f%n ", stats.minFloat);
                    System.out.printf(" Max: %f%n ", stats.maxFloat);
                    System.out.printf(" Sum: %f%n ", stats.floatSum);
                    System.out.printf(" Avg: %.2f%n ",
                            stats.count > 0 ? (double)stats.floatSum / stats.count : 0);
                    break;
                case STRING:
                    System.out.printf(" Shortest: %dchars%n ", stats.minStringLength);
                    System.out.printf(" Longest: %dchars%n ", stats.maxStringLength);
                    break;
            }
        });
    }
}
