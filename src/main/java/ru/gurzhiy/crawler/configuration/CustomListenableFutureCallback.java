package ru.gurzhiy.crawler.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.concurrent.ListenableFutureCallback;
import ru.gurzhiy.crawler.concurrent.Pair;



public class CustomListenableFutureCallback implements ListenableFutureCallback<Pair> {

    private final static Logger log = LoggerFactory.getLogger(CustomListenableFutureCallback.class);
    @Override
    public void onFailure(Throwable ex) {
        log.error("failed!");
        ex.printStackTrace();
    }

    @Override
    public void onSuccess(Pair result) {
        log.info("sucess");
    }
}
