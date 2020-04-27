package com.llu25.paperweb;

import com.llu25.paperweb.datastructures.FIFO;
import com.llu25.paperweb.datastructures.LRU;
import com.llu25.paperweb.services.KeyWordExtractionService;
import com.llu25.paperweb.services.TwitterService;
import com.llu25.paperweb.services.UpdateNewsService;
import com.monkeylearn.MonkeyLearnException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;
import twitter4j.Status;
import twitter4j.TwitterException;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@CrossOrigin(origins = "*")
@RestController
@SpringBootApplication
public class PaperWebApplication {

    private Map<String, FIFO<List<News>>> news;
    private LRU<String, Map<Integer, List<News>>> searchHistory;
    public final UpdateNewsService us;
    public final KeyWordExtractionService ks;
    public final TwitterService ts;

    public PaperWebApplication() {
        news = new ConcurrentHashMap<>();
        for (String type : Utils.basicNewsTypes) news.put(type, new FIFO<>(Utils.FIFOSize));
        searchHistory = new LRU<>(Utils.LRUSize);
        us = new UpdateNewsService(this);
        ks = new KeyWordExtractionService();
        ts = new TwitterService();
        Timer timer = new Timer();
        timer.schedule(us, 0, Utils.updatePeriod);
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
                Map<Integer, List<News>> thisNews = Utils.parseNewsJson(Utils.getNewsJson(keyword));
                searchHistory.set(keyword, thisNews);
            }
            list = searchHistory.get(keyword).get(id);
        }
        return list == null ? new LinkedList<>() : list;
    }

    @GetMapping("/getTweets")
    @ResponseBody
    public List<Status> getTweets(@RequestParam String content) throws MonkeyLearnException, TwitterException {
        content = content.replaceAll("%20", " ");
        System.out.println(content);
        List<String> keyWords = ks.getKeyWordsFromInternet(new String[] {content}).get(0);
        return ts.searchTweets(keyWords);
    }

    public Map<String, FIFO<List<News>>> getNews() { return news; }

    public LRU<String, Map<Integer, List<News>>> getSearchHistory() { return searchHistory; }

    public static void main(String[] args) {
        SpringApplication.run(PaperWebApplication.class, args);
    }
}
