package com.llu25.paperweb;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.llu25.paperweb.datastructures.FIFO;
import com.llu25.paperweb.datastructures.LRU;
import com.llu25.paperweb.datastructures.Pair;
import com.llu25.paperweb.services.AutoCompleteService;
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
    private LRU<String, Pair<Long,  Map<Integer, List<News>>>> searchHistory;
    private List<Pair<String, String>> source;
    public final UpdateNewsService us;
    public final KeyWordExtractionService ks;
    public final TwitterService ts;
    public final AutoCompleteService as;
    public final Set<String> basicNewsTypes, sourceId;

    public PaperWebApplication() throws IOException {
        news = new ConcurrentHashMap<>();
        // get basic news
        String[] types = {"general", "science", "business", "sports", "health", "technology", "entertainment"};
        basicNewsTypes = new HashSet<>(Arrays.asList(types));
        for (String type : basicNewsTypes) news.put(type, new FIFO<>(Utils.FIFOSize));
        searchHistory = new LRU<>(Utils.LRUSize);
        // get source
        source = Utils.getSource();
        sourceId = new HashSet<>();
        for (Pair<String, String> pair : source) sourceId.add(pair.getKey());
        // initialize services
        us = new UpdateNewsService(this);
        ks = new KeyWordExtractionService();
        ts = new TwitterService();
        as = new AutoCompleteService();
        // start update services
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
        if (basicNewsTypes.contains(keyword)) list = news.get(keyword).get(id);
        else {
            if (!searchHistory.containsKey(keyword) ||
                    System.currentTimeMillis() - searchHistory.get(keyword).getKey() > Utils.updatePeriod) {
                Map<Integer, List<News>> thisNews = Utils.parseNewsJson(getNewsJson(keyword));
                searchHistory.set(keyword, new Pair<>(System.currentTimeMillis(), thisNews));
            }
            list = searchHistory.get(keyword).getValue().get(id);
        }
        return list == null ? new LinkedList<>() : list;
    }

    public String getNewsJson(String keyword) throws IOException {
        if (basicNewsTypes.contains(keyword)) {
            return Utils.doGet("http://newsapi.org/v2/top-headlines?country=us&category=" +
                    keyword + "&apiKey=" + Utils.news_api_key);
        }
        else if (sourceId.contains(keyword)) {
            return Utils.doGet("http://newsapi.org/v2/top-headlines?sources=" +
                    keyword + "&apiKey=" + Utils.news_api_key);
        }
        return Utils.doGet("https://newsapi.org/v2/everything?q=" + keyword +
                "&language=en&sortBy=publishedAt&apiKey=" + Utils.news_api_key);
    }

    @GetMapping("/getTweets")
    @ResponseBody
    public List<Status> getTweets(@RequestParam String content) throws MonkeyLearnException, TwitterException {
        content = content.replaceAll("%20", " ");
        List<String> keyWords = ks.getKeyWordsFromInternet(new String[] {content}).get(0);
        return ts.searchTweets(keyWords);
    }

    @GetMapping("/getSource")
    @ResponseBody
    public List<Pair<String, String>> getSource(){
        return source;
    }

    @GetMapping("/getSuggestions")
    @ResponseBody
    public List<String> getSuggestions(@RequestParam String content){
        return as.search(content);
    }

    public Map<String, FIFO<List<News>>> getNews() { return news; }

    public static void main(String[] args) {
        SpringApplication.run(PaperWebApplication.class, args);
    }
}
