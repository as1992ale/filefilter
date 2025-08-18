package com.filefilter;

import java.util.EnumMap;
import java.util.Map;

public class DataStatistics {
    private static class TypeStats {
        int count = 0;
        long intSum = 0;
        double floatSum = 0.0;
        long minInt = Long.MAX_VALUE;
        long maxInt = Long.MIN_VALUE;
        double minFloat = Double.MAX_VALUE;
        double maxFloat = Double.MIN_VALUE;
        int minStringLength = Integer.MAX_VALUE;
        int maxStringLength = 0;
    }

    private final Map<DataType, TypeStats> statsMap = new EnumMap<>(DataType.class);

    public void updateStats(DataType type, String value) {
        TypeStats stats = statsMap.computeIfAbsent(type, k -> new TypeStats());
        stats.count++;

        switch (type) {
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
            case STRING:
                int length = value.length();
                stats.minStringLength = Math.min(stats.minStringLength, length);
                stats.maxStringLength = Math.max(stats.maxStringLength, length);
                break;
        }
    }

    public void displayShortStats() {
        System.out.println("\n=== Short Statistics ===");
        statsMap.forEach((type, stats) ->
                System.out.printf("%s: %d items%n", type, stats.count));
    }

    public void displayFullStats() {
        System.out.println("\n=== Full Statistics ===");
        statsMap.forEach((type, stats) -> {
            System.out.printf("\n%s (%d items):%n", type, stats.count);
            switch (type) {
                case INTEGER:
                    System.out.printf("  Min: %d%n", stats.minInt);
                    System.out.printf("  Max: %d%n", stats.maxInt);
                    System.out.printf("  Sum: %d%n", stats.intSum);
                    System.out.printf("  Avg: %.2f%n",
                            stats.count > 0 ? (double)stats.intSum / stats.count : 0);
                    break;
                case FLOAT:
                    System.out.printf("  Min: %f%n", stats.minFloat);
                    System.out.printf("  Max: %f%n", stats.maxFloat);
                    System.out.printf("  Sum: %f%n", stats.floatSum);
                    System.out.printf("  Avg: %f%n",
                            stats.count > 0 ? stats.floatSum / stats.count : 0);
                    break;
                case STRING:
                    System.out.printf("  Shortest: %d chars%n", stats.minStringLength);
                    System.out.printf("  Longest: %d chars%n", stats.maxStringLength);
                    break;
            }
        });
    }
}