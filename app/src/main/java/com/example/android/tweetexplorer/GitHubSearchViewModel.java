package com.example.android.tweetexplorer;

import com.example.android.tweetexplorer.data.GitHubRepo;
import com.example.android.tweetexplorer.data.GitHubSearchRepository;
import com.example.android.tweetexplorer.data.Status;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

public class GitHubSearchViewModel extends ViewModel {
    private GitHubSearchRepository mRepository;
    private LiveData<List<GitHubRepo>> mSearchResults;
    private LiveData<Status> mLoadingStatus;

    public GitHubSearchViewModel() {
        mRepository = new GitHubSearchRepository();
        mSearchResults = mRepository.getSearchResults();
        mLoadingStatus = mRepository.getLoadingStatus();
    }

    public void loadSearchResults(String query, String sort, String language, String user,
                                  boolean searchInName, boolean searchInDescription,
                                  boolean searchInReadme) {
        mRepository.loadSearchResults(query, sort, language, user, searchInName,
                searchInDescription, searchInReadme);
    }

    public LiveData<List<GitHubRepo>> getSearchResults() {
        return mSearchResults;
    }

    public LiveData<Status> getLoadingStatus() {
        return mLoadingStatus;
    }
}
