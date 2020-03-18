package com.example.android.tweetexplorer.data;

import java.io.Serializable;
import java.util.List;

public class TweetBoundingBox implements Serializable {
    public String type;
    public List<List<List<Integer>>> coordinates;
}
