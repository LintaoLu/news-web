package com.llu25.paperweb.services;

import com.llu25.paperweb.Utils;
import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;
import java.util.List;
import java.util.stream.Collectors;

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

    public List<String> searchTweets() throws TwitterException {
        Query query = new Query("china");
        query.lang("en");
        QueryResult result = twitter.search(query);

        return result.getTweets().stream()
                .map(Status::getText)
                .collect(Collectors.toList());
    }

    public static void main(String[] args) throws TwitterException {
       TwitterService twitterService = new TwitterService();
       for (String str : twitterService.searchTweets())
            System.out.println(str);
    }
}
