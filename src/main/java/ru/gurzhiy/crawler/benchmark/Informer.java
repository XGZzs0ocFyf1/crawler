package ru.gurzhiy.crawler.benchmark;

public class Informer {
    public static void informer() {
        Runtime runtime = Runtime.getRuntime();
        long totalMemory = runtime.totalMemory();
        long freeMemory = runtime.freeMemory();
        long usedMemory = totalMemory - freeMemory;
        long maxMemory = runtime.maxMemory();

        System.out.println("totalmemory " + (totalMemory/1024/1024)+"Mb; " + totalMemory);
        System.out.println("freeMemory " + (freeMemory/1024/1024)+"Mb; " +freeMemory);
        System.out.println("usedMemory " + (usedMemory/1024/1024)+"Mb; " +usedMemory);
        System.out.println("maxMemory " + (maxMemory/1024/1024)+"Mb; " +maxMemory);
    }
}
