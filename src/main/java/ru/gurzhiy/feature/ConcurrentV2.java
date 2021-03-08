package ru.gurzhiy.feature;

import ru.gurzhiy.crawler.benchmark.DataProducer;
import ru.gurzhiy.crawler.concurrent.Pair;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public class ConcurrentV2 {

    private static String filename = "D:\\fireField\\resultShort.txt";
    private static AtomicBoolean eof = new AtomicBoolean();

    public static void main(String[] args) {


        eof.set(false);
        Queue<Pair> que = new ConcurrentLinkedQueue<>();
        DataProducer producer = new DataProducer(filename, que, eof);

        Thread producerThread = new Thread(producer);
        producerThread.start();

    }
}
