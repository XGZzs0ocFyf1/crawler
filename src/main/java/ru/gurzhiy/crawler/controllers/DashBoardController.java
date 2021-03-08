package ru.gurzhiy.crawler.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;


//http://localhost:9080/dictionary_search/
@Controller
@RequestMapping("dictionary_search")
public class DashBoardController {

    //todo: протестируй меня с мокито
    @GetMapping("/")
    public String getPage(){
        return "userForm";
    }
}
