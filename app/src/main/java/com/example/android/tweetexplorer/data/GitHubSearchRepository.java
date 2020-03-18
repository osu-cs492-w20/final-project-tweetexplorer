package com.example.android.tweetexplorer.data;

import android.text.TextUtils;
import android.util.Log;

import com.example.android.tweetexplorer.utils.GitHubUtils;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class GitHubSearchRepository implements GitHubSearchAsyncTask.Callback {
    private static final String TAG = GitHubSearchRepository.class.getSimpleName();
    private MutableLiveData<List<GitHubRepo>> mSearchResults;
    private MutableLiveData<Status> mLoadingStatus;

    private String mCurrentQuery;
    private String mCurrentSort;
    private String mCurrentLanguage;
    private String mCurrentUser;
    private boolean mCurrentSearchInName;
    private boolean mCurrentSearchInDescription;
    private boolean mCurrentSearchInReadme;

    public GitHubSearchRepository() {
        mSearchResults = new MutableLiveData<>();
        mSearchResults.setValue(null);

        mLoadingStatus = new MutableLiveData<>();
        mLoadingStatus.setValue(Status.SUCCESS);
    }

    public LiveData<List<GitHubRepo>> getSearchResults() {
        return mSearchResults;
    }

    public LiveData<Status> getLoadingStatus() {
        return mLoadingStatus;
    }

    @Override
    public void onSearchFinished(List<GitHubRepo> searchResults) {
        mSearchResults.setValue(searchResults);
        if (searchResults != null) {
            mLoadingStatus.setValue(Status.SUCCESS);
        } else {
            mLoadingStatus.setValue(Status.ERROR);
        }
    }

    private boolean shouldExecuteSearch(String query, String sort, String language, String user,
                                        boolean searchInName, boolean searchInDescription,
                                        boolean searchInReadme) {
        return !TextUtils.equals(query, mCurrentQuery)
                || !TextUtils.equals(sort, mCurrentSort)
                || !TextUtils.equals(language, mCurrentLanguage)
                || !TextUtils.equals(user, mCurrentUser)
                || mCurrentSearchInName != searchInName
                || mCurrentSearchInDescription != searchInDescription
                || mCurrentSearchInReadme != searchInReadme;
    }

    public void loadSearchResults(String query, String sort, String language, String user,
                                  boolean searchInName, boolean searchInDescription,
                                  boolean searchInReadme) {
        if (shouldExecuteSearch(query, sort, language, user, searchInName, searchInDescription, searchInReadme)) {
            mCurrentQuery = query;
            mCurrentSort = sort;
            mCurrentLanguage = language;
            mCurrentUser = user;
            mCurrentSearchInName = searchInName;
            mCurrentSearchInDescription = searchInDescription;
            mCurrentSearchInReadme = searchInReadme;
            String url = GitHubUtils.buildGitHubSearchURL(query, sort, language, user, searchInName,
                    searchInDescription, searchInReadme);
            mSearchResults.setValue(null);
            Log.d(TAG, "executing search with url: " + url);
            mLoadingStatus.setValue(Status.LOADING);
            new GitHubSearchAsyncTask(this).execute(url);
        } else {
            Log.d(TAG, "using cached search results");
        }
    }
}
