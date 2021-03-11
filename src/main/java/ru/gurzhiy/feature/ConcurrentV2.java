package ru.gurzhiy.feature;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.gurzhiy.crawler.Utils;
import ru.gurzhiy.crawler.benchmark.DataProducer;
import ru.gurzhiy.crawler.benchmark.Informer;
import ru.gurzhiy.crawler.concurrent.Pair;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;


public class ConcurrentV2 {

    private static String filename = "D:\\fireField\\result.txt";
    private static AtomicBoolean eof = new AtomicBoolean();
    private static List<Pair> list = new CopyOnWriteArrayList<>();
    private static final Logger log = LoggerFactory.getLogger(ConcurrentV2.class);
    private static String request = "конь не валялся";
    private static int bufferSize = (int) (Math.pow(2, 9));


      private static Queue<Pair> queue = new LinkedBlockingQueue<>(); // На 50 мб файле 1234 ms ; на  1.8 гб 33c
//     private static Queue<Pair> queue = new LinkedBlockingQueue<>(20000); // На 50 мб файле 1234 ms ; на  1.8 гб 33
    // мс, 28мс; на 4х потоках плюс мейн обрабатывает 1.8 гб за 24 мс - 27 мс (по 4м замерам)
    //private static Queue<Pair> queue = new ConcurrentLinkedQueue<>();
   // private static Queue<Pair> queue = new ArrayBlockingQueue<>(25000); // 1157 ms на 50 мб, 60 с на 1.8 гб файле
    // (4
    // потока + мейн)


    //produser, w3, w4, w5
    //628879 ms
    //размер списка 5


    // w3, w4, w5
    //603952 ms
    //размер списка 5


    /**
     *
     * totalmemory 109Mb; 114819072
     * freeMemory 86Mb; 90253968
     * usedMemory 23Mb; 24565104
     * maxMemory 1803Mb; 1890582528
     */
    public static void main(String[] args) {

      //  log.info("группа нитей {}", Thread.currentThread().getThreadGroup().getName());
        Thread.currentThread().getThreadGroup().list();
     //   log.info("activeGroupCount {}", Thread.currentThread().getThreadGroup().activeGroupCount());
      //  log.info("getMaxPriority {}", Thread.currentThread().getThreadGroup().getMaxPriority());
        long t1 = System.currentTimeMillis();
        eof.set(false);

        Thread.currentThread().setName("reader");

//
//        ExecutorService service = Executors.newFixedThreadPool(4);
//        service.

        Thread w3 = new Thread(new WorkerThread(queue, list, request, eof));
        w3.setName("w3");
        Thread w4 = new Thread(new WorkerThread(queue, list, request, eof));
        w4.setName("w4");
        Thread w5 = new Thread(new WorkerThread(queue, list, request, eof));
        w5.setName("w5");
        Thread w6 = new Thread(new WorkerThread(queue, list, request, eof));
        w6.setName("w6");

        w3.start();
        w4.start();
        w5.start();
        w6.start();


//        DataProducer producer = new DataProducer(filename, queue, eof);
// Thread producerThread = new Thread(producer);

        long tr1 = System.currentTimeMillis();
        //  log.info("Запущен поток DataProducer");
        try (
                FileInputStream fis = new FileInputStream(filename);
                InputStreamReader isr = new InputStreamReader(fis, StandardCharsets.UTF_8);
                BufferedReader br = new BufferedReader(isr, bufferSize)) {

            for (String line = null; (line = br.readLine()) != null; ) {
                queue.offer(new Pair(0, line));
              //     log.info("queue size {}", queue.size());

               // Informer.informer();
            }
            long tr2 = System.currentTimeMillis();
        //    log.info("Время чтения файла {} миллисекунд", (tr2 - tr1));
            eof.set(true);
        //    log.info("Set eof to {}", eof.get());
        //    log.info("queue size {}", queue.size());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Informer.informer();

//        list =   queue
//                .stream()      // Без параллельности 2984 ms
////                .parallel() //: с параллельностью 2547 ms
//                .filter( x-> {
//                    int criteria = 0 ;
//                    if ( ( criteria = Utils.getRelevantCriteriaForRequestAndLine(request, x.getLine())) >2){
//                        x.setRelevanceCriteria(criteria);
//                        return true;
//                    }
//                    return false;
//                } )
//                .collect(Collectors.toList());


        // producerThread.start();
        //w1.start();
        // w2.start();


        try {
            //           producerThread.join();
//            w1.join();
//            w2.join();
            w3.join();
            w4.join();
            w5.join();
            w6.join();
            log.info("Ждем завершение нити : producer");
            long t2 = System.currentTimeMillis();
            log.info("программа работала : {} ms", (t2 - t1));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("размер списка " + list.size());
        System.out.println("размер очереди " + queue.size());
        list.stream().skip(0).limit(10).forEach(x -> System.out.println(x.getRelevanceCriteria() + " " + x.getLine().trim()));


    }
}
