package com.llu25.paperweb;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.llu25.paperweb.components.News;
import com.llu25.paperweb.datastructures.Pair;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import java.io.*;
import java.util.*;

public class Utils {

    public static int newsPerList = 3, keyWordsPerNews = 3, keywordShortcut = 3,
            LRUSize = 500, FIFOSize = 20, autocompleteNum = 10;
    public static final String news_api_key, monkey_learn_api_key;
    public static final List<String> twitter_api_keys;

    public static long updatePeriod = 9000000 ; // 2.5h

    static {
        news_api_key = readAPIKey("news_api_key.txt").get(0);
        monkey_learn_api_key = readAPIKey("monkey_learn_api_key.txt").get(0);
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

    public static Map<Integer, List<News>> parseNewsJson(String json) {
        Map<Integer, List<News>> map = new LinkedHashMap<>();
        List<String> text = new LinkedList<>();
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

            // drop invalid news
            if (title.equals("No title") && description.equals("")) continue;

            if (!title.equals("No title")) text .add(title);
            else text.add(description);

            list.add(new News(counter, source, author, title, description, url, urlToImage, publishedAt, new ArrayList<>(), new ArrayList<>()));
            counter++;
        }
        // put the remaining news to map
        if (list.size() != 0) map.put(counter / Utils.newsPerList + 1, list);

        return map;
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

    public static List<Pair<String, String>> getSource() throws IOException {
        String request = "https://newsapi.org/v2/sources?language=en&apiKey=" + news_api_key;
        String json = doGet(request);
        List<Pair<String, String>> list = new ArrayList<>();
        if (json == null || json.length() == 0) return list;
        JsonArray jsonArray = new JsonParser().parse(json).getAsJsonObject().get("sources").getAsJsonArray();
        for (JsonElement source : jsonArray) {
            JsonObject sourceObj = source.getAsJsonObject();
            String sourceId = sourceObj.get("id").isJsonNull() ? null : sourceObj.get("id").getAsString();
            String sourceName = sourceObj.get("name").isJsonNull() ? null : sourceObj.get("name").getAsString();
            if (sourceId == null || sourceName == null) continue;
            list.add(new Pair<>(sourceId, sourceName));
        }
        return list;
    }

    public static void main(String[] args) throws IOException {
        List<Pair<String, String>> list = getSource();
        System.out.println(list);
        System.out.println(list.size());
    }
}
