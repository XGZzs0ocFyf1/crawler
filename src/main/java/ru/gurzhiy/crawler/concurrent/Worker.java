package ru.gurzhiy.crawler.concurrent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.gurzhiy.crawler.Utils;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

public class Worker implements Runnable {
    private final static Logger log = LoggerFactory.getLogger(Worker.class);

    private ConcurrentLinkedQueue<Pair> queue;
    private List<Pair> pairs;
    private String name;
    private final String request;
    private AtomicBoolean eof;

    public Worker(String name, String request, ConcurrentLinkedQueue queue, List<Pair> pairs, AtomicBoolean eof) {
        this.name = name;
        this.queue = queue;
        this.request = request;
        this.pairs = pairs;
        this.eof = eof;
    }

    @Override
    public void run() {
        log.info("запущен поток worker  [{}]", name);

        while (!eof.get()){
            if (queue.size() == 0){
                try {
                    Thread.sleep(4);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            String line = null;

            Pair pair = queue.poll();
            if (pair == null) {
//                log.info("Очередь пуста!!!!");
            } else {
//                log.info("Сохраняю строку из Pair");
                line = pair.getLine();
                int relevanceCriterion = Utils.getRelevantCriteriaForRequestAndLine(request, line);

                if (relevanceCriterion > 2) {
                    pairs.add(new Pair(relevanceCriterion, line));
                }
            }

        }




    }
}
