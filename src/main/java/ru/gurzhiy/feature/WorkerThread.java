package ru.gurzhiy.feature;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.gurzhiy.crawler.Utils;
import ru.gurzhiy.crawler.concurrent.Pair;

import java.util.List;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicBoolean;


public class WorkerThread implements Runnable {

    private final static Logger log = LoggerFactory.getLogger(WorkerThread.class);
    private Queue<Pair> queue;
    private List<Pair> list;
    private AtomicBoolean eof = new AtomicBoolean();
    private final String request;



    public WorkerThread(Queue<Pair> queue, List<Pair> list, String request, AtomicBoolean eof) {
        this.queue = queue;
        this.list = list;
        this.request = request;
        this.eof = eof;
    }




    @Override
    public void run() {

        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        log.info(" {} запущен ", Thread.currentThread().getName());
        while (true) {
            try {
                if (queue.size() == 0) {
                    //очередь пустая ждем пока появятся данные в ней
                    Thread.sleep(1);
                 //   log.info(Thread.currentThread().getName() + " поспал ...");
                } else {
                    Pair pair = queue.poll();
                //    log.info(Thread.currentThread().getName() + " забрал 1 элемент из очереди. queue size {}",
                  //          queue.size());
                    if (pair != null) {
                        String line = pair.getLine();
                        int relevantCriterion = Utils.getRelevantCriteriaForRequestAndLine(request, line);
                        if (relevantCriterion > 2) {
                    //        log.info("{} :КР больше 2 ({}). добавляю в список. в " +
                     //               "списке: {}", Thread.currentThread().getName(), relevantCriterion, list.size());
                            list.add(new Pair(relevantCriterion, line));
                        }
                        //  list.add(queue.poll());
//                       log.info("добавляю в список в списке : {}", list.size());
                    }


                }

                if (eof.get() && queue.size() == 0) {
//                    log.info("EOF флаг выставлен");
//                    log.info("EOF флаг выставлен");
//                    log.info("EOF флаг выставлен");
//                    log.info("EOF флаг выставлен");
                    log.info("EOF флаг выставлен");
                    log.info("размер очереди {}", queue.size());

                    break;
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

    }
}
