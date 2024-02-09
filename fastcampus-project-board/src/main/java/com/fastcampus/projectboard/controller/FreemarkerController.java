package com.fastcampus.projectboard.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.List;


import java.util.Map;

@RequestMapping("/prac") //localhost:8080/practice/welcome
@Controller
public class FreemarkerController {
    @GetMapping("/welcome")
    public String hello(Map model){
        model.put("message", "hello freemarker!");
        return "practice";
    }

//    @GetMapping("/articles")
//    public String articles(Map model){
//        model.put("articles", List.of());
//        return "articles/index";
//    }



}
