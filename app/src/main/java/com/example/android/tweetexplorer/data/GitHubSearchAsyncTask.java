package com.example.android.tweetexplorer.data;

import android.os.AsyncTask;

import com.example.android.tweetexplorer.utils.GitHubUtils;
import com.example.android.tweetexplorer.utils.NetworkUtils;

import java.io.IOException;
import java.util.List;

public class GitHubSearchAsyncTask extends AsyncTask<String, Void, String> {
    private Callback mCallback;

    public interface Callback {
        void onSearchFinished(List<GitHubRepo> searchResults);
    }

    public GitHubSearchAsyncTask(Callback callback) {
        mCallback = callback;
    }

    @Override
    protected String doInBackground(String... strings) {
        String url = strings[0];
        String searchResults = null;
        try {
            searchResults = NetworkUtils.doHttpGet(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return searchResults;
    }

    @Override
    protected void onPostExecute(String s) {
        List<GitHubRepo> searchResults = null;
        if (s != null) {
            searchResults = GitHubUtils.parseGitHubSearchResults(s);
        }
        mCallback.onSearchFinished(searchResults);
    }
}
