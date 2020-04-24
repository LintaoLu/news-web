package com.llu25.paperweb;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.llu25.paperweb.services.KeyWordExtractionService;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import java.io.*;
import java.util.*;

public class Utils {

    public static int newsPerList = 3, keyWordsPerNews = 3;
    public static int LRUSize = 100, FIFOSize = 20;
    public static final String news_api_key;
    public static final List<String> twitter_api_keys;
    public static final Set<String> basicNewsTypes;
    public static long updatePeriod = 10800000; //180 Min

    static {
        String[] types = {"general", "science", "business", "sports", "health", "technology", "entertainment" };
        basicNewsTypes = new HashSet<>(Arrays.asList(types));
        news_api_key = readAPIKey("news_api_key.txt").get(0);
        twitter_api_keys = readAPIKey("twitter_api_key.txt");
    }

    public static String doGet(String request) throws IOException {
        String ans;
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(request);
        CloseableHttpResponse response = httpclient.execute(httpGet);
        try {
            System.out.println(response.getStatusLine());
            HttpEntity entity = response.getEntity();
            // do something useful with the response body
            // and ensure it is fully consumed
            ans = EntityUtils.toString(entity);
            EntityUtils.consume(entity);
        } finally {
            response.close();
        }
        return ans;
    }

    public static Map<Integer, List<News>> parseNewsJson(KeyWordExtractionService keyWordExtractionService,
                                                         boolean enableService, String json) throws IOException {
        Map<Integer, List<News>> map = new LinkedHashMap<>();
        if (json == null || json.equals("")) return map;
        List<News> list = new LinkedList<>();
        int counter = 0;
        JsonArray jsonArray = new JsonParser().parse(json).getAsJsonObject().get("articles").getAsJsonArray();
        for (JsonElement news : jsonArray) {
            if (counter != 0 && counter % newsPerList == 0) {
                map.put(counter / newsPerList, list);
                list = new LinkedList<>();
            }

            JsonObject newsObj = news.getAsJsonObject();
            String source = newsObj.getAsJsonObject("source").get("name").isJsonNull() ? "" :
                    newsObj.getAsJsonObject("source").get("name").getAsString();
            String author = newsObj.get("author").isJsonNull() ? "Unknown" : newsObj.get("author").getAsString();
            String title = newsObj.get("title").isJsonNull() ? "No title" : newsObj.get("title").getAsString();
            String description = newsObj.get("description").isJsonNull() ? "" : newsObj.get("description").getAsString();
            String url = newsObj.get("url").isJsonNull() ? "#" : newsObj.get("url").getAsString();
            String urlToImage = newsObj.get("urlToImage").isJsonNull() ? "https://lintaolu.com/assets/images/img_not_find.jpg" :
                    newsObj.get("urlToImage").getAsString();
            String publishedAt = newsObj.get("publishedAt").isJsonNull() ? "" : newsObj.get("publishedAt").getAsString();

            List<String> keyWords = new ArrayList<>();
//            if (enableService) {
//                if (!title.equals("No title")) keyWords = keyWordExtractionService.getKeyWordsFromLocal(title);
//                else if (!description.equals("")) keyWords = keyWordExtractionService.getKeyWordsFromLocal(description);
//                keyWords = keyWords.subList(0, Math.min(keyWords.size(), keyWordsPerNews));
//                System.out.println(keyWords);
//            }

            list.add(new News(counter, source, author, title, description, url, urlToImage, publishedAt, keyWords));
            counter++;
        }
        if (list.size() != 0) map.put(counter / Utils.newsPerList + 1, list);
        return map;
    }

    public static String getNewsJson(String keyword) throws IOException {
        if (basicNewsTypes.contains(keyword)) {
            return doGet("http://newsapi.org/v2/top-headlines?category=" +
                    keyword + "&language=en&apiKey=" + news_api_key);
        }
        return doGet("https://newsapi.org/v2/everything?q=" + keyword +
                "&language=en&sortBy=publishedAt&apiKey=" + news_api_key);
    }

    private static List<String> readAPIKey(String path) {
        List<String> list = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = reader.readLine()) != null) {
                list.add(line);
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return list;
    }

    public static void generateText(String log, String path) throws IOException {
        File logFile = new File(path);
        logFile.createNewFile(); // if file already exists will do nothing
        try {
            BufferedWriter writer = new BufferedWriter(
                    new FileWriter(path, false)  //Set true for append mode
            );
            writer.write(log);
            writer.close();
        } catch (Exception e) { e.printStackTrace(); }
    }
}
