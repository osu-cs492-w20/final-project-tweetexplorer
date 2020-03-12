package com.example.android.lifecycleweather.data;

import android.os.AsyncTask;

import com.example.android.lifecycleweather.utils.NetworkUtils;
import com.example.android.lifecycleweather.utils.OpenWeatherMapUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.security.auth.callback.Callback;

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
