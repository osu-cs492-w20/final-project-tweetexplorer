package com.example.android.tweetexplorer.data;

import android.text.TextUtils;
import android.util.Log;

import com.example.android.tweetexplorer.utils.TwitterUtils;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class TweetRepository implements TwitterAsyncTask.Callback {
    private static final String TAG = TweetRepository.class.getSimpleName();
    private MutableLiveData<List<Tweet>> mTweetList;
    private MutableLiveData<Status> mLoadingStatus;

    private String mCurrentScreenName;

    public TweetRepository() {
        mTweetList = new MutableLiveData<>();
        mTweetList.setValue(null);

        mLoadingStatus = new MutableLiveData<>();
        mLoadingStatus.setValue(Status.SUCCESS);
    }

    public LiveData<List<Tweet>> getSearchResults() {
        return mTweetList;
    }

    public LiveData<Status> getLoadingStatus() {
        return mLoadingStatus;
    }

    @Override
    public void onSearchFinished(List<Tweet> tweetList) {
        mTweetList.setValue(tweetList);
        if (tweetList != null) {
            mLoadingStatus.setValue(Status.SUCCESS);
        } else {
            mLoadingStatus.setValue(Status.ERROR);
        }
    }

    private boolean shouldExecuteSearch(String screenName) {
        return !TextUtils.equals(screenName, mCurrentScreenName);
    }

    public void loadSearchResults(String screenName) {
        if (shouldExecuteSearch(screenName)) {
            mCurrentScreenName = screenName;
            String url = TwitterUtils.buildTimelineURL(screenName);
            mTweetList.setValue(null);
            Log.d(TAG, "executing search with url: " + url);
            mLoadingStatus.setValue(Status.LOADING);
            new TwitterAsyncTask(this).execute(url);
        } else {
            Log.d(TAG, "using cached search results");
        }
    }
}
