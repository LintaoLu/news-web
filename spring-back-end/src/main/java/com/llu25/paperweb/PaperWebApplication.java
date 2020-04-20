package com.llu25.paperweb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@CrossOrigin(origins = "*")
@RestController
@SpringBootApplication
public class PaperWebApplication {

    private Map<String, FIFO<List<News>>> news;
    private LRU<String, Map<Integer, List<News>>> searchHistory;

    public PaperWebApplication() {
        news = new ConcurrentHashMap<>();
        for (String type : Utils.basicNewsTypes) news.put(type, new FIFO<>(Utils.FIFOSize));
        searchHistory = new LRU<>(Utils.LRUSize);
        Timer timer = new Timer();
        timer.schedule(new UpdateNewsService(this), 0, 20000000); //500 Min
    }

    @GetMapping("/getNews")
    @ResponseBody
    public List<News> getNews(@RequestParam int id, @RequestParam String keyword) throws IOException {
        System.out.println("accept request " + id + " " + keyword);
        List<News> list = new LinkedList<>();
        if (keyword.equals("")) return list;
        String[] arr = keyword.split(" ");
        StringBuilder sb = new StringBuilder();
        for(String str : arr) sb.append(str).append('&');
        sb.setLength(sb.length()-1);
        keyword = sb.toString();
        System.out.println(keyword);
        if (Utils.basicNewsTypes.contains(keyword)) list = news.get(keyword).get(id);
        else {
            if (!searchHistory.containsKey(keyword)) {
                Map<Integer, List<News>> thisNews = Utils.parseNewsJson(Utils.getJson(keyword));
                searchHistory.set(keyword, thisNews);
            }
            list = searchHistory.get(keyword).get(id);
        }
        return list == null ? new LinkedList<>() : list;
    }

    public Map<String, FIFO<List<News>>> getNews() { return news; }

    public LRU<String, Map<Integer, List<News>>> getSearchHistory() { return searchHistory; }

    public static void main(String[] args) {
        SpringApplication.run(PaperWebApplication.class, args);
    }
}
