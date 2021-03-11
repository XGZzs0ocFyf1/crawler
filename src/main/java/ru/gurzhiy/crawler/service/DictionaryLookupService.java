package ru.gurzhiy.crawler.service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import ru.gurzhiy.crawler.benchmark.DataProducer;
import ru.gurzhiy.crawler.concurrent.Pair;
import ru.gurzhiy.feature.WorkerThread;

import java.util.List;
import java.util.Queue;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
public class DictionaryLookupService {



    private static final Logger log = LoggerFactory.getLogger(DictionaryLookupService.class);

    @Autowired
    @Qualifier("crawlerPool")
    public ThreadPoolTaskExecutor executor;

    @Async
    public CompletableFuture<List<Pair>> dictionaryLookup(String request, String filename){
        /**
         * Отдельная очередь для чтения queue и буфер результатов output
         */
        Queue<Pair> queue = new LinkedBlockingQueue<>();
        List<Pair> output = new CopyOnWriteArrayList<>();
        AtomicBoolean eof = new AtomicBoolean();
        int numberOfWorkerThreads = 3;

        long start = System.currentTimeMillis();

        log.info(Thread.currentThread().getName()+" Looking up \"{}\" ", request);

        executor.execute(new DataProducer(filename, queue, eof));

        for (int i = 0; i < numberOfWorkerThreads; i++) {
            executor.execute(new WorkerThread(queue, output, request, eof));

        }

        while (true){
            if (executor.getActiveCount() == 0){
                long stop = System.currentTimeMillis();
                log.info("Время поиска выражения {} составило: {}", request, (stop-start));
                break;
            }
        }

        return CompletableFuture.completedFuture(output);
    }


}
