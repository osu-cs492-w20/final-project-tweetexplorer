package com.example.android.lifecycleweather;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.android.lifecycleweather.data.ForecastItem;
import com.example.android.lifecycleweather.data.OpenWeatherMapRepository;
import com.example.android.lifecycleweather.data.Status;

import java.util.ArrayList;
import java.util.List;

public class OpenWeatherMapViewModel extends ViewModel {
    private OpenWeatherMapRepository mRepository;
    private LiveData<ArrayList<ForecastItem>> mSearchResults;
    private LiveData<Status> mLoadingStatus;

    public OpenWeatherMapViewModel(){
        mRepository = new OpenWeatherMapRepository();
        mSearchResults = mRepository.getSearchResults();
        mLoadingStatus = mRepository.getLoadingStatus();
    }

    public void loadSearchResults(String location, String units){
        mRepository.loadSearchResults(location, units);
    }

    public LiveData<ArrayList<ForecastItem>> getSearchResults() {
        return mSearchResults;
    }

    public LiveData<Status> getLoadingStatus(){
        return mLoadingStatus;
    }
}
