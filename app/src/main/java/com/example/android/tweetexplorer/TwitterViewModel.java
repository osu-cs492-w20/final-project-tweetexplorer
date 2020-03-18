package com.example.android.tweetexplorer;

import com.example.android.tweetexplorer.data.Tweet;
import com.example.android.tweetexplorer.data.TweetRepository;
import com.example.android.tweetexplorer.data.Status;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

public class TwitterViewModel extends ViewModel {
    private TweetRepository mRepository;
    private LiveData<List<Tweet>> mSearchResults;
    private LiveData<Status> mLoadingStatus;

    public TwitterViewModel() {
        mRepository = new TweetRepository();
        mSearchResults = mRepository.getSearchResults();
        mLoadingStatus = mRepository.getLoadingStatus();
    }

    public void loadSearchResults(String screenName) {
        mRepository.loadSearchResults(screenName);
    }

    public LiveData<List<Tweet>> getSearchResults() {
        return mSearchResults;
    }

    public LiveData<Status> getLoadingStatus() {
        return mLoadingStatus;
    }
}
