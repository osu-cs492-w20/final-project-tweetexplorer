package com.example.android.tweetexplorer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.tweetexplorer.data.GitHubRepo;
import com.example.android.tweetexplorer.data.Status;

import java.util.List;

public class MainActivity extends AppCompatActivity implements GitHubSearchAdapter.OnSearchResultClickListener {
    private static final String TAG = MainActivity.class.getSimpleName();

    private RecyclerView mSearchResultsRV;
    private EditText mSearchBoxET;
    private ProgressBar mLoadingIndicatorPB;
    private TextView mErrorMessageTV;
    private GitHubSearchAdapter mGitHubSearchAdapter;

    private GitHubSearchViewModel mViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSearchBoxET = findViewById(R.id.et_search_box);
        mSearchResultsRV = findViewById(R.id.rv_search_results);

        mSearchResultsRV.setLayoutManager(new LinearLayoutManager(this));
        mSearchResultsRV.setHasFixedSize(true);

        mGitHubSearchAdapter = new GitHubSearchAdapter(this);
        mSearchResultsRV.setAdapter(mGitHubSearchAdapter);

        mLoadingIndicatorPB = findViewById(R.id.pb_loading_indicator);
        mErrorMessageTV = findViewById(R.id.tv_error_message);

        mViewModel = new ViewModelProvider(this).get(GitHubSearchViewModel.class);

        mViewModel.getSearchResults().observe(this, new Observer<List<GitHubRepo>>() {
            @Override
            public void onChanged(List<GitHubRepo> gitHubRepos) {
                mGitHubSearchAdapter.updateSearchResults(gitHubRepos);
            }
        });

        mViewModel.getLoadingStatus().observe(this, new Observer<Status>() {
            @Override
            public void onChanged(Status status) {
                if (status == Status.LOADING) {
                    mLoadingIndicatorPB.setVisibility(View.VISIBLE);
                } else if (status == Status.SUCCESS) {
                    mLoadingIndicatorPB.setVisibility(View.INVISIBLE);
                    mSearchResultsRV.setVisibility(View.VISIBLE);
                    mErrorMessageTV.setVisibility(View.INVISIBLE);
                } else {
                    mLoadingIndicatorPB.setVisibility(View.INVISIBLE);
                    mSearchResultsRV.setVisibility(View.INVISIBLE);
                    mErrorMessageTV.setVisibility(View.VISIBLE);
                }
            }
        });

        Button searchButton = findViewById(R.id.btn_search);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchQuery = mSearchBoxET.getText().toString();
                if (!TextUtils.isEmpty(searchQuery)) {
                    doGitHubSearch(searchQuery);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onSearchResultClicked(GitHubRepo repo) {
        Intent intent = new Intent(this, TweetDetailActivity.class);
        intent.putExtra(TweetDetailActivity.EXTRA_GITHUB_REPO, repo);
        startActivity(intent);
    }

    private void doGitHubSearch(String searchQuery) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String sort = preferences.getString(
                getString(R.string.pref_sort_key),
                getString(R.string.pref_sort_default)
        );
        String language = preferences.getString(
                getString(R.string.pref_language_key),
                getString(R.string.pref_language_default)
        );
        String user = preferences.getString(
                getString(R.string.pref_user_key), ""
        );
        boolean searchInName = preferences.getBoolean(
                getString(R.string.pref_in_name_key), true
        );
        boolean searchInDescription = preferences.getBoolean(
                getString(R.string.pref_in_description_key), true
        );
        boolean searchInReadme = preferences.getBoolean(
                getString(R.string.pref_in_readme_key), true
        );
        mViewModel.loadSearchResults(searchQuery, sort, language, user, searchInName,
                searchInDescription, searchInReadme);
    }
}
