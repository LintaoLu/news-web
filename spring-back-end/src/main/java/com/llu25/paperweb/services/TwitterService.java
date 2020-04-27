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
        int counter = 0;
        for (String str : keywords) {
            if (counter >= Utils.keywordShortcut) break;
            String[] words = str.split(" ");
            for (String word : words) {
                if (counter >= Utils.keywordShortcut) break;
                sb.append(word).append('&');
                counter++;
            }
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
