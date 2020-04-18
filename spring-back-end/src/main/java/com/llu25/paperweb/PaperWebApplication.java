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
        timer.schedule(new UpdateNewsService(this), 0, 10000000); //1000 Min
    }

    @GetMapping("/getGeneralNews")
    @ResponseBody
    public List<News> getGeneralNews(@RequestParam int id) {
        if (!news.containsKey(NewsType.GENERAL)) return new LinkedList<>();
        List<News> list = news.get(NewsType.GENERAL).get(id);
        return list == null ? new LinkedList<>() : list;
    }

    @GetMapping("/getScienceNews")
    @ResponseBody
    public List<News> getScienceNews(@RequestParam int id) {
        if (!news.containsKey(NewsType.SCIENCE)) return new LinkedList<>();
        List<News> list = news.get(NewsType.SCIENCE).get(id);
        return list == null ? new LinkedList<>() : list;
    }

    @GetMapping("/getBusinessNews")
    @ResponseBody
    public List<News> getBusinessNews(@RequestParam int id) {
        if (!news.containsKey(NewsType.BUSINESS)) return new LinkedList<>();
        List<News> list = news.get(NewsType.BUSINESS).get(id);
        return list == null ? new LinkedList<>() : list;
    }

    @GetMapping("/getSportsNews")
    @ResponseBody
    public List<News> getSportsNews(@RequestParam int id) {
        if (!news.containsKey(NewsType.SPORTS)) return new LinkedList<>();
        List<News> list = news.get(NewsType.SPORTS).get(id);
        return list == null ? new LinkedList<>() : list;
    }

    @GetMapping("/getTechnologyNews")
    @ResponseBody
    public List<News> getTechnologyNews(@RequestParam int id) {
        if (!news.containsKey(NewsType.TECHNOLOGY)) return new LinkedList<>();
        List<News> list = news.get(NewsType.TECHNOLOGY).get(id);
        return list == null ? new LinkedList<>() : list;
    }

    @GetMapping("/getEntertainmentNews")
    @ResponseBody
    public List<News> getEntertainmentNews(@RequestParam int id) {
        if (!news.containsKey(NewsType.ENTERTAINMENT)) return new LinkedList<>();
        List<News> list = news.get(NewsType.ENTERTAINMENT).get(id);
        return list == null ? new LinkedList<>() : list;
    }

    @GetMapping("/getHealthNews")
    @ResponseBody
    public List<News> getHealthNews(@RequestParam int id) {
        if (!news.containsKey(NewsType.HEALTH)) return new LinkedList<>();
        List<News> list = news.get(NewsType.HEALTH).get(id);
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
