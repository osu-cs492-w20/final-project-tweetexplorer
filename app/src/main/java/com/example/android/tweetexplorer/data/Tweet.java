package com.example.android.tweetexplorer.data;

import java.io.Serializable;
import java.util.Date;

public class Tweet implements Serializable {
    public String created_at;
    public String id_str;
    public String text;
    public TweetUser user;
    public TweetGeo geo;
    public TweetGeo coordinates;
    public TweetPlace place;
}