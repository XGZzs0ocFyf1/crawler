package ru.gurzhiy.crawler.concurrent;

import ru.gurzhiy.crawler.benchmark.DataProducer;

import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class Concurrent {

    static ConcurrentLinkedQueue<Pair> queue = new ConcurrentLinkedQueue<>();
    static ExecutorService pool = Executors.newFixedThreadPool(10);
    static List<Pair> pairs = new CopyOnWriteArrayList<>();
    private static AtomicInteger workerIdx = new AtomicInteger();
    private static AtomicBoolean eof = new AtomicBoolean();
    private static String filename = "D:\\fireField\\resultShort.txt";


    public static void main(String[] args) {

        long t1 = System.currentTimeMillis();
        eof.set(false);

        String request = "конь не валялся";

        pool.execute(new DataProducer(filename, queue, eof));
        pool.execute(new Worker("W"+workerIdx.incrementAndGet(), request, queue, pairs, eof));
        pool.execute(new Worker("W"+workerIdx.incrementAndGet(), request, queue, pairs, eof));
        pool.execute(new Worker("W"+workerIdx.incrementAndGet(), request, queue, pairs, eof));
        pool.execute(new Worker("W"+workerIdx.incrementAndGet(), request, queue, pairs, eof));





        if (eof.get()){
            long t2 = System.currentTimeMillis();
        }else{
            System.out.println("not eof");
        }
        pool.shutdown();

       // System.out.println("поиск выражения закончен. время поиска "+(t2-t1)+"мс");
      //  pairs.forEach(x-> System.out.println(x));
//            List<Future<Pair>> futures = executor.invokeAll(Arrays.asList(
//                    new DataProducer(queue),
//                    new Worker(request, queue, pairs),
//                    new Worker(request, queue, pairs))
//
//            );
//
//            executor.shutdown();


    }
}
