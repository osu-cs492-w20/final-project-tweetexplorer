package com.example.android.tweetexplorer.data;

import android.os.AsyncTask;

import com.example.android.tweetexplorer.utils.NetworkUtils;
import com.example.android.tweetexplorer.utils.OpenWeatherMapUtils;

import java.io.IOException;
import java.util.ArrayList;

public class OpenWeatherMapAsyncTask extends AsyncTask<String, Void, String> {
    private Callback mCallback;

    public interface Callback{
        void onSearchFinished(ArrayList<ForecastItem> searchResults);
    }

    public OpenWeatherMapAsyncTask(Callback callback){
        mCallback = callback;
    }

//    @Override
//    protected void onPreExecute() {
//        super.onPreExecute();
//    }

    @Override
    protected String doInBackground(String... params) {
        String openWeatherMapURL = params[0];
        String forecastJSON = null;
        try {
            forecastJSON = NetworkUtils.doHTTPGet(openWeatherMapURL);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return forecastJSON;
    }

    @Override
    protected void onPostExecute(String s){
        ArrayList<ForecastItem> forecastJSON = null;
        if(s != null){
            forecastJSON = OpenWeatherMapUtils.parseForecastJSON(s);
        }
        mCallback.onSearchFinished(forecastJSON);
    }
}
