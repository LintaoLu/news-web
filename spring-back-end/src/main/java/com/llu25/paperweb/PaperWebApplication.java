package com.llu25.paperweb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.util.*;

@CrossOrigin(origins = "*")
@RestController
@SpringBootApplication
public class PaperWebApplication {

    private Map<NewsType, LRU<List<News>>> news;
    private Map<String, Pair<String, LRU<List<News>>>> searchHistory;

    public PaperWebApplication() {
        news = new HashMap<>();
        for (NewsType type : NewsType.values()) news.put(type, new LRU<>(Utils.LRUSize));
        searchHistory = new HashMap<>();
        Timer timer = new Timer();
        timer.schedule(new UpdateNewsService(this), 0, 10000000); //1000 Min
    }

    @GetMapping("/getNews")
    @ResponseBody
    public List<News> getNews(@RequestParam int id, @RequestParam String ip, @RequestParam String keyword) throws IOException {
        System.out.println("accept request " + id + " " + ip + " " + keyword);
        List<News> list = new LinkedList<>();
        if (keyword.equals("")) return list;

        switch (keyword) {
            case "general":
                if (news.containsKey(NewsType.GENERAL)) {
                    list = news.get(NewsType.GENERAL).get(id);
                }
                break;
            case "business":
                if (news.containsKey(NewsType.BUSINESS)) {
                    list = news.get(NewsType.BUSINESS).get(id);
                }
                break;
            case "entertainment":
                if (news.containsKey(NewsType.ENTERTAINMENT)) {
                    list = news.get(NewsType.ENTERTAINMENT).get(id);
                }
                break;
            case "health":
                if (news.containsKey(NewsType.HEALTH)) {
                    list = news.get(NewsType.HEALTH).get(id);
                }
                break;
            case "science":
                if (news.containsKey(NewsType.SCIENCE)) {
                    list = news.get(NewsType.SCIENCE).get(id);
                }
                break;
            case "sports":
                if (news.containsKey(NewsType.SPORTS)) {
                    list = news.get(NewsType.SPORTS).get(id);
                }
                break;
            case "technology":
                if (news.containsKey(NewsType.TECHNOLOGY)) {
                    list = news.get(NewsType.TECHNOLOGY).get(id);
                }
                break;
            default:
                if (!searchHistory.containsKey(ip) || !searchHistory.get(ip).getKey().equals(keyword)) {
                    LRU<List<News>> news = Utils.parseNewsJson(Utils.getJson(keyword));
                    searchHistory.put(ip, new Pair<>(keyword, news));
                }
                list = searchHistory.get(ip).getValue().get(id);
        }
        return list == null ? new LinkedList<>() : list;
    }

    public Map<NewsType, LRU<List<News>>> getNews() {
        return news;
    }

    public Map<String, Pair<String, LRU<List<News>>>> getSearchHistory() {
        return searchHistory;
    }

    public static void main(String[] args) {
        SpringApplication.run(PaperWebApplication.class, args);
    }
}
