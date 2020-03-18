package com.example.android.tweetexplorer.data;

import java.io.Serializable;

public class TweetPlace implements Serializable {
    public String place_type;
    public String name;
    public String full_name;
    public TweetBoundingBox bounding_box;
}
