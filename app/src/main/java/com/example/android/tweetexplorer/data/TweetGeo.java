package com.example.android.tweetexplorer.data;

import java.io.Serializable;
import java.util.List;

public class TweetGeo implements Serializable {
    public String type;
    public List<Integer> coordinates;
}