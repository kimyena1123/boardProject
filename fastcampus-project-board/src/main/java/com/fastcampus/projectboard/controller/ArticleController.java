package com.fastcampus.projectboard.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;
import java.util.List;


/*
/articles
/articles/{article-id}
/articles/search
/articles/search-hashtag
 */
@RequestMapping("/articles")
@Controller
public class ArticleController {

//    @GetMapping //localhost:8080/articles
//    public String articles(Map model){
//        model.put("articles", List.of());
//        return "articles/index";
//    }

    @GetMapping
    public String articles(ModelMap map){
        map.put("articles", List.of());
        return "articles/index";
    }

    @GetMapping("/{articleId}")
    public String article(@PathVariable(name = "articleId") Long articleId, ModelMap map) {
        map.addAttribute("article", null);
        map.addAttribute("articleComment", List.of());

        return "articles/detail";
    }



}
