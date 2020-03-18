package com.example.android.tweetexplorer;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.android.tweetexplorer.data.Tweet;
import com.example.android.tweetexplorer.data.TwitterRepository;
import com.example.android.tweetexplorer.data.Status;

import java.util.ArrayList;

public class TwitterViewModel extends ViewModel {
    private TwitterRepository mRepository;
    private LiveData<ArrayList<Tweet>> mSearchResults;
    private LiveData<Status> mLoadingStatus;

    public TwitterViewModel(){
        mRepository = new TwitterRepository();
        mSearchResults = mRepository.getSearchResults();
        mLoadingStatus = mRepository.getLoadingStatus();
    }

    public void loadSearchResults(String location, String units){
        mRepository.loadSearchResults(location, units);
    }

    public LiveData<ArrayList<Tweet>> getSearchResults() {
        return mSearchResults;
    }

    public LiveData<Status> getLoadingStatus(){
        return mLoadingStatus;
    }
}
