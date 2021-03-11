package ru.gurzhiy.crawler.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.gurzhiy.crawler.benchmark.DataProducer;
import ru.gurzhiy.crawler.concurrent.Pair;
import ru.gurzhiy.crawler.service.DictionaryLookupService;
import ru.gurzhiy.feature.WorkerThread;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;


/**
 * для локального запуска URL http://localhost:9080/dictionary_search/
 */


@Controller
@RequestMapping("dictionary_search")
public class DashBoardController {

    private final static Logger log = LoggerFactory.getLogger(DashBoardController.class);
    private DictionaryLookupService service;

    public DashBoardController(DictionaryLookupService service) {
        this.service = service;
    }



    //медленный
//    @GetMapping("/")
//    public String getPage(@RequestParam(name = "q", required = false, defaultValue = "конь не валялся") String request,
//                          Model model) throws ExecutionException, InterruptedException {
//
//        CompletableFuture<List<Pair>>  results = service.dictionaryLookup(request, "D:\\fireField\\result.txt");
//        List<Pair> pairs = results.get();
//        model.addAttribute("list", pairs);
//        return "userForm";
//
//    }

}
