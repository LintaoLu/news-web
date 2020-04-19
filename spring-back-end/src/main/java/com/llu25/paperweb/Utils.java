package com.llu25.paperweb;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import java.io.IOException;
import java.util.*;

public class Utils {

    public static int newsPerList = 3;
    public static int LRUSize = 100, FIFOSize = 20;
    public static final String news_api_key = "ce03d2af6ccc4b5e9fc475540b46f170";
    public static final Set<String> basicNewsTypes;

    static {
        String[] types = {"general", "science", "business", "sports", "health", "technology", "entertainment" };
        basicNewsTypes = new HashSet<>(Arrays.asList(types));
    }

    public static String doGet(String request) throws IOException {
        String ans = null;
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

    public static Map<Integer, List<News>> parseNewsJson(String json) {
        Map<Integer, List<News>> map = new LinkedHashMap<>();
        if (json == null || json.equals("")) return map;
        List<News> list = new LinkedList<>();
        int counter = 0;
        JsonArray jsonArray = new JsonParser().parse(json).getAsJsonObject().get("articles").getAsJsonArray();
        for (JsonElement news : jsonArray) {
            if (counter != 0 && counter % Utils.newsPerList == 0) {
                map.put(counter / Utils.newsPerList, list);
                list = new LinkedList<>();
            }
            JsonObject newsObj = news.getAsJsonObject();
            String source = newsObj.getAsJsonObject("source").get("name").isJsonNull() ? "" :
                    newsObj.getAsJsonObject("source").get("name").getAsString();
            String author = newsObj.get("author").isJsonNull() ? "Unknown" : newsObj.get("author").getAsString();
            String title = newsObj.get("title").isJsonNull() ? "No title" : newsObj.get("title").getAsString();
            String description = newsObj.get("description").isJsonNull() ? "" : newsObj.get("description").getAsString();
            String url = newsObj.get("url").isJsonNull() ? "#" : newsObj.get("url").getAsString();
            String urlToImage = newsObj.get("urlToImage").isJsonNull() ? "http://localhost:4200/assets/images/img_not_find.jpg" :
                    newsObj.get("urlToImage").getAsString();
            String publishedAt = newsObj.get("publishedAt").isJsonNull() ? "" : newsObj.get("publishedAt").getAsString();
            list.add(new News(counter, source, author, title, description, url, urlToImage, publishedAt));
            counter++;
        }
        if (list.size() != 0) map.put(counter / Utils.newsPerList + 1, list);
        return map;
    }

    public static String getJson(String keyword) throws IOException {
        return doGet("https://newsapi.org/v2/everything?q=" + keyword + "&language=en&sortBy=publishedAt&apiKey=" + Utils.news_api_key);
    }
}
