package com.example.android.tweetexplorer.utils;

import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.example.android.tweetexplorer.data.Tweet;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;

import androidx.annotation.Nullable;

public class TwitterUtils {
    private static final String TAG = TwitterUtils.class.getSimpleName();

    // private final static String TW_TIMELINE_BASE_URL = "https://api.twitter.com/1.1/statuses/user_timeline.json";
    private final static String TW_TIMELINE_BASE_URL = "http://people.oregonstate.edu/~golletzj/timeline.json";
    private final static String TW_HANDLE_QUERY_PARAM = "screen_name";
    private final static String TW_COUNT_QUERY_PARAM = "count";
    private final static String TW_COUNT_QUERY_VALUE = "2";
    private final static String TW_RETWEETS_QUERY_PARAM = "include_rts";
    private final static String TW_RETWEETS_QUERY_VALUE = "false";
    private final static String TW_ENTITIES_QUERY_PARAM = "include_entities";
    private final static String TW_ENTITIES_QUERY_VALUE = "false";
    private final static String TW_NOREPLIES_QUERY_PARAM = "exclude_replies";
    private final static String TW_NOREPLIES_QUERY_VALUE = "true";

    // private final static String TW_AUTH_HEADER_KEY = "authorization: OAuth oauth_consumer_key=";
    // private final static String TW_API_KEY = "";

    public static String buildTimelineURL(String screenName) {
        return Uri.parse(TW_TIMELINE_BASE_URL).buildUpon()
                .appendQueryParameter(TW_HANDLE_QUERY_PARAM, screenName)
                .appendQueryParameter(TW_COUNT_QUERY_PARAM, TW_COUNT_QUERY_VALUE)
                .appendQueryParameter(TW_RETWEETS_QUERY_PARAM, TW_RETWEETS_QUERY_VALUE)
                .appendQueryParameter(TW_ENTITIES_QUERY_PARAM, TW_ENTITIES_QUERY_VALUE)
                .appendQueryParameter(TW_NOREPLIES_QUERY_PARAM, TW_NOREPLIES_QUERY_VALUE)
                .build()
                .toString();
    }

    public static ArrayList<Tweet> parseTimelineResults(String json) {
        Gson gson = new Gson();
        Tweet[] tweetsList = gson.fromJson(json, Tweet[].class);
        Log.d(TAG, Arrays.toString(tweetsList));
        assert(false);
        if (tweetsList != null && tweetsList.length != 0) {
            ArrayList<Tweet> tweets = new ArrayList<>();
            for (Tweet tweet : tweetsList) {
                tweets.add(tweet);
            }

            return tweets;
        } else {
            return null;
        }
    }
}
