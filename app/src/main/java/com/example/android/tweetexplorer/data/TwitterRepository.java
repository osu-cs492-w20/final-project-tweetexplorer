package com.example.android.tweetexplorer.data;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import android.text.TextUtils;
import android.util.Log;


import com.example.android.tweetexplorer.utils.TwitterUtils;

import java.util.ArrayList;

public class TwitterRepository implements TwitterAsyncTask.Callback{
    private static final String TAG = TwitterRepository.class.getSimpleName();
    private MutableLiveData<ArrayList<Tweet>> mSearchResults;
    private MutableLiveData<Status> mLoadingStatus;
    private String mCurrentLocation;
    private String mCurrentUnits;

    public TwitterRepository() {
        mSearchResults = new MutableLiveData<>();
        mSearchResults.setValue(null);

        mLoadingStatus = new MutableLiveData<>();
        mLoadingStatus.setValue(Status.SUCCESS);

        mCurrentLocation = null;
        mCurrentUnits = null;
    }

    public LiveData<ArrayList<Tweet>> getSearchResults() {
        return mSearchResults;
    }

    public LiveData<Status> getLoadingStatus(){
        return mLoadingStatus;
    }

    @Override
    public void onSearchFinished(ArrayList<Tweet> searchResults){
        mSearchResults.setValue(searchResults);
        if(searchResults != null){
            mLoadingStatus.setValue(Status.SUCCESS);
        } else {
            mLoadingStatus.setValue(Status.ERROR);
        }
    }

    private boolean shouldExecuteSearch(String location, String units){
        return !TextUtils.equals(location, mCurrentLocation) || !TextUtils.equals(units, mCurrentUnits);
    }

    public void loadSearchResults(String location, String units){
        if(shouldExecuteSearch(location, units)){
            mCurrentLocation = location;
            mCurrentUnits = units;
            String url = TwitterUtils.buildTimelineURL("barackobama");
            mSearchResults.setValue(null);
            Log.d(TAG, "executing search with url: " + url);
            mLoadingStatus.setValue(Status.LOADING);
            new TwitterAsyncTask(this).execute(url);
        } else {
            Log.d(TAG, "using cached search results");
        }
    }
}
