package ru.gurzhiy.crawler.configuration;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.AsyncListenableTaskExecutor;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
import ru.gurzhiy.crawler.concurrent.Pair;

import java.util.concurrent.Callable;

public class MyBean {

    @Autowired
    private AsyncListenableTaskExecutor executor;
    @Autowired
    private ListenableFutureCallback threadListenableCallback;


   public void runTasks(){
       ListenableFuture<Pair> f = executor.submitListenable(() -> new Pair(0,""));

   }


}
