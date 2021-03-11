package ru.gurzhiy.crawler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import ru.gurzhiy.crawler.concurrent.Pair;
import ru.gurzhiy.crawler.service.DictionaryLookupService;

import java.util.List;

//@Component
public class CLi implements CommandLineRunner {
    private static String filename = "D:\\fireField\\result.txt";

    @Autowired
    private DictionaryLookupService service;

    @Override
    public void run(String... args) throws Exception {
//        List<Pair> output =  service.dictionaryLookup("конь не валялся", filename).join();
//        output.stream().forEach(x-> System.out.println(x.getRelevanceCriteria()+ " " +x.getLine()));
    }
}
