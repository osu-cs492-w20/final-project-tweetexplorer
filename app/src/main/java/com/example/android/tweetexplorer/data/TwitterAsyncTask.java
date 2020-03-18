package com.example.android.tweetexplorer.data;

import android.os.AsyncTask;

import com.example.android.tweetexplorer.utils.NetworkUtils;
import com.example.android.tweetexplorer.utils.TwitterUtils;

import java.io.IOException;
import java.util.ArrayList;

public class TwitterAsyncTask extends AsyncTask<String, Void, String> {
    private Callback mCallback;

    public interface Callback{
        void onSearchFinished(ArrayList<Tweet> searchResults);
    }

    public TwitterAsyncTask(Callback callback){
        mCallback = callback;
    }

//    @Override
//    protected void onPreExecute() {
//        super.onPreExecute();
//    }

    @Override
    protected String doInBackground(String... params) {
        String twitterURL = params[0];
        String timelineJSON = null;
        try {
            timelineJSON = NetworkUtils.doHTTPGet(twitterURL);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return timelineJSON;
    }

    @Override
    protected void onPostExecute(String s){
        ArrayList<Tweet> timelineJSON = null;
        if(s != null){
            timelineJSON = TwitterUtils.parseTimelineJSON(s);
        }
        mCallback.onSearchFinished(timelineJSON);
    }
}
