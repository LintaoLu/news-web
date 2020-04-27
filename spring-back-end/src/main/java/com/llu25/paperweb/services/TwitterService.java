package com.llu25.paperweb.services;

import com.llu25.paperweb.Utils;
import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;
import java.util.*;

public class TwitterService {

    private Twitter twitter;

    public TwitterService() {
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true).setOAuthConsumerKey(Utils.twitter_api_keys.get(0))
                .setOAuthConsumerSecret(Utils.twitter_api_keys.get(1))
                .setOAuthAccessToken(Utils.twitter_api_keys.get(2))
                .setOAuthAccessTokenSecret(Utils.twitter_api_keys.get(3))
                .setTweetModeExtended(true);
        twitter = new TwitterFactory(cb.build()).getInstance();
    }

    public List<Status> searchTweets(List<String> keywords) throws TwitterException {
        if (keywords == null || keywords.size() == 0) return new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        Queue<LinkedList<String>> queue = new LinkedList<>();
        for (String str : keywords) queue.offer(new LinkedList<>(Arrays.asList(str.split(" "))));
        int counter = 0;
        while (!queue.isEmpty() && counter < Utils.keywordShortcut) {
            LinkedList<String> list = queue.poll();
            if (list != null && list.size() > 0) {
                sb.append(list.removeFirst()).append('&');
                counter++;
            }
            if (list != null && list.size() != 0) queue.offer(list);
        }
        sb.setLength(sb.length()-1);
        if (sb.length() == 0) return new ArrayList<>();
        Query query = new Query(sb.toString());
        query.lang("en");
        QueryResult result = twitter.search(query);

        List<Status> list = new ArrayList<>();
        Set<String> set = new HashSet<>();
        for (Status status : result.getTweets()) {
            sb.setLength(0);
            String text = status.getText();
            for (char c : text.toCharArray()) {
                if (c == '\n') sb.append(' ');
                else sb.append(c);
            }
            if (set.contains(sb.toString())) continue;
            set.add(sb.toString());
            list.add(status);
        }
        return list;
    }
}
