package com.llu25.paperweb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@CrossOrigin(origins = "*")
@RestController
@SpringBootApplication
public class PaperWebApplication {

    private Map<NewsType, LRU<List<News>>> news;
    private Map<String, LRU<List<News>>> searchHistory;

    public PaperWebApplication() {
        news = new HashMap<>();
        searchHistory = new HashMap<>();
        Timer timer = new Timer();
        timer.schedule(new UpdateNewsService(this), 0, 10000000);//1000 Min
    }

    @GetMapping("/getLatestNews")
    @ResponseBody
    public List<News> getLatestNews(@RequestParam int id) {
        if (!news.containsKey(NewsType.ALL)) return new LinkedList<>();
        List<News> list = news.get(NewsType.ALL).get(id);
        return list == null ? new LinkedList<>() : list;
    }

    public Map<NewsType, LRU<List<News>>> getNews() {
        return news;
    }

    public Map<String, LRU<List<News>>> getSearchHistory() {
        return searchHistory;
    }

    public static void main(String[] args) {
        SpringApplication.run(PaperWebApplication.class, args);
    }
}
